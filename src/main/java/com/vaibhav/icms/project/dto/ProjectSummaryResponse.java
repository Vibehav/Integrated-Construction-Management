package com.vaibhav.icms.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectSummaryResponse {

    private Long id;
    private String name;
    private String prjectCode;
    private String status;
    
    
}
