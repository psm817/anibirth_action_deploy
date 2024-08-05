package com.cod.AniBirth.calendar.service;

import com.cod.AniBirth.calendar.entity.Calendar;
import com.cod.AniBirth.calendar.repository.CalendarRepository;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final CalendarRepository calendarRepository;

    public void create(String title, LocalDateTime startDate, LocalDateTime endDate, Volunteer volunteer) {
        Calendar calendar = Calendar.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .volunteer(volunteer)
                .build();

        calendarRepository.save(calendar);
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public Calendar getByVolunteer(Volunteer volunteer) {
        return calendarRepository.findByVolunteer(volunteer);
    }

    public void modify(Calendar calendar, String title, LocalDateTime start, LocalDateTime end, Volunteer volunteer) {
        calendar.setTitle(title);
        calendar.setStartDate(start);
        calendar.setEndDate(end);
        calendar.setVolunteer(volunteer);

        calendarRepository.save(calendar);
    }
}
