package com.vaibhav.icms.task.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignRequest {

  private Long assigneeId;
}
/*
{
  "assigneeId": 9
}


Validation Rules:-
Assignee must be project member
Task must belong to {projectId}

*/