package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.util.ReportType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportUpdateDto(
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, message = "Name must have at least 3 characteres")
    String name,
    ReportType reportType) {

}
