package com.nataliya.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Locations")
@NoArgsConstructor
@Getter
@Setter
public class Location {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "Latitude", nullable = false)
    private BigDecimal latitude;

    @Column(name = "Longitude", nullable = false)
    private BigDecimal longitude;
}
