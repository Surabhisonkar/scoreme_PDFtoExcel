package com.ScoreMe.PdfToExcel.Service;

import java.io.IOException;
import java.util.*;

public interface PdfTableExtractorService {

    void extractTablesFromPDF(String pdfPath, String excelPath) throws IOException;
}