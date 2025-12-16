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
    @PreAuthorize("hasAnyRole('ADMIN' ,'PROJECT_MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request){
        ProjectResponse response = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN' ,'PROJECT_MANAGER')")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable Long id,@RequestBody UpdateProjectRequest request){
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PROJECT_MANAGER','ACCOUNTANT')")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long id){
        ProjectResponse response = projectService.getProject(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyRole('ADMIN' ,'PROJECT_MANAGER','ACCOUNTANT')")
    public ResponseEntity<List<ProjectResponse>> listProjects(){
        List<ProjectResponse> response = projectService.listProjects();
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build(); //204
    }

    // User can view their projects
    @GetMapping("/my")
    public ResponseEntity<List<ProjectSummaryResponse>> getMyProjects(
        @AuthenticationPrincipal User user 
    ) {
        return ResponseEntity.ok(projectService.getMyProjects(user.getId()));
    }
    }
    

