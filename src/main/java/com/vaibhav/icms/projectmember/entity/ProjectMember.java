package com.vaibhav.icms.projectmember.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.vaibhav.icms.project.entity.Project;
import com.vaibhav.icms.projectmember.enums.ProjectRole;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "project_members", 
    uniqueConstraints = {
        @UniqueConstraint (columnNames = {"project_id","user_id"})
    }
)
public class ProjectMember {

    @Id
    @GeneratedValue( strategy=GenerationType.IDENTITY)
    private Long id;
    
    // “When you load ProjectMember, DO NOT load user or project immediately.”
    // Many members belong to one project
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    // 
    //LAZY fetch → prevents performance issues
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="role_on_project")
    private ProjectRole projectRole;

    @CreationTimestamp
    @Column(name="created_at",updatable=false)
    private LocalDateTime createdAt;
}
