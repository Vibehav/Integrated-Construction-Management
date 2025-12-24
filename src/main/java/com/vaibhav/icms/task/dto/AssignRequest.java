package com.vaibhav.icms.task.dto;

import com.vaibhav.icms.task.enums.TaskStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignRequest {

  private Long assigneeId;

  private TaskStatus status;
    
}
/*
{
  "assigneeId": 9
}


Validation Rules:-
Assignee must be project member
Task must belong to {projectId}

*/