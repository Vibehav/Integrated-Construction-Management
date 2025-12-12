package com.vaibhav.icms.project.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.vaibhav.icms.project.enums.ProjectStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Projects {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
     private String projectCode;
     // Logic  given below
    @PrePersist
    protected void generateProjectCode() {
        if (this.projectCode == null) {
            
            
            String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
            
            
            String randomPart = UUID.randomUUID()
                                    .toString()
                                    .replace("-", "")
                                    .substring(0, 4)
                                    .toUpperCase();
                                
            this.projectCode = "ICM" + datePart + randomPart;
        }
        //ICM11122025ZX04
    }

    @Column(nullable=false)
    private String name;

    @Column(name="client_name", nullable=false)
    private String clientName;

    @Column(name="start_date", nullable=false)
    private LocalDate startDate;

    @Column(nullable=false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ProjectStatus status;
    
    @Column(name="budget_planned", precision= 15,scale=2)
    private BigDecimal budgetPlanned;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



}
/*
name
clientName
startDate
location
status
budgetPlanned
*/