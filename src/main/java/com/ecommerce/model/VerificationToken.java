package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VerificationToken {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "id",nullable = false)
    private Long id;
@Lob
@Column(name = "token",nullable = false,unique = true)
private String token;
@Column(name = "created_timestamp",nullable = false)
private Timestamp createtimestamp;
@ManyToOne(cascade = CascadeType.PERSIST)
    private localuser user;


}
