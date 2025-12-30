package com.vaibhav.icms.project.service;

import java.util.ArrayList;
import java.util.List;

import com.vaibhav.icms.projectmember.entity.ProjectMember;
import com.vaibhav.icms.projectmember.enums.ProjectRole;
import com.vaibhav.icms.projectmember.repository.ProjectMemberRepository;
import com.vaibhav.icms.projectmember.service.ProjectMemberService;
import com.vaibhav.icms.user.entity.User;
import com.vaibhav.icms.user.enums.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.project.dto.CreateProjectRequest;
import com.vaibhav.icms.project.dto.ProjectResponse;
import com.vaibhav.icms.project.dto.ProjectSummaryResponse;
import com.vaibhav.icms.project.dto.UpdateProjectRequest;
import com.vaibhav.icms.project.entity.Project;
import com.vaibhav.icms.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectMemberService projectMemberService;


    //create project
    public ProjectResponse createProject(CreateProjectRequest request){

        Project project = Project.builder()
                                    .name(request.getName())
                                    .clientName(request.getClientName())
                                    .location(request.getLocation())
                                    .startDate(request.getStartDate())
                                    .status(request.getStatus())
                                    .budgetPlanned(request.getBudgetPlanned())
                                    .build();

        projectRepository.save(project);

        return mapToResponse(project);
    }
    // update project
    public ProjectResponse updateProject(Long projectId,UpdateProjectRequest request,User user){
        Project project = projectRepository.findById(projectId)
                                            .orElseThrow(() -> new RuntimeException("Id Not Found" + projectId));

        checkIsProjectManager(projectId, user);

        if(request.getName() != null && !request.getName().isEmpty() ) project.setName(request.getName());
        if(request.getClientName()!=null && !request.getClientName().isEmpty()) project.setClientName(request.getClientName());
        if(request.getLocation()!=null && !request.getLocation().isEmpty()) project.setLocation(request.getLocation());
        if(request.getStartDate()!=null ) project.setStartDate(request.getStartDate());
        if(request.getBudgetPlanned()!=null) project.setBudgetPlanned(request.getBudgetPlanned());
        if(request.getStatus()!=null) project.setStatus(request.getStatus());

        projectRepository.save(project);
        
        return mapToResponse(project);

    }


    // get Project
    public ProjectResponse getProject(Long id){
        Project project = projectRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Project Id doesn't exists" + id));
        return mapToResponse(project);
    }

    //list all projects
    public List<ProjectResponse> listProjects(){
        List<Project> projects = projectRepository.findAll();
        List<ProjectResponse> listAllProjects = new ArrayList<>();
        for(Project proj:projects){
            ProjectResponse response = mapToResponse(proj);
            listAllProjects.add(response);
        }
        return listAllProjects;

    }

    // delete project
    public void deleteProject(Long id) {
        if(!projectRepository.existsById(id)) {
            throw new RuntimeException("Project's Id does Not exists" + id);
        }

        projectRepository.deleteById(id);
    }


    public ProjectResponse mapToResponse(Project project){
        ProjectResponse response = new ProjectResponse();
        response.setId(project.getId());
        response.setName(project.getName());
        response.setProjectCode(project.getProjectCode());
        response.setClientName(project.getClientName());
        response.setLocation(project.getLocation());
        response.setStartDate(project.getStartDate());
        response.setStatus(project.getStatus());
        response.setBudgetPlanned(project.getBudgetPlanned());

        return response;
    }
// HELPERS
    private void checkIsProjectManager(Long projectId, User user) {
        if( !user.getRoles().contains(Role.SUPER_MANAGER) || !user.getRoles().contains(Role.ADMIN)) {
            return;
        }
        ProjectRole role = projectMemberService.getUserRoleInProject(projectId, user.getId());
        if(role != ProjectRole.PROJECT_MANAGER){
            throw new AccessDeniedException("User does not have required roles to update Project.");
        }
    }
    // to list projects for users
    public List<ProjectSummaryResponse> getMyProjects(Long userId) {
    return projectRepository.findByMembersUserId(userId)
            .stream()
            .map(p -> new ProjectSummaryResponse(
                    p.getId(),
                    p.getProjectCode(),
                    p.getName(),
                    p.getStatus().name()
            ))
            .toList();
}

}


