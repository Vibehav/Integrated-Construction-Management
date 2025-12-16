package com.vaibhav.icms.projectmember.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public ResponseEntity<ProjectMemberResponse> addMember(
        @PathVariable Long projectId, @Valid @RequestBody AddProjectMemberRequest request
    ) {
        ProjectMemberResponse response = projectMemberService.addMember(projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get members in the project
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponse>> getMembersByProject(@PathVariable Long projectId){
        List<ProjectMemberResponse> members = projectMemberService.getMembersByProject(projectId);

        return ResponseEntity.ok(members);
        
    }

    // delete members in the project
    @DeleteMapping("{projectId}/members/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER')")
    public ResponseEntity<Void> removeMember(
        @PathVariable Long projectId,
        @PathVariable Long userId
    ) {
        projectMemberService.removeMember(projectId, userId);
        return ResponseEntity.noContent().build(); // 204
    }



}
