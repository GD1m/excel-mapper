package ru.gdim.excelmapper.excel.workbook;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface WorkbookFactory {

    Workbook build(InputStream inputStream) throws IOException;

    Workbook build(String path) throws IOException, InvalidFormatException;

    Workbook build(File file) throws IOException, InvalidFormatException;

}
