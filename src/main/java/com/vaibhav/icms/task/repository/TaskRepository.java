package com.vaibhav.icms.task.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaibhav.icms.task.entity.Task;
import com.vaibhav.icms.task.enums.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // use this instead of findByid because of soft Delete
    Optional<Task> findByIdAndDeletedFalse(Long id);

    //To get all tasks inside a project
    List<Task> findByProjectIdAndDeletedFalse(Long projectId);


    /*
    Used for filters:
        TODO
        IN_PROGRESS
        BLOCKED
        DONE
    */ // Task of this project with this status
    List<Task> findByProjectIdAndStatusAndDeletedFalse(Long projectId,TaskStatus status);

    // Task assigned to a particular user in the project
    List<Task> findByProjectIdAndAssigneeIdAndDeletedFalse(Long projectId, Long assigneeId);

    // Remaining Task in a project
    List<Task> findByProjectIdAndAssigneeIsNullAndDeletedFalse(Long projectId);

}
