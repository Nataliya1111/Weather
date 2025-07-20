package com.nataliya.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class User {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Login", unique = true, nullable = false)
    private String login;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Column(name = "Password", nullable = false)
    private String password;
}
