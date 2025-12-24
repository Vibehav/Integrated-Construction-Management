package com.vaibhav.icms.task.dto;

import com.vaibhav.icms.task.enums.TaskStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateTaskStatusRequest {
    private TaskStatus status;
}
/*

{
  "status": "IN_PROGRESS"
}

Business Rules
Invalid transitions rejected
assignee_id must NOT be null for IN_PROGRESS
DONE is terminal
 */
