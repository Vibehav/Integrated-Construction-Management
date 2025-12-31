package com.vaibhav.icms.projectmember.service;

import java.util.List;

import com.vaibhav.icms.exception.common.ResourceNotFoundException;
import com.vaibhav.icms.exception.common.UserAlreadyAssignedException;
import com.vaibhav.icms.projectmember.enums.ProjectRole;
import com.vaibhav.icms.user.enums.Role;
import com.vaibhav.icms.user.service.UserService;
import org.springframework.security.access.AccessDeniedException;
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
    private final UserService userService;


    // ADD MEMBER TO PROJECT
    //=======================
    public ProjectMemberResponse addMember(Long projectId, AddProjectMemberRequest request,User currentUser){

        checkHasAuthority(projectId, currentUser);

        // 1. validate project
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found: " + projectId));

        // 2. validate user
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new ResourceNotFoundException(" User Not Found by Id: " + request.getUserId()));
        // 3. check duplicate memberships
        boolean exists = projectMemberRepository.existsByProjectIdAndUserId(projectId, request.getUserId());

        if(exists) {  // create UserAlreadyAssignedException later
            throw new UserAlreadyAssignedException("User already part of this project");
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
    public void removeMember(Long projectId, Long userId, User currentUser){

        checkHasAuthority(projectId, currentUser);
        ProjectMember member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId).orElseThrow(() -> new ResourceNotFoundException("Project member not found- Project ID: " + projectId + " User ID: " + userId));

        projectMemberRepository.delete(member);
    }


    //  LIST ALL MEMBERS OF A PROJECT
    // ===============================
    public List<ProjectMemberResponse> getMembersByProject(Long projectId,User currentUser) {
        checkHasAuthority(projectId,currentUser);
        return projectMemberRepository.findByProjectId(projectId)
        .stream()
        .map(this::mapperToResponse)
        .toList();
    }

    //MAPPER FOR PROJECT MEMBERS
    public ProjectMemberResponse mapperToResponse(ProjectMember member) {
        return ProjectMemberResponse.builder()
                .memberId(member.getId())
                .userId(member.getUser().getId())
                .userName(member.getUser().getUsername())
                .role(member.getProjectRole())
                .addedAt(member.getCreatedAt())
                .build();
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
        return projectMemberRepository.findByProjectIdAndUserId(projectId,userId).orElseThrow(()-> new ResourceNotFoundException("member not found in project"));
    }

    public ProjectRole getUserRoleInProject(Long projectId, Long userId) {
        return projectMemberRepository
                .findUserRoleInProject(projectId, userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User is not a member of this project")
                );
    }
    //HELPERS
    private void checkHasAuthority(Long projectId, User user) {
        if( !user.getRoles().contains(Role.SUPER_MANAGER) || !user.getRoles().contains(Role.ADMIN)) {
            return;
        }
        ProjectRole role = getUserRoleInProject(projectId, user.getId());
        if(role != ProjectRole.PROJECT_MANAGER){
            throw new AccessDeniedException("User does not have required roles to update Project.");
        }
    }
}
