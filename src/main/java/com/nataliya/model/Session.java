package com.nataliya.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Sessions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Session {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "Expires_At", nullable = false)
    private LocalDateTime expiresAt;
}
