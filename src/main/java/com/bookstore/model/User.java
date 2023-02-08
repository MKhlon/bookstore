package com.bookstore.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToOne()
    @JoinColumn(name = "role_id")
    private Role role;
    @Column(name = "email")
    private String email;
    @Column(name = "phone")
    private Double phone;
    @Column(name = "address")
    private String address;
    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
}