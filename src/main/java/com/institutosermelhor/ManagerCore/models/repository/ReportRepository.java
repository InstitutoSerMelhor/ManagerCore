package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.Report;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportRepository extends MongoRepository<Report, String> {

  public Report findByFileName(String fileName);

  public List<Report> findByIsEnabledTrue();
}
