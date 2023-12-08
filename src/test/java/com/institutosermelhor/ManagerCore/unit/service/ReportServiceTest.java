package com.institutosermelhor.ManagerCore.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.mocks.ReportMock;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.models.repository.ReportRepository;
import com.institutosermelhor.ManagerCore.service.ReportService;
import com.institutosermelhor.ManagerCore.util.ReportType;
import com.mongodb.BasicDBObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBootTest
@SpringJUnitConfig
public class ReportServiceTest {

  @MockBean
  private ReportRepository reportRepository;

  @MockBean
  private GridFsTemplate gridFsTemplate;

  @Autowired
  private ReportService reportService;

  @Autowired
  private ReportMock reportMock;

  @Test
  @DisplayName("getReports method should return a list of reports")
  void testApiEndpoint() {
    Mockito.when(reportRepository.findByIsEnabledTrue())
        .thenReturn(List.of(reportMock.giveMeAReport()));

    List<Report> reports = reportService.getReports();

    assertEquals(reports.get(0).getId(), reportMock.giveMeAReport().getId());
    assertEquals(reports.get(0).getName(), reportMock.giveMeAReport().getName());
    assertEquals(reports.get(0).getReportType(), reportMock.giveMeAReport().getReportType());
  }

  @Test
  @DisplayName("getReportsByType method should return a list of reports of specific type")
  void testApiEndpoint2() {
    Mockito.when(reportRepository.findByReportType(ReportType.ACTIVITY))
        .thenReturn(List.of(reportMock.giveMeAReport()));

    List<Report> reports = reportService.getReportsByType(ReportType.ACTIVITY);

    assertEquals(reports.get(0).getId(), reportMock.giveMeAReport().getId());
    assertEquals(reports.get(0).getName(), reportMock.giveMeAReport().getName());
    assertEquals(reports.get(0).getReportType(), reportMock.giveMeAReport().getReportType());
  }

  @Test
  @DisplayName("save method should return created file id")
  void testApiEndpoint3() throws IOException {
    Mockito.when(reportRepository.findByName("Balance Report")).thenReturn(null);
    Mockito.when(gridFsTemplate.store(any(InputStream.class),
            anyString(),
            anyString(), any(BasicDBObject.class)))
        .thenReturn(
            new ObjectId("C9027E45D3FE956A075ADDC7"));
    Mockito.when(reportRepository.save(any(Report.class)))
        .thenReturn(reportMock.giveMeAReport());

    String fileId = reportService.saveFile(reportMock.giveMeACreationDto());

    assertEquals(fileId, "c9027e45d3fe956a075addc7");
  }

  @Test
  @DisplayName("save method should throw an error if report name already exists on database")
  void testApiEndpoint4() {
    Mockito.when(reportRepository.findByName("Balance Report"))
        .thenReturn(reportMock.giveMeAReport());

    assertThrows(ConflictException.class,
        () -> reportService.saveFile(reportMock.giveMeACreationDto()));
  }

  @Test
  @DisplayName("delete method should return nothing")
  void testApiEndpoint5() {
    Mockito.when(reportRepository.findById("validId123")).thenReturn(
        Optional.ofNullable(reportMock.giveMeAReport()));

    reportService.delete("validId123");

    Mockito.verify(reportRepository).findById("validId123");
  }

  @Test
  @DisplayName("delete method should throw an error if report is not found")
  void testApiEndpoint6() {
    Mockito.when(reportRepository.findById("invalidId123")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> reportService.delete("invalidId123"));
  }

  @Test
  @DisplayName("update method should return nothing")
  void testApiEndpoint7() {
    Mockito.when(reportRepository.findById("validId123")).thenReturn(
        Optional.ofNullable(reportMock.giveMeAReport()));

    reportService.update("validId123", reportMock.giveMeAUpdateDto());

    Mockito.verify(reportRepository).findById("validId123");
  }

  @Test
  @DisplayName("update method should throw an error if report is not found")
  void testApiEndpoint8() {
    Mockito.when(reportRepository.findById("invalidId123")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> reportService.update("invalidId123", reportMock.giveMeAUpdateDto()));
  }
}
