package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.util.ReportType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportUpdateDto(
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    @Min(value=3, message = "Name must have at least 3 characteres")
    String name,
    @Size(min=6, max=8, message = "Report type must be between 6 and 8 characteres. Values accepted: ACTIVITY, BALANCE, BYLAWS and PROJECT.")
    ReportType reportType) {

}
