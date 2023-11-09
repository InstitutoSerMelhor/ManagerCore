package com.institutosermelhor.ManagerCore.models.entity;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Project {

  @Id
  private String id;

  private String name;

  private String description;

  private Date createdAt;

  private Date updatedAt;

  private boolean isDeleted = false;
}
