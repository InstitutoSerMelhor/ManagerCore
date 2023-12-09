package com.institutosermelhor.ManagerCore.unit.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ReportUpdateDto;
import com.institutosermelhor.ManagerCore.mocks.ReportMock;
import com.institutosermelhor.ManagerCore.service.ReportService;
import com.institutosermelhor.ManagerCore.util.ReportType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
public class ReportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ReportService reportService;

  @Autowired
  private ReportMock reportMock;

  @Test
  @DisplayName("getReports method when report collection is empty return an empty list")
  void testApiEndpoint() throws Exception {
    Mockito.when(reportService.getReports()).thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));

    Mockito.verify(reportService).getReports();
  }

  @Test
  @DisplayName("getReports method when report collection has reports returns reports list")
  void testApiEndpoint2() throws Exception {
    Mockito.when(reportService.getReports()).thenReturn(List.of(reportMock.giveMeAReport()));

    mockMvc.perform(MockMvcRequestBuilders.get("/reports"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(reportMock.giveMeAReport().getName()))
        .andExpect(
            jsonPath("$[0].reportType").value(
                reportMock.giveMeAReport().getReportType().getName()));

    Mockito.verify(reportService).getReports();
  }

  @Test
  @DisplayName("getReports method when type is declared on a query string returns reports from the specific type")
  void testApiEndpoint3() throws Exception {
    Mockito.when(reportService.getReportsByType(any(ReportType.class)))
        .thenReturn(List.of(reportMock.giveMeAReport()));

    mockMvc.perform(MockMvcRequestBuilders.get("/reports?type=ACTIVITY"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(reportMock.giveMeAReport().getName()))
        .andExpect(
            jsonPath("$[0].reportType").value(
                reportMock.giveMeAReport().getReportType().getName()));

    Mockito.verify(reportService).getReportsByType(any(ReportType.class));
  }

  @Test
  @DisplayName("getReports method when wrong type is declared on a query string returns bad request error")
  void testApiEndpoint4() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/reports?type=WRONGTYPE"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("getReport method returns the specific report")
  void testApiEndpoint5() throws Exception {
    Mockito.when(reportService.getFileById(reportMock.giveMeAReport().getId()))
        .thenReturn(reportMock.giveMeADownloadDto());

    mockMvc.perform(MockMvcRequestBuilders.get("/reports/" + reportMock.giveMeAReport().getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(reportMock.giveMeADownloadDto().contentType()))
        .andExpect(content().bytes(reportMock.giveMeADownloadDto().data()));
  }

  @Test
  @DisplayName("saveFile method returns the file id")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint6() throws Exception {
    Mockito.when(reportService.saveFile(any(ReportCreationDto.class)))
        .thenReturn("fileId123");

    MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "Balance Report.pdf",
        "application/pdf", "balanceReportData".getBytes());

    mockMvc.perform(MockMvcRequestBuilders.multipart(
                "/reports").file(pdfFile).param("name", "Balance Report")
            .param("reportType", "BALANCE"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.fileId").value("fileId123"));
  }

  @Test
  @DisplayName("saveFile method should throw bad request if file content type is not PDF")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint7() throws Exception {
    MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "Balance Report.txt",
        "text/plain", "balanceReportData".getBytes());

    mockMvc.perform(MockMvcRequestBuilders.multipart(
                "/reports").file(pdfFile).param("name", "Balance Report")
            .param("reportType", "BALANCE"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].field").value("pdfFile"))
        .andExpect(jsonPath("$[0].mensage")
            .value("File type not supported. Accept only PDF."));
  }

  @Test
  @DisplayName("saveFile method should throw bad request if name has less than 3 characters")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint8() throws Exception {
    Mockito.when(reportService.saveFile(any(ReportCreationDto.class)))
        .thenReturn("fileId123");

    MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "Balance Report.pdf",
        "application/pdf", "balanceReportData".getBytes());

    mockMvc.perform(MockMvcRequestBuilders.multipart(
                "/reports").file(pdfFile).param("name", "Ba")
            .param("reportType", "BALANCE"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].field").value("name"))
        .andExpect(jsonPath("$[0].mensage")
            .value("Name must have at least 3 characteres"));
  }

  @Test
  @DisplayName("saveFile method should throw bad request if reportType is null")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint9() throws Exception {
    Mockito.when(reportService.saveFile(any(ReportCreationDto.class)))
        .thenReturn("fileId123");

    MockMultipartFile pdfFile = new MockMultipartFile("pdfFile", "Balance Report.pdf",
        "application/pdf", "balanceReportData".getBytes());

    mockMvc.perform(MockMvcRequestBuilders.multipart(
            "/reports").file(pdfFile).param("name", "Balance Report"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].field").value("reportType"))
        .andExpect(jsonPath("$[0].mensage").value("must not be null"));
  }

  @Test
  @DisplayName("delete method when delete a report returns no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint10() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.delete("/reports/" + reportMock.giveMeAReport().getId()))
        .andExpect(status().isNoContent());

    Mockito.verify(reportService).delete(reportMock.giveMeAReport().getId());
  }

  @Test
  @DisplayName("update method when update a report returns no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint11() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.put("/reports/" + reportMock.giveMeAReport().getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(reportMock.giveMeAUpdateDto())))
        .andExpect(status().isNoContent());

    Mockito.verify(reportService)
        .update(reportMock.giveMeAReport().getId(), reportMock.giveMeAUpdateDto());
  }

  @Test
  @DisplayName("update method should return bad request if name has less than 3 characters")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint12() throws Exception {
    ReportUpdateDto invalidUpdate = new ReportUpdateDto("No", ReportType.PROJECT);
    mockMvc.perform(MockMvcRequestBuilders.put("/reports/" + reportMock.giveMeAReport().getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(invalidUpdate)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].field").value("name"))
        .andExpect(jsonPath("$[0].mensage")
            .value("Name must have at least 3 characteres"));
  }
}
