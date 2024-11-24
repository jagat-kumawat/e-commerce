package com.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class product {
    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id",nullable = false)
    private Long id;
    @Column(name = "name",nullable = false,unique = true)
    private String name;
    @Column(name = "short_description",nullable = false)
    private String shortdescription;
    @Column(name = "long_Description")
    private String longDescription;
    @Column(name = "price",nullable = false)
    private Double price;

    @OneToOne(mappedBy = "prod",cascade = CascadeType.REMOVE,optional = false,orphanRemoval = true)
    private Inventory inventory;

    @OneToMany(mappedBy = "proudt",cascade = CascadeType.REMOVE,orphanRemoval = true)
 private List<WebOrderQuantities> webOrderQuantities = new ArrayList<>();

}
