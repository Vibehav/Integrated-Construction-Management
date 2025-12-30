package com.vaibhav.icms.projectmember.controller;

import java.util.List;

import com.vaibhav.icms.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibhav.icms.projectmember.dto.AddProjectMemberRequest;
import com.vaibhav.icms.projectmember.dto.ProjectMemberResponse;
import com.vaibhav.icms.projectmember.service.ProjectMemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectMemberController {
    
    private final ProjectMemberService projectMemberService;

    // Add Members to the project
    @PostMapping("/{projectId}/members")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectMemberResponse> addMember(
        @PathVariable Long projectId,
        @Valid @RequestBody AddProjectMemberRequest request,
        @AuthenticationPrincipal User currentUser
    ) {
        ProjectMemberResponse response = projectMemberService.addMember(projectId, request,currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get members in the project
    @GetMapping("/{projectId}/members")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectMemberResponse>> getMembersByProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal User currentUser){
        List<ProjectMemberResponse> members = projectMemberService.getMembersByProject(projectId,currentUser);

        return ResponseEntity.ok(members);
        
    }

    // delete members in the project
    @DeleteMapping("{projectId}/members/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeMember(
        @PathVariable Long projectId,
        @PathVariable Long userId,
        @AuthenticationPrincipal User currentUser
    ) {
        projectMemberService.removeMember(projectId, userId,currentUser);
        return ResponseEntity.noContent().build(); // 204
    }



}
