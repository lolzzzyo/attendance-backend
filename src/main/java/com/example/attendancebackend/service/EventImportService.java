package com.example.attendancebackend.service;

import com.example.attendancebackend.importdata.ExcelEventImporter;
import com.example.attendancebackend.model.Event;
import com.example.attendancebackend.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class EventImportService {

    private final ExcelEventImporter excelEventImporter;
    private final EventRepository eventRepository;

    public EventImportService(
            ExcelEventImporter excelEventImporter,
            EventRepository eventRepository
    ) {
        this.excelEventImporter = excelEventImporter;
        this.eventRepository = eventRepository;
    }

    public void importEvents(InputStream inputStream) throws IOException {

        List<Event> events = excelEventImporter.importEvents(inputStream);

        for (Event event : events) {

            Event existingEvent = eventRepository
                    .findByExternalId(event.getExternalId())
                    .orElse(null);

            if (existingEvent != null) {
                boolean changed = false;

                if (!existingEvent.getStartDateTime().equals(event.getStartDateTime())) {
                    existingEvent.setStartDateTime(event.getStartDateTime());
                    changed = true;
                }

                if (!existingEvent.getEndDateTime().equals(event.getEndDateTime())) {
                    existingEvent.setEndDateTime(event.getEndDateTime());
                    changed = true;
                }

                if (!existingEvent.getLocation().equals(event.getLocation())) {
                    existingEvent.setLocation(event.getLocation());
                    changed = true;
                }

                if (!existingEvent.getDescription().equals(event.getDescription())) {
                    existingEvent.setDescription(event.getDescription());
                    changed = true;
                }

                if (changed) {
                    eventRepository.save(existingEvent);
                }

            } else {
                eventRepository.save(event);
            }
        }
    }
}
