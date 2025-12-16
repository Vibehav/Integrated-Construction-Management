package com.vaibhav.icms.projectmember.dto;

import com.vaibhav.icms.projectmember.enums.ProjectRole;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProjectMemberRequest {

    @NotNull
    private Long userId;

    @NotNull
    private ProjectRole role;

    
}
