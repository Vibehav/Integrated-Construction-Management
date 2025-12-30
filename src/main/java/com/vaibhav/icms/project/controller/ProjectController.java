package com.vaibhav.icms.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vaibhav.icms.project.dto.CreateProjectRequest;
import com.vaibhav.icms.project.dto.ProjectResponse;
import com.vaibhav.icms.project.dto.ProjectSummaryResponse;
import com.vaibhav.icms.project.dto.UpdateProjectRequest;
import com.vaibhav.icms.project.service.ProjectService;
import com.vaibhav.icms.user.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN' ,'SUPER_MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request){
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN' ,'SUPER_MANAGER')")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long projectId,
                                                         @RequestBody UpdateProjectRequest request,
                                                         @AuthenticationPrincipal User user){
        ProjectResponse response = projectService.updateProject(projectId, request,user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_MANAGER','ACCOUNTANT')")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId){
        ProjectResponse response = projectService.getProject(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyRole('ADMIN' ,'SUPER_MANAGER','ACCOUNTANT')")
    public ResponseEntity<List<ProjectResponse>> listProjects(){
        List<ProjectResponse> response = projectService.listProjects();
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/delete/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build(); //204
    }

    // User can view their projects
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my")
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(
        @AuthenticationPrincipal User user 
    ) {
        return ResponseEntity.ok(projectService.getMyProjects(user.getId()));
    }
    }
    

