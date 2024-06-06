package com.inspirelegacyventures.gathersocial.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "user_entity")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(unique = true)
    private String email;

    private String firstname;
    private String lastname;
    private String homeLocation;
    private String currentLocation;

    @ElementCollection
    @CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "activity_type")
    @Column(name = "preferences")
    private Map<ActivityType, String> activityPreferences = new HashMap<>();

    @OneToMany(mappedBy = "user")
    private Set<Vote> votes = new HashSet<>();
}
