package com.pricekart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "spec_key", nullable = false, length = 100)
    private String key;

    @Column(name = "spec_value", nullable = false, length = 255)
    private String value;
}
