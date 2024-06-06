package com.inspirelegacyventures.gathersocial.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @ManyToMany(mappedBy = "members")
    @JsonManagedReference
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Vote> votes = new HashSet<>();

    public void addGroup(Group group) {
        this.groups.add(group);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", homeLocation='" + homeLocation + '\'' +
                ", currentLocation='" + currentLocation + '\'' +
                '}';
    }
}
