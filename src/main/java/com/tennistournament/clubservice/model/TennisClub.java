package com.tennistournament.clubservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tennis_clubs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "courts")
public class TennisClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String address;

    @Pattern(regexp = "^\\+?[0-9\\s\\-()]{7,20}$")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @Size(max = 100)
    private String email;

    @Column(name = "opening_time")
    private LocalTime openingTime;

    @Column(name = "closing_time")
    private LocalTime closingTime;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @OneToMany(mappedBy = "tennisClub", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Court> courts = new ArrayList<>();

    public void addCourt(Court court) {
        courts.add(court);
        court.setTennisClub(this);
    }

    public void removeCourt(Court court) {
        courts.remove(court);
        court.setTennisClub(null);
    }

    public boolean isOpenAt(LocalTime time) {
        if (time == null || openingTime == null || closingTime == null) {
            return false;
        }
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
}