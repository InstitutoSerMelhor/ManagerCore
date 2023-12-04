package com.institutosermelhor.ManagerCore.controller.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProjectDto(
    String id,
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    String name, 
    @NotNull(message = "Description must be provided")
    @NotEmpty(message = "Description cannot be empty")
    @Size(min = 200, message = "Description must have at least 200 characteres.")
    String description, 
    @NotNull(message = "Area must be provided")
    @NotEmpty(message = "Area cannot be empty")
    String area  
) {

}
