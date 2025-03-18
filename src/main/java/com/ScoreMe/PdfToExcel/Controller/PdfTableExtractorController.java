package com.ScoreMe.PdfToExcel.Controller;
import com.ScoreMe.PdfToExcel.Service.PdfTableExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class PdfTableExtractorController {

    @Autowired
    private PdfTableExtractorService pdfTableExtractorService;

    @GetMapping("/extract-tables")
    public String extractTables(@RequestParam String pdfPath, @RequestParam String excelPath) {
        try {
            pdfTableExtractorService.extractTablesFromPDF(pdfPath, excelPath);
            return "Tables extracted successfully to " + excelPath;
        } catch (IOException e) {
            return "Failed to extract tables: " + e.getMessage();
        }
    }
}