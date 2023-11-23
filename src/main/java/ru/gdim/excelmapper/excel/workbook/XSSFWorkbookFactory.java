package ru.gdim.excelmapper.excel.workbook;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class XSSFWorkbookFactory implements WorkbookFactory {

    @Override
    public Workbook build(String path) throws IOException, InvalidFormatException {

        try (OPCPackage opcPackage = OPCPackage.open(path)) {

            return new XSSFWorkbook(opcPackage);
        }
    }

    @Override
    public Workbook build(File file) throws IOException, InvalidFormatException {

        try (OPCPackage opcPackage = OPCPackage.open(file)) {

            return new XSSFWorkbook(opcPackage);
        }
    }

    @Override
    public Workbook build(InputStream inputStream) throws IOException {

        try (Workbook workBook = new XSSFWorkbook(inputStream)) {

            return workBook;
        }
    }

}
