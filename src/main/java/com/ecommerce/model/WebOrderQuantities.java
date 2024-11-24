package com.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class WebOrderQuantities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(name = "quantity",nullable = false)
    private Integer quantity;
@JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id",nullable = false)
    private product proudt;

@JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name="order_id",nullable = false)
    private WebOrder webOrder;
}
