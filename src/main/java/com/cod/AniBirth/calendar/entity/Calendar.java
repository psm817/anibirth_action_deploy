package com.cod.AniBirth.calendar.entity;

import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @OneToOne
    @JoinColumn(name = "volunteer_id", unique = true)
    @JsonIgnore
    private Volunteer volunteer;
}
