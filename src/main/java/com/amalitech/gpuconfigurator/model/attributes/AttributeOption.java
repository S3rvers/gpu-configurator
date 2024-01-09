package com.amalitech.gpuconfigurator.model.attributes;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attributeOptions")
public class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "optionName", nullable = false)
    private String optionName;

    @Column(name = "priceAdjustment")
    private BigDecimal priceAdjustment;

    @ManyToOne
    @JoinColumn(name = "attribute_id", nullable = false)
    private Attribute attribute;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
