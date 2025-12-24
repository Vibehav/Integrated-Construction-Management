package com.vaibhav.icms.projectmember.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaibhav.icms.project.entity.Project;
import com.vaibhav.icms.project.repository.ProjectRepository;
import com.vaibhav.icms.projectmember.dto.AddProjectMemberRequest;
import com.vaibhav.icms.projectmember.dto.ProjectMemberResponse;
import com.vaibhav.icms.projectmember.dto.UserProjectResponse;
import com.vaibhav.icms.projectmember.entity.ProjectMember;
import com.vaibhav.icms.projectmember.repository.ProjectMemberRepository;
import com.vaibhav.icms.user.entity.User;
import com.vaibhav.icms.user.repository.UserRepository;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@Builder
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    // ADD MEMBER TO PROJECT
    //=======================
    public ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request){

        // 1. validate project
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found: " + projectId));

        // 2. validate user
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException(" User Not Found by Id: " + request.getUserId()));

        // 3. check duplicate memberships
        boolean exists = projectMemberRepository.existsByProjectIdAndUserId(projectId, request.getUserId());

        if(exists) {  // create UserAlreadyAssignedException later
            throw new RuntimeException("Usser already part of this project");
        }
        // 4. Create ProjectMember entity
        ProjectMember member = ProjectMember.builder()
                                            .project(project)
                                            .user(user)
                                            .projectRole(request.getRole())
                                            .build();


        // 5. save
        projectMemberRepository.save(member);

        return mapperToResponse(member);
        
    }

    
    // REMOVE MEMBER FROM THE RELATIONSHIP
    // ===================================
    // Deleting the relationship
    public void removeMember(Long projectId, Long userId){  

        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId).orElseThrow(() -> new RuntimeException("Project member not found- Project ID: " + projectId + " User ID: " + userId ));

        projectMemberRepository.delete(member);
    }
    
    
    //  LIST ALL MEMBERS OF A PROJECT 
    // ===============================
    
    public List<ProjectMemberResponse> getMembersByProject(Long projectId) {
        
        return projectMemberRepository.findByProjectId(projectId)
        .stream()
        .map(this::mapperToResponse)
        .toList();
    }
    //MAPPER FOR PROJECT MEMBERS
    public ProjectMemberResponse mapperToResponse(ProjectMember member){
        return ProjectMemberResponse.builder()
                                    .memberId(member.getId())
                                    .userId(member.getUser().getId())
                                    .userName(member.getUser().getName())
                                    .role(member.getProjectRole())
                                    .addedAt(member.getCreatedAt())
                                    .build();
        
    }
   
    //   USER DASHBOARD
    // ==================
    public List<UserProjectResponse> getProjectsForCurrentUser(Long userId) {

        return projectMemberRepository.findByUserId(userId)
                .stream()
                .map(this::toUserProjectResponse)
                .toList();
    }

    // MAPPER FOR USER RESPONSE
    public UserProjectResponse toUserProjectResponse(ProjectMember member){
        return UserProjectResponse.builder()
                                  .projectId(member.getProject().getId())
                                  .projectName(member.getProject().getName())
                                  .role(member.getProjectRole())
                                  .build();

    }

    // USED IN DIFFERENT CLASSES

    public ProjectMember getMemberByProjectIdAndUserId(Long projectId,Long userId){
        return projectMemberRepository.findByProjectIdAndUserId(projectId,userId).orElseThrow(()-> new RuntimeException("member not found in project"));
    }
    
}
