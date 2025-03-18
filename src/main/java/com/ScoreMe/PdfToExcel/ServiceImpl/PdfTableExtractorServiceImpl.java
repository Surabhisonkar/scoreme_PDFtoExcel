package com.ScoreMe.PdfToExcel.ServiceImpl;

import com.ScoreMe.PdfToExcel.Service.PdfTableExtractorService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class PdfTableExtractorServiceImpl implements PdfTableExtractorService {
    //Extracts tables from PDF
    public void extractTablesFromPDF(String pdfPath, String excelPath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfPath))) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            List<List<String>> tables = detectTables(text);
            writeTablesToExcel(tables, excelPath);
        }
    }
   //Used for detecting tables
    private List<List<String>> detectTables(String text) {
        List<List<String>> tables = new ArrayList<>();
        String[] lines = text.split("\n");

        List<String> currentTable = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!currentTable.isEmpty()) {
                    tables.add(new ArrayList<>(currentTable));
                    currentTable.clear();
                }
            } else {
                currentTable.add(line);
            }
        }
        if (!currentTable.isEmpty()) {
            tables.add(currentTable);
        }

        return tables;
    }

    //Used for actually writing the table extracted into the Excel
    private void writeTablesToExcel(List<List<String>> tables, String excelPath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Extracted Tables");

            int rowNum = 0;
            for (List<String> table : tables) {
                for (String rowText : table) {
                    Row row = sheet.createRow(rowNum++);
                    String[] parts = rowText.split("(?<=\\s)(T|C|B/F)(?=\\s)|(:\\s)|\\s{10,}");

                    int cellNum = 0;
                    for (String part : parts) {
                        if (part.endsWith(":")) {
                            part = part.substring(0, part.length() - 1).trim();
                        }
                        Cell cell = row.createCell(cellNum++);
                        cell.setCellValue(part.trim());
                    }
                }
                rowNum++;
            }

            try (FileOutputStream fileOut = new FileOutputStream(excelPath)) {
                workbook.write(fileOut);
            }
        }
    }

}