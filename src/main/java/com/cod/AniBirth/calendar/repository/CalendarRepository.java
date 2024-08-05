package com.cod.AniBirth.calendar.repository;

import com.cod.AniBirth.calendar.entity.Calendar;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Calendar findByVolunteer(Volunteer volunteer);
}
