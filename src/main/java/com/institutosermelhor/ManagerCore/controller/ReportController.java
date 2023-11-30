package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDownloadDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportUpdateDto;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.service.ReportService;
import com.institutosermelhor.ManagerCore.util.ReportType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

  private final ReportService service;

  @Autowired
  public ReportController(ReportService service) {
    this.service = service;
  }

  @Secured("ADMIN")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Map<String, String>> saveFile(@RequestParam String name,
      @RequestParam("type") ReportType reportType,
      @RequestParam MultipartFile file)  throws Exception {
    String fileId = service.saveFile(name, reportType, file);
    return ResponseEntity.ok().body(Map.of("fileId", fileId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String id)
      throws IOException {
    ReportDownloadDto report = service.getFileById(id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(report.type()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + report.name() + "\"")
        .body(new ByteArrayResource(report.data()));
  }

  @GetMapping()
  public ResponseEntity<List<ReportDto>> getReports(@RequestParam("type") ReportType reportType) {
    List<Report> reports;
    if (reportType != null) {
      reports = service.getReportByType(reportType);
    } else {
      reports = service.getReports();
    }
    List<ReportDto> reportsDto = reports.stream()
        .map(report -> new ReportDto(report.getId(), report.getName(), report.getReportType()))
        .toList();
    return ResponseEntity.ok(reportsDto);
  }

  @Secured("ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid ReportUpdateDto report) {
    service.update(id, report);
    return ResponseEntity.noContent().build();
  }
}
