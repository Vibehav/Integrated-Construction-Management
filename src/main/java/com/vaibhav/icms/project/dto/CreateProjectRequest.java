package com.vaibhav.icms.project.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.vaibhav.icms.project.enums.ProjectStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectRequest {
    
    @NotBlank
    private String name;

    @NotBlank
    private String clientName;

    @NotBlank
    private String location;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private ProjectStatus status;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal budgetPlanned;

}
