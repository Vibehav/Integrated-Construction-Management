package com.vaibhav.icms.project.service;

import java.util.ArrayList;
import java.util.List;

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
    public ProjectResponse updateProject(Long id,UpdateProjectRequest request){
        Project project = projectRepository.findById(id)
                                            .orElseThrow(() -> new RuntimeException("Id Not Found" + id));

        
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

    // to list projeccts for users
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


