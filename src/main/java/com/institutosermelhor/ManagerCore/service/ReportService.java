package com.institutosermelhor.ManagerCore.service;

import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.models.repository.ReportRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Tag(name = "Reports")
public class ReportService {

  private final ReportRepository repository;

  @Autowired
  public ReportService(ReportRepository repository) {
    this.repository = repository;
  }

  public Report save(MultipartFile file) throws Exception {
    String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    long maxSize = 16000000; //16mb
    if (file.getSize() >= maxSize) {
      throw new Exception("File size too large");
    }

    if (!Objects.equals(file.getContentType(), "application/pdf")) {
      throw new Exception("File type not supported");
    }

    try {
      Report report = Report.builder().name(fileName).type(file.getContentType())
          .data(file.getBytes())
          .build();
      return repository.save(report);
    } catch (Exception e) {
      throw new Exception("Could not save file " + fileName);
    }
  }

  public Report getFile(String id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("File not found!"));
  }
}
