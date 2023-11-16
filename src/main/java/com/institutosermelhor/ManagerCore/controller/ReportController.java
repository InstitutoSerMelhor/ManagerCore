package com.institutosermelhor.ManagerCore.controller;

import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDownloadDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDto;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.service.ReportService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/reports")
@Tag(name = "Reports")
public class ReportController {

  private final ReportService service;

  @Autowired
  public ReportController(ReportService service) {
    this.service = service;
  }

  @Secured("ADMIN")
  @PostMapping()
  public ResponseEntity<String> saveFile(@RequestParam MultipartFile file) throws Exception {
    String fileId = service.saveFile(file);
    return ResponseEntity.ok().body(fileId);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ByteArrayResource> getFileById(@PathVariable String id) throws IOException {
    ReportDownloadDto report = service.getFileById(id);
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(report.type()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.name() + "\"")
        .body(new ByteArrayResource(report.data()));
  }

  @GetMapping()
  public ResponseEntity<List<ReportDto>> getFiles() {
    List<Report> reports = service.getFiles();
    List<ReportDto> reportsDto = reports.stream()
        .map(report -> new ReportDto(report.getId(), report.getFileName(), report.getFileSize()))
        .toList();
    return ResponseEntity.ok(reportsDto);
  }

  @Secured("ADMIN")
  @PutMapping()
  public ResponseEntity<String> updateFile(@RequestParam MultipartFile file) throws Exception {
    String fileId = service.updateFile(file);
    return ResponseEntity.ok().body(fileId);
  }

  @Secured("ADMIN")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteFile(@PathVariable String id) {
    service.deleteFile(id);
    return ResponseEntity.noContent().build();
  }
}
