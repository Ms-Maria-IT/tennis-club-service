package com.tennistournament.clubservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "courts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(nullable = false, name = "court_name")
    private String courtName;

    @NotNull
    @Min(1)
    @Column(nullable = false, name = "court_number")
    private Integer courtNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "surface_type", nullable = false)
    private SurfaceType surfaceType;

    @Column(name = "is_indoor")
    @Builder.Default
    private Boolean isIndoor = false;

    @Column(name = "has_floodlights")
    @Builder.Default
    private Boolean hasFloodlights = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tennis_club_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private TennisClub tennisClub;

    public enum SurfaceType {
        HARD, CLAY, GRASS, CARPET, ARTIFICIAL_GRASS
    }

    public String getFullCourtName() {
        if (tennisClub != null && tennisClub.getName() != null) {
            return tennisClub.getName() + " - " + courtName;
        }
        return courtName;
    }

    public boolean isAvailable() {
        return isActive != null && isActive;
    }
}