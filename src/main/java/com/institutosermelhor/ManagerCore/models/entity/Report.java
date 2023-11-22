package com.institutosermelhor.ManagerCore.models.entity;

import com.institutosermelhor.ManagerCore.util.ReportType;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@AllArgsConstructor
public class Report {

  @Id
  private String id;

  @Indexed(unique = true)
  private String name;

  private ReportType reportType;

  @Builder.Default
  private boolean isEnabled = true;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
