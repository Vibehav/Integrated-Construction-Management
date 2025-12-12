package com.vaibhav.icms.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vaibhav.icms.project.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponse {

    private Long id;

    private String projectCode;

    private String name;

    private String clientName;

    private String location;

    private LocalDate startDate;

    private ProjectStatus status;

    private BigDecimal budgetPlanned;
}
