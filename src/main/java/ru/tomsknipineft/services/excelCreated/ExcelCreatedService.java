package ru.tomsknipineft.services.excelCreated;


import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.Calendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Data
@Service
public class ExcelCreatedService {

    private List<Calendar> calendars;

    private String filename;

    private String pathFile = "DownloadsCalendar/";

    /**
     * Метод создания файла Excel
     */
    public void write() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = createSheet(workbook);
        createHeader(workbook, sheet);
        createCells(workbook, sheet);
        filename = "Calendar_" + calendars.get(0).getCodeContract() + ".xlsx";

        try (FileOutputStream outputStream = new FileOutputStream(pathFile + filename)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод перевода файла Excel с календарем в поток байтов типа ByteArrayResource
     * @return поток байтов типа ByteArrayResource
     */
    public ByteArrayResource resource() {
        ByteArrayResource resource;
        File file = new File(pathFile + filename);
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            resource = new ByteArrayResource(fileInputStream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resource;
    }

    /**
     * Создание страницы с параметрами колонок в файле Excel
     * @param workbook рабочий лист Excel
     * @return настроенная страница в файле Excel
     */
    private Sheet createSheet(XSSFWorkbook workbook) {
        Sheet sheet = workbook.createSheet("Календарный план");
        sheet.setColumnWidth(1, 2800);
        sheet.setColumnWidth(2, 10000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        return sheet;
    }

    /**
     * Создание шапки таблицы с календарными сроками проекта
     * @param workbook рабочий лист Excel
     * @param sheet страница в файле Excel
     */
    private void createHeader(XSSFWorkbook workbook, Sheet sheet) {
        Row header = sheet.createRow(1);

        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.MEDIUM);
        headerStyle.setBorderBottom(BorderStyle.MEDIUM);
        headerStyle.setBorderLeft(BorderStyle.MEDIUM);
        headerStyle.setBorderRight(BorderStyle.MEDIUM);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(1);
        headerCell.setCellValue("№ этапа");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Наименование этапа работ");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Дата начала");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Дата окончания");
        headerCell.setCellStyle(headerStyle);
    }

    /**
     * Создание ячеек таблицы с расчетными параметрами
     * @param workbook рабочий лист Excel
     * @param sheet страница в файле Excel
     */
    private void createCells(XSSFWorkbook workbook, Sheet sheet) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFCellStyle styleBorder = workbook.createCellStyle();
        XSSFCellStyle stageStyle = workbook.createCellStyle();
        style.setWrapText(true);
        stageStyle.setAlignment(HorizontalAlignment.CENTER);
        stageStyle.setBorderTop(BorderStyle.MEDIUM);
        stageStyle.setBorderBottom(BorderStyle.THIN);
        stageStyle.setBorderLeft(BorderStyle.THIN);
        stageStyle.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        styleBorder.setBorderTop(BorderStyle.THIN);
        styleBorder.setBorderBottom(BorderStyle.THIN);
        styleBorder.setBorderLeft(BorderStyle.THIN);
        styleBorder.setBorderRight(BorderStyle.THIN);
        XSSFCreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
        int nextIter = 0;
        int chapterCalendar = 1;
        for (Calendar calendar : calendars) {
            Row row;
            Cell cell;
            int rowChapterCalendar = 1;
            int y = 2 + nextIter;

            row = sheet.createRow(y);
            cell = row.createCell(1);
            cell.setCellValue("Этап строительства " + calendar.getStage());
            cell.setCellStyle(stageStyle);
            cell = row.createCell(2);
            cell.setCellStyle(stageStyle);
            cell = row.createCell(3);
            cell.setCellStyle(stageStyle);
            cell = row.createCell(4);
            cell.setCellStyle(stageStyle);
            sheet.addMergedRegion(new CellRangeAddress(y, y, 1, 4));
            // создание ячеек сроков полевых ИИ
            if (!calendar.getEngineeringSurvey().equals(calendar.getStartContract())) {

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Полевые инженерные изыскания");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getStartContract());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEngineeringSurvey());
            }
            // создание ячеек сроков отчета ИИ
            if (!calendar.getEngineeringSurveyReport().equals(calendar.getStartContract())) {
                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Отчет по инженерным изысканиям");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEngineeringSurvey());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEngineeringSurveyReport());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar++ + "." + rowChapterCalendar);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Согласование инженерных изысканий");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEngineeringSurveyReport());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getAgreementEngineeringSurvey());
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков РД и СД
            if (!calendar.getWorkingStart().equals(calendar.getWorkingFinish())) {
                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Рабочая документация");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getWorkingStart());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getWorkingFinish());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Согласование рабочей документации");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getWorkingFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getAgreementWorking());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Сметная документация");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getWorkingFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEstimatesFinish());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar++ + "." + rowChapterCalendar);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Согласование сметной документации");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getEstimatesFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getAgreementEstimates());
                rowChapterCalendar = 1;
            }
            // создание ячеек сроков ПД
            if (!calendar.getProjectFinish().equals(calendar.getWorkingFinish())) {
                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Проектная документация");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getWorkingFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getProjectFinish());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar + "." + rowChapterCalendar++);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Согласование проектной документации");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getProjectFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getAgreementProject());

                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar++ + "." + rowChapterCalendar);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Экспертиза проектной документация");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getAgreementProject());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getExamination());
                rowChapterCalendar = 1;
            }
        // создание ячеек срока ЗУР
            if (!calendar.getProjectFinish().equals(calendar.getLandFinish())) {
                row = sheet.createRow(++y);
                cell = row.createCell(1);
                cell.setCellStyle(styleBorder);
                cell.setCellValue(chapterCalendar++ + "." + rowChapterCalendar);

                cell = row.createCell(2);
                cell.setCellStyle(styleBorder);
                cell.setCellValue("Землеустроительная документация");

                cell = row.createCell(3);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getProjectFinish());

                cell = row.createCell(4);
                cell.setCellStyle(style);
                cell.setCellValue(calendar.getLandFinish());
            }
            nextIter = y - 1;
        }
    }
}
