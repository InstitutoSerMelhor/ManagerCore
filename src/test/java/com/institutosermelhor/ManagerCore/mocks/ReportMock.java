package com.institutosermelhor.ManagerCore.mocks;

import com.institutosermelhor.ManagerCore.controller.Dtos.ReportCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportDownloadDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportUpdateDto;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.util.ReportType;
import lombok.Data;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReportMock {

  public Report giveMeAReport() {
    return new Report(
        "validId123",
        "Activity Report",
        ReportType.ACTIVITY,
        true,
        null,
        null);
  }

  public ReportDownloadDto giveMeADownloadDto() {
    return new ReportDownloadDto("Activity Report", "application/pdf", "Report data".getBytes());
  }

  public ReportUpdateDto giveMeAUpdateDto() {
    return new ReportUpdateDto("New Report name", ReportType.PROJECT);
  }

  public ReportCreationDto giveMeACreationDto() {
    MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "Balance Report.pdf",
        "application/pdf", "balanceReportData".getBytes());
    return new ReportCreationDto("Balance Report", ReportType.BALANCE, pdfFile);
  }
}
