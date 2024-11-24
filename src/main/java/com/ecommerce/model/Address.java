package com.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
       private Long id;


    @Column(name = "address_line1",length = 100)
    private String addressLine1;
    @Column(name = "address_line2",length = 100)
    private String addressLine2;
    @Column(name = "city",length = 100)
    private String city;
    @Column(name = "country",length = 100)
    private String country;
@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private localuser user;
//@JsonIgnore
//    @OneToMany(mappedBy = "address",fetch = FetchType.EAGER,cascade  =CascadeType.REMOVE,orphanRemoval = true)
//    private List<WebOrder> webOrders = new ArrayList<>();
}

