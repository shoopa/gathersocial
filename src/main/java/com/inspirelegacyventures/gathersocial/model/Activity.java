package com.inspirelegacyventures.gathersocial.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "activity",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"group_id", "date", "type"})})
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ActivityType type; // e.g., "BREAKFAST", "LUNCH", "DINNER", "COFFEE", "DRINKS"

    private String status; // e.g., "pending", "completed"
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private User host;

    @OneToMany(mappedBy = "activity")
    private Set<Vote> votes = new HashSet<>();
}
