package com.vaibhav.icms.task.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.vaibhav.icms.task.enums.TaskStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaskResponse {

  private Long id;

  private String title;

  private Long projectId;

  private Long assignee;

  private Long createdBy;

  private String description;

  private TaskStatus status;

  private LocalDate dueDate;

  private LocalDateTime createdAt;
    
}
/*
{
  "id": 12,
  "projectId": 3,
  "title": "Pour foundation concrete",
  "description": "Use M30 grade",
  "status": "TODO",
  "assignee": null, 
  "createdBy": {
    "id": 5,
    "name": "Rahul"
  },
  "dueDate": "2025-01-20",
  "createdAt": "2025-01-10T11:30:00"
}

*/