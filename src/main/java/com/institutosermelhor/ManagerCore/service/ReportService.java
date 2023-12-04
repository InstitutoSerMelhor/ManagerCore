package com.institutosermelhor.ManagerCore.service;

import com.institutosermelhor.ManagerCore.controller.Dtos.ReportCreationDTO;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDownloadDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportUpdateDto;
import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.models.repository.ReportRepository;
import com.institutosermelhor.ManagerCore.util.ReportType;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
  private final GridFsTemplate gridFsTemplate;
  private final GridFsOperations gridFsOperations;
  private final ReportRepository repository;

  @Autowired
  public ReportService(GridFsTemplate gridFsTemplate,
      GridFsOperations gridFsOperations, ReportRepository repository) {
    this.gridFsTemplate = gridFsTemplate;
    this.gridFsOperations = gridFsOperations;
    this.repository = repository;
  }

  public String saveFile(ReportCreationDTO newReport)
      throws IOException {
    if (repository.findByName(newReport.name()) != null) {
      throw new ConflictException("File name already exists");
    }
    DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", newReport.reportType().getSize());

        Object fileID = gridFsTemplate.store(newReport.reportType().getInputStream(),
        newReport.reportType().getOriginalFilename(), 
        newReport.reportType().getContentType(), metadata);
        Report report = Report.builder().id(fileID.toString()).name(newReport.name()).reportType(newReport.fileName())
            .build();

        repository.save(report);

      return report.getId().toString();
    }


  public ReportDownloadDto getFileById(String id) throws IOException {
    Report report = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("File not found"));

    GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));

    if (!report.isEnabled() || gridFSFile == null || gridFSFile.getMetadata() == null) {
      throw new NotFoundException("File not found!");
    }

    return new ReportDownloadDto(gridFSFile.getFilename(),
        gridFSFile.getMetadata().get("_contentType").toString(),
        gridFSFile.getMetadata().get("fileSize").toString(),
        IOUtils.toByteArray(gridFsOperations.getResource(gridFSFile).getInputStream()));
  }

  public List<Report> getReports() {
    return repository.findByIsEnabledTrue();
  }

  public List<Report> getReportByType(ReportType reportType) {
    return repository.findByReportType(reportType);
  }

  public void delete(String id) {
    Report report = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("File not found"));
    report.setEnabled(false);
    repository.save(report);
  }

  public void update(String id, ReportUpdateDto report) {
    Report reportToUpdate = repository.findById(id)
        .orElseThrow(() -> new NotFoundException("File not found"));
    reportToUpdate.setName(report.name());
    reportToUpdate.setReportType(report.reportType());
    repository.save(reportToUpdate);
  }
}
