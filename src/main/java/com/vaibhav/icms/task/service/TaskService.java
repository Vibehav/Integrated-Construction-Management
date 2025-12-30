package com.vaibhav.icms.task.service;



import com.vaibhav.icms.user.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.vaibhav.icms.project.entity.Project;
import com.vaibhav.icms.project.repository.ProjectRepository;
import com.vaibhav.icms.projectmember.entity.ProjectMember;
import com.vaibhav.icms.projectmember.enums.ProjectRole;
import com.vaibhav.icms.projectmember.repository.ProjectMemberRepository;
import com.vaibhav.icms.projectmember.service.ProjectMemberService;
import com.vaibhav.icms.task.dto.AssignRequest;
import com.vaibhav.icms.task.dto.CreateTaskRequest;
import com.vaibhav.icms.task.dto.TaskResponse;
import com.vaibhav.icms.task.dto.UpdateTaskRequest;
import com.vaibhav.icms.task.dto.UpdateTaskStatusRequest;
import com.vaibhav.icms.task.entity.Task;
import com.vaibhav.icms.task.enums.TaskStatus;
import com.vaibhav.icms.task.repository.TaskRepository;
import com.vaibhav.icms.user.entity.User;
import com.vaibhav.icms.user.enums.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberService projectMemberService;
    private final ProjectRepository projectRepository;
    private final UserService userService;

    //      Create a Task
    // =======================
   /*
   1. Check if Project Exists before creating a task
   2. Check if Project Member is a Project Manager or Site Engineer.
   3. assign
   4. save
    */
    public TaskResponse createTask(Long projectId, CreateTaskRequest request, User currentUser){

        Project project = projectRepository.findById(projectId).orElseThrow(()-> new RuntimeException("Project not found while creating task"));

        if(!projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUser.getId())){
            throw new RuntimeException("Task Creator is not a member of this project");
        }

        // Define which roles are allowed to create tasks
        ensureCreatorRoleIsAuthenticated(projectId, currentUser);


        Task task = Task.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .dueDate(request.getDueDate())
                        .status(TaskStatus.NOT_STARTED)
                        .createdBy(currentUser)
                        .project(project)
                        .deleted(false)
                        .build();

        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);

    }

    private void ensureCreatorRoleIsAuthenticated(Long projectId, User currentUser) {
        boolean flag = false;
        Set<ProjectRole> canCreateTask = EnumSet.of(ProjectRole.PROJECT_MANAGER, ProjectRole.SITE_ENGINEER);

        if (!canCreateTask.contains(projectMemberService.getUserRoleInProject(projectId, currentUser.getId()))) {
            flag = true;
        }
        if(!currentUser.getRoles().contains(Role.SUPER_MANAGER) && !currentUser.getRoles().contains(Role.ADMIN)){
            flag = true;
        }

        if(flag) {
            return;
        } else {
            throw new AccessDeniedException("Insufficient permissions to create a task.");
        }
    }

    //     Assign a Task
    // ===================
    /*
    Assigned By preAuth -> Admin or Project_Manager

    1. Check if the task is present 
    2. check if the assignee id is present
    3. Get the assignee id and check if it is a project member or not
    4. assignee and save
    */
   @Transactional  
    public TaskResponse assignTask(Long projectId, Long taskId,User currentUser, AssignRequest request){
        
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(() -> new RuntimeException("Task Not found."));

        //Roles which can assign tasks
       ensureCreatorRoleIsAuthenticated(projectId,currentUser);

       User assignee = userService.getUser(request.getAssigneeId());
        if(request.getAssigneeId() != null){
            boolean isMember = projectMemberRepository.existsByProjectIdAndUserId(projectId, request.getAssigneeId());
            if(!isMember) throw new IllegalArgumentException("The assigned user is not a member of this project.");
        }

        task.setAssignee(assignee);
        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);

    }


    //   Update a status (Assignee Updates Task: complete, in_progress) 
    // ==================================================================
    
    public TaskResponse updateStatus(Long projectId, Long taskId, UpdateTaskStatusRequest request, User currentUser){
        
        Task task = getTaskOrThrow(taskId);

        ensureTaskInProject(task, projectId);
        ensureCanUpdateStatus(projectId,currentUser,task);
        ensureValidTransaction(task, request.getStatus());
        
        
        task.setStatus(request.getStatus());     
        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);
    }


    //   Update a Task
    // =================
    public TaskResponse updateTask(Long projectId, Long taskId, UpdateTaskRequest request,User currentUser){
       
        Task task = getTaskOrThrow(taskId);

        ensureTaskInProject(task, projectId);

        ensureCreatorRoleIsAuthenticated(projectId,currentUser);

        if(request.getTitle() !=null && !request.getTitle().isEmpty())task.setTitle(request.getTitle());
        if(request.getDescription()!=null && !request.getDescription().isEmpty())task.setDescription(request.getDescription());
        if(request.getDueDate()!=null)task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);

    }

    public void deleteTask(Long projectId,Long taskId,User currentUser){
        Task task = getTaskOrThrow(taskId);

        //Roles which can assign tasks
        Set<ProjectRole> assignTask = EnumSet.of(ProjectRole.PROJECT_MANAGER);

        if (!assignTask.contains(projectMemberService.getUserRoleInProject(projectId, currentUser.getId())) || !currentUser.getRoles().contains(Role.ADMIN) || !currentUser.getRoles().contains(Role.SUPER_MANAGER)) {
            throw new AccessDeniedException("Insufficient permissions to assign a task.");
        }
        ensureTaskInProject(task,projectId);

        task.setDeleted(true);
        taskRepository.save(task);

    }
    // Entity to DTO class
    public TaskResponse taskMapperToResponse(Task task){
        return TaskResponse.builder()
                           .id(task.getId())
                           .title(task.getTitle())
                           .projectId(task.getProject().getId())
                           .assignee(task.getAssignee() != null ? task.getAssignee().getId() : null)
                           .createdBy(task.getCreatedBy().getId())
                           .description(task.getDescription())
                           .status(task.getStatus())
                           .dueDate(task.getDueDate())
                           .createdAt(task.getCreatedAt())
                           .build();
    }

    // ==================== HELPERS ===============================

    private Task getTaskOrThrow(Long taskId){
        return taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(() -> new IllegalArgumentException("Task Node Found"));
    }

    private void ensureTaskInProject(Task task, Long projectId){
        if(!task.getProject().getId().equals(projectId)) {
            throw new RuntimeException("Task does not belongs to the project");
        }
    }

    // assign,projectManager,Admin only these people can update
    private void ensureCanUpdateStatus(Long projectId, User currentUser, Task task){

        //check if assignee can update
        if(task.getAssignee()!=null && task.getAssignee().getId().equals(currentUser.getId())){
            return;
        }

        //check for admin
        if(currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.SUPER_MANAGER)){
            return;
        }

        // check for Project Manager (check inside the project because he should be inside the project)
        ProjectMember member = projectMemberService.getMemberByProjectIdAndUserId(projectId,currentUser.getId());

        if(member.getProjectRole() == ProjectRole.PROJECT_MANAGER){
            return;
        }

        throw new AccessDeniedException("Not allowed to update the task status");
    }

    // CHECK IF THE TRANSACTION IS VALID
    public void ensureValidTransaction(Task task, TaskStatus nextStatus) {
        if (task.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Task has been Completed");
        }

        if ((nextStatus == TaskStatus.IN_PROGRESS || nextStatus == TaskStatus.COMPLETED) && task.getAssignee() == null) {
            throw new RuntimeException("First Assignee the User");
        }

    }
}
