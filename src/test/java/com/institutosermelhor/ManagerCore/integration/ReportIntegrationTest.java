package com.institutosermelhor.ManagerCore.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.mocks.ReportMock;
import com.institutosermelhor.ManagerCore.models.entity.Report;
import com.institutosermelhor.ManagerCore.models.repository.ReportRepository;
import com.institutosermelhor.ManagerCore.service.ReportService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
public class ReportIntegrationTest extends MongoDbTestcontainerConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ReportRepository reportRepository;

  @Autowired
  private ReportService reportService;

  @Autowired
  private ReportMock reportMock;

  @AfterEach
  void cleanAll() {
    this.reportRepository.deleteAll();
  }

  @Test
  @DisplayName("getReports method when report collection is empty return an empty list")
  void testApiEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @DisplayName("getReports method when report collection has reports return a reports list")
  void testApiEndpoint2() throws Exception {
    reportRepository.save(reportMock.giveMeAReport());

    mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(reportMock.giveMeAReport().getName()))
        .andExpect(
            jsonPath("$[0].reportType").value(
                reportMock.giveMeAReport().getReportType().getName()));
  }

  @Test
  @DisplayName("getReports method when type is declared on a query string returns reports from the specific type")
  void testApiEndpoint3() throws Exception {
    reportRepository.save(reportMock.giveMeAReport());

    mockMvc.perform(MockMvcRequestBuilders.get("/reports?type=ACTIVITY"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(reportMock.giveMeAReport().getName()))
        .andExpect(
            jsonPath("$[0].reportType").value(
                reportMock.giveMeAReport().getReportType().getName()));
  }

  @Test
  @DisplayName("downloadFile should return the specific file to download")
  void testApiEndpoint4() throws Exception {
    String fileId = reportService.saveFile(reportMock.giveMeACreationDto());

    mockMvc.perform(MockMvcRequestBuilders.get("/reports/" + fileId))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/pdf"))
        .andExpect(content().bytes("balanceReportData".getBytes()));
  }

  @Test
  @DisplayName("delete method when delete an project return no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint5() throws Exception {
    Report report = reportRepository.save(reportMock.giveMeAReport());

    mockMvc.perform(MockMvcRequestBuilders.delete("/reports/" + report.getId()))
        .andExpect(status().isNoContent());

    Report reportSearched = reportRepository.findAll().get(0);
    assertFalse(reportSearched.isEnabled());
  }

  @Test
  @DisplayName("update method when update project data return project data updated")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint6() throws Exception {
    Report reportInserted = reportRepository.save(reportMock.giveMeAReport());

    mockMvc.perform(MockMvcRequestBuilders.put("/reports/" + reportInserted.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reportMock.giveMeAUpdateDto())))
        .andExpect(status().isNoContent());

    Report reportUpdated = reportRepository.findAll().get(0);
    assertEquals(reportUpdated.getId(), reportInserted.getId());
    assertEquals(reportUpdated.getName(), reportMock.giveMeAUpdateDto().name());
    assertEquals(reportUpdated.getReportType().getName(),
        reportMock.giveMeAUpdateDto().reportType().getName());
  }
}
