package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.util.ReportType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReportUpdateDto(
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    String name, 
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    ReportType reportType) {

}
