package ru.practicum.common.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.dto.Location;
import ru.practicum.common.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @Column
    Long confirmedRequests;
    @Column
    LocalDateTime createdOn;
    @Column
    String description;
    @Column(nullable = false)
    LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    Location location;
    @Column(nullable = false)
    Boolean paid;
    @Column
    Integer participantLimit;
    @Column
    LocalDateTime publishedOn;
    @Column
    Boolean requestModeration;
    @Column
    State state;
    @Column(nullable = false)
    String title;
    @Column
    Long views;
}
