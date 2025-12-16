package com.vaibhav.icms.projectmember.dto;

import com.vaibhav.icms.projectmember.enums.ProjectRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserProjectResponse {
    
    private Long projectId;
    private String projectName;
    private ProjectRole role;
}
