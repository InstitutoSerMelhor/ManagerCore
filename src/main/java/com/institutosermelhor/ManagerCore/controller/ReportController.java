package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reports")
public class ReportController {

  private final ReportService service;

  @Autowired
  public ReportController(ReportService service) {
    this.service = service;
  }

  @PostMapping()
  public ResponseEntity<String> save(@RequestParam MultipartFile file) throws Exception {
    Report report = service.save(file);
    return ResponseEntity.ok().body(report.getId());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Resource> getFile(@PathVariable String id) {
    Report report = service.getFile(id);
    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(report.getType()))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + report.getName() + "\"")
        .body(new ByteArrayResource(report.getData()));
  }
}
