package com.example.attendancebackend.importdata;

import org.apache.poi.ss.usermodel.*;

import com.example.attendancebackend.model.Event;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelEventImporter {
    public Event convertRow(Row row) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String date = row.getCell(0).toString();
        String time = row.getCell(1).toString();

        DataFormatter dataFormatter = new DataFormatter();

        String homeTeam = dataFormatter.formatCellValue(row.getCell(2));
        String awayTeam = dataFormatter.formatCellValue(row.getCell(3));
        String location = dataFormatter.formatCellValue(row.getCell(4));
        String field = dataFormatter.formatCellValue(row.getCell(5));
        String externalId = dataFormatter.formatCellValue(row.getCell(8));

        LocalDateTime startDateTime = LocalDateTime.parse(
                date + " " + time,
                formatter
        );
        LocalDateTime endDateTime = startDateTime.plusHours(2).plusMinutes(30);

        String description = homeTeam
                + " vs "
                + awayTeam
                + " - Veld: "
                + field;

        Event event = new Event();

        event.setExternalId(externalId);
        event.setStartDateTime(startDateTime);
        event.setEndDateTime(endDateTime);
        event.setLocation(location);
        event.setDescription(description);

        return event;
    }

    public List<Event> importEvents(InputStream inputStream) throws IOException {
        List<Event> events = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // Skip header
                if (row.getRowNum() == 0) {
                    continue;
                }

                // Skip completely empty rows
                if (row.getLastCellNum() == -1) {
                    continue;
                }

                Event event = convertRow(row);
                events.add(event);
            }
        }

        return events;
    }
}