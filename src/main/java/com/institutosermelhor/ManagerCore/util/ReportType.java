package com.institutosermelhor.ManagerCore.util;

import lombok.Getter;

@Getter
public enum ReportType {
  ACTIVITY("ACTIVITY"),
  BALANCE("BALANCE"),
  BYLAWS("BYLAWS"),
  PROJECT("PROJECT");

  private final String name;

  ReportType(String name) {
    this.name = name;
  }

}

