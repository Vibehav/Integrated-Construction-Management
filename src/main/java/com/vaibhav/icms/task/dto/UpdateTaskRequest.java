package com.vaibhav.icms.task.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
}
/*
{
  "title": "Pour concrete – foundation",
  "description": "Concrete truck arrives at 8 AM",
  "dueDate": "2025-01-22"
}

*/
