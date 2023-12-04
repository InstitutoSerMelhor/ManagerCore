package com.institutosermelhor.ManagerCore.controller.Dtos;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.institutosermelhor.ManagerCore.controller.validade.custom.PDFTester;
import com.institutosermelhor.ManagerCore.util.ReportType;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ReportCreationDTO(
        @RequestParam 
        @NotNull(message = "Name must be provided")
        @NotEmpty(message = "Name cannot be empty")
        @Min(value=3, message = "Name must have at least 3 characteres")
        String name, 
        @RequestParam(name = "type")
        @NotNull
        ReportType fileName,
        @RequestBody
        @NotNull
        @PDFTester
        MultipartFile reportType
        ) {
    }