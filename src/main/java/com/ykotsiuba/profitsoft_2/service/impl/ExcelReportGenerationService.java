package com.ykotsiuba.profitsoft_2.service.impl;

import com.ykotsiuba.profitsoft_2.dto.article.ArticleResponseDTO;
import com.ykotsiuba.profitsoft_2.entity.enums.ExcelHeader;
import com.ykotsiuba.profitsoft_2.service.ReportGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static com.ykotsiuba.profitsoft_2.entity.enums.ExcelColumnWidth.*;

@Service
@Slf4j
public class ExcelReportGenerationService implements ReportGenerationService {

    private static final String HEADER_FONT_NAME = "Arial";

    private static final short HEADER_FONT_SIZE = 16;

    private static final short ROW_HEIGHT = 300;

    private static final short HEADER_ROW_HEIGHT = 400;

    private static final String SHEET_NAME = "Articles";

    private final Workbook workbook;

    private final Sheet sheet;

    private CellStyle headerStyle;

    private CellStyle cellStyle;

    @Override
    public byte[] writeReport(List<ArticleResponseDTO> articles) {
        createHeader();
        for (int i=0; i< articles.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.setHeight(ROW_HEIGHT);
            addArticle(row, articles.get(i));
        }

        return write();
    }

    public ExcelReportGenerationService() {
        workbook = new XSSFWorkbook();

        sheet = workbook.createSheet(SHEET_NAME);
        sheet.setColumnWidth(0, TINY.getSize());
        sheet.setColumnWidth(1, BIG.getSize());
        sheet.setColumnWidth(2, MEDIUM.getSize());
        sheet.setColumnWidth(3, SMALL.getSize());

        initStyles();
    }

    private void initStyles() {
        headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        XSSFFont headerFont = ((XSSFWorkbook) workbook).createFont();
        headerFont.setFontName(HEADER_FONT_NAME);
        headerFont.setFontHeightInPoints(HEADER_FONT_SIZE);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setWrapText(true);
    }

    private void createHeader() {
        Row header = sheet.createRow(0);
        header.setHeight(HEADER_ROW_HEIGHT);

        for (int i=0; i< ExcelHeader.values().length; i++) {
            Cell headerCell = header.createCell(i);
            headerCell.setCellValue(ExcelHeader.values()[i].getName());
            headerCell.setCellStyle(headerStyle);
        }
    }

    private void addArticle(Row row, ArticleResponseDTO responseDTO) {
        Cell cell = row.createCell(0);
        cell.setCellValue(row.getRowNum());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue(responseDTO.getTitle());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(2);
        cell.setCellValue(responseDTO.getAuthorFullName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(3);
        cell.setCellValue(responseDTO.getYear());
        cell.setCellStyle(cellStyle);
    }


    private byte[] write() {
        try(ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            log.info("Writing excel file...");
            workbook.write(bos);
            workbook.close();
            return bos.toByteArray();
        } catch (IOException e) {
            log.info("Error writing excel file...");
            throw new RuntimeException(e);
        }
    }
}
