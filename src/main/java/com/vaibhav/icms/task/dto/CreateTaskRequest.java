package com.vaibhav.icms.task.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class CreateTaskRequest {

  @NotBlank
  private String title;

  @NotBlank
  private String description;

  @NotNull
  private LocalDate dueDate;

}
/*
{
  "title": "Pour foundation concrete",
  "description": "Use M30 grade",
  "dueDate": "2025-01-20"
}
*/