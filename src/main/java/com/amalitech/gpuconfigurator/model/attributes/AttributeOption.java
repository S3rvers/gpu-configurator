
package com.amalitech.gpuconfigurator.model.attributes;


import com.amalitech.gpuconfigurator.model.CompatibleOption;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attribute_options")
public class AttributeOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "price_adjustment")
    private BigDecimal priceAdjustment;

    @Column(name = "media")
    private String media;

    @Column(name="base_amount")
    private Float baseAmount;

    @Column(name="max_amount")
    private Float maxAmount;

    @Column(name="price_increment")
    private Double priceFactor;

    @ManyToOne
    private Attribute attribute;

    private Integer inStock;

    @OneToMany(mappedBy = "attributeOption", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<CompatibleOption> compatibleOptions;

    @Column(name = "createdAt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}