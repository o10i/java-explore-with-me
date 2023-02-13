package ru.practicum.common.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.common.dto.Location;

import javax.persistence.*;

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
    String createdOn;
    @Column
    String description;
    @Column(nullable = false)
    String eventDate;
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
    String publishedOn;
    @Column
    Boolean requestModeration;
    @Column
    String state;
    @Column(nullable = false)
    String title;
    @Column
    Long views;
}
