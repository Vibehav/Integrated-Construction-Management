package com.vaibhav.icms.task.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.vaibhav.icms.project.entity.Project;
import com.vaibhav.icms.task.enums.TaskStatus;
import com.vaibhav.icms.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Task always belongs to a project
    @ManyToOne(fetch = FetchType.LAZY,optional=false)
    @JoinColumn(name = "project_id", nullable=false)
    private Project project;

    @Column(name = "title", nullable=false)
    private String title;

    @Column(name = "description",columnDefinition = "TEXT", nullable=false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    // who is responsible (nullable) null because when a task created, it is not necessary to assign it to a user right now.
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    //which user created the task
    @ManyToOne(fetch=FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by",nullable=false, updatable=false)
    private User createdBy;

    private LocalDate dueDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable=false)
    private boolean deleted = false;

    
}
