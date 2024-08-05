package com.cod.AniBirth.calendar.controller;

import com.cod.AniBirth.calendar.entity.Calendar;
import com.cod.AniBirth.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/events")
    public List<Map<String, Object>> getEvents() {
        List<Calendar> calendars = calendarService.getAllCalendars();
        List<Map<String, Object>> events = new ArrayList<>();

        for (Calendar calendar : calendars) {
            Map<String, Object> event = new HashMap<>();
            event.put("title", calendar.getTitle());
            event.put("startDate", calendar.getStartDate());
            event.put("endDate", calendar.getEndDate());
            event.put("volunteer", Map.of("id", calendar.getVolunteer().getId()));
            events.add(event);
        }

        return events;
    }
}