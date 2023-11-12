package com.institutosermelhor.ManagerCore.models.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
@AllArgsConstructor
public class Project {

  @Id
  private String id;

  private String name;

  private String description;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;

  @Builder.Default
  private boolean isEnabled = true;
}
