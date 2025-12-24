package com.vaibhav.icms.task.service;



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
import com.vaibhav.icms.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectMemberService projectMemberService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    //      Create a Task 
    // =======================
   
    public TaskResponse createTask(Long projectId, CreateTaskRequest request, User currentUser){

        Project project = projectRepository.findById(projectId).orElseThrow(()-> new RuntimeException("Project not found while creating task"));

        if(!projectMemberRepository.existsByProjectIdAndUserId(projectId, currentUser.getId())){
            throw new RuntimeException("Task Creator is not a member of this project");
        }

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
        System.out.println(" ============================================== ");
        return taskMapperToResponse(savedTask);

    }

    //     Assign a Task
    // ===================
    /*
    Assigned By preAuth -> Admin or Project_Manager

    1. Check if the task is present 
    2. check if the assignee id is present
    3. Get the assignee Id and check if it is a project member or not
    4. assignee
    */
   @Transactional  
    public TaskResponse assignTask(Long projectId, Long taskId, AssignRequest request){ 
        
        Task task = taskRepository.findByIdAndDeletedFalse(taskId).orElseThrow(() -> new RuntimeException("Task Not found."));

        User assignee = userRepository.findById(request.getAssigneeId()).orElseThrow(()->new RuntimeException("Assignee not fount"));

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
        ensureValidTransation(task, request.getStatus());
        
        
        task.setStatus(request.getStatus());     
        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);
    }


    //   Update a Task
    // =================
    public TaskResponse updateTask(Long projectId, Long taskId, UpdateTaskRequest request,User currentUser){
       
        Task task = getTaskOrThrow(taskId);

        ensureTaskInProject(task, projectId);
        ensureCreatorOrManager(projectId,currentUser,task);

        if(request.getTitle() !=null && !request.getTitle().isEmpty())task.setTitle(request.getTitle());
        if(request.getDescription()!=null && !request.getDescription().isEmpty())task.setDescription(request.getDescription());
        if(request.getDueDate()!=null)task.setDueDate(request.getDueDate());

        Task savedTask = taskRepository.save(task);
        return taskMapperToResponse(savedTask);

    }

    public void deleteTask(Long projectId,Long taskId,User currentUser){
        Task task = getTaskOrThrow(taskId);
        ensureCreatorOrManager(projectId,currentUser, task);
        ensureTaskInProject(task,projectId);
        
        task.setDeleted(true);
        taskRepository.save(task);

    }
    // Entity to DTO class
    public TaskResponse taskMapperToResponse(Task task){
        return TaskResponse.builder()
                           .id(task.getId())
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

    // assign,projectManager,Afmin only these people can update
    private void ensureCanUpdateStatus(Long projectId, User currentUser, Task task){
        
        //check if assignee can update
        if(task.getAssignee()!=null && task.getAssignee().getId().equals(currentUser.getId())){
            return;
        }

        //check for admin
        if(currentUser.getRoles().contains(Role.ADMIN)){
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
    public void ensureValidTransation(Task task, TaskStatus nextStatus){
        if(task.getStatus() == TaskStatus.COMPLETED){
            throw new RuntimeException("Completed is Terminal");
        }

        if((nextStatus == TaskStatus.IN_PROGRESS || nextStatus == TaskStatus.COMPLETED) && task.getAssignee() != null) {
            throw new RuntimeException("First Assignee the User");
        }



    }

    // Check while updating the task if the user is (Admin,project manager or has createdBy id same)
    public void ensureCreatorOrManager(Long projectId, User currentUser, Task task){
        if(task.getCreatedBy().getId().equals(currentUser.getId())){
            return;
        }

        if(currentUser.getRoles().contains(Role.ADMIN) || currentUser.getRoles().contains(Role.PROJECT_MANAGER) ){
            return;
        }

        ProjectMember member = projectMemberService.getMemberByProjectIdAndUserId(projectId, currentUser.getId());

        if(member.getProjectRole() == ProjectRole.PROJECT_MANAGER) {return;}



          throw new AccessDeniedException("Forbidden");
    }




}
