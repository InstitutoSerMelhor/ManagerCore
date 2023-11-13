package com.institutosermelhor.ManagerCore.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@AllArgsConstructor
public class Report {

  @Id
  private String id;

  private String name;

  private String type;

  private byte[] data;

}
