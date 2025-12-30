package com.vaibhav.icms.projectmember.dto;

import java.time.LocalDateTime;

import com.vaibhav.icms.projectmember.enums.ProjectRole;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectMemberResponse {
    
    private Long memberId;
    private Long userId;
    private String userName;
    private ProjectRole role;
    private LocalDateTime addedAt;

    
}
