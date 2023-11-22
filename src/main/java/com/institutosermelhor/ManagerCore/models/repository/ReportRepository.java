package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.util.ReportType;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {

  public Report findByName(String name);

  public List<Report> findByIsEnabledTrue();

  public List<Report> findByReportType(ReportType reportType);
}
