package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.controller.validade.custom.PDFTester;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.institutosermelhor.ManagerCore.util.ReportType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReportCreationDto(
    @RequestParam
    @NotNull(message = "Name must be provided")
    @NotEmpty(message = "Name cannot be empty")
    @Size(min = 3, message = "Name must have at least 3 characteres")
    String name,
    @RequestParam(name = "type")
    @NotNull
    ReportType reportType,
    @RequestParam
    @PDFTester
    MultipartFile pdfFile
) {

}