package com.vaibhav.icms.task.controller;

import com.vaibhav.icms.task.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.vaibhav.icms.task.service.TaskService;
import com.vaibhav.icms.user.entity.User;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TaskResponse> createTask(
        @PathVariable Long projectId,
        @RequestBody CreateTaskRequest request,
        @AuthenticationPrincipal User user){
            TaskResponse response = taskService.createTask(projectId, request, user);
            return ResponseEntity.ok(response);
        }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/assign/{taskId}")
    public ResponseEntity<TaskResponse> assignTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User user,
            @RequestBody AssignRequest request){
        TaskResponse response = taskService.assignTask(projectId, taskId, user, request);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("/updateSatus/{taskId}")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody UpdateTaskStatusRequest request,
            @AuthenticationPrincipal User currentUser){
        TaskResponse response = taskService.updateStatus(projectId,taskId,request,currentUser);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("isAuthenticated()")
    @PutMapping("updateTask/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        TaskResponse response =
                taskService.updateTask(projectId, taskId, request, currentUser);
        return ResponseEntity.ok(response);
    }

    // ================= DELETE TASK =================
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @AuthenticationPrincipal User currentUser
    ) {
        taskService.deleteTask(projectId, taskId, currentUser);
        return ResponseEntity.noContent().build();
    }
    
}
/*
/projects/{projectId}/tasks

=============
1. Create Task
POST /projects/{projectId}/tasks

🔐 Authorization
User must be project member

================================
=================================
2. Assign/Reassign Task
PUT /projects/{projectId}/tasks/{taskId}/assign


🔐 Authorization
ADMIN
PROJECT_MANAGER

================================
================================
3. Update Task Status 
PUT /projects/{projectId}/tasks/{taskId}/status

🔐 Authorization
Assignee
ADMIN
PROJECT_MANAGER

===============================
===============================
4️⃣ Update Task (title / description / due date)
PUT /projects/{projectId}/tasks/{taskId}

🔐 Authorization

Creator ( who?)
ADMIN
PROJECT_MANAGER

===============================
===============================
5️⃣ Get All Tasks of a Project
GET /projects/{projectId}/tasks
🔐 Authorization
Any project member

?status=TODO
?assignee=me
?unassigned=true

Example - GET /projects/3/tasks?assignee=me&status=IN_PROGRESS

================================
================================
6️⃣ Get Single Task
GET /projects/{projectId}/tasks/{taskId}

🔐 Authorization
Any project member


================================
================================
7️⃣ Delete Task (Soft delete preferred)
DELETE /projects/{projectId}/tasks/{taskId}

🔐 Authorization
ADMIN
PROJECT_MANAGER

📌 Prefer:
isDeleted = true
Keeps audit history

*/