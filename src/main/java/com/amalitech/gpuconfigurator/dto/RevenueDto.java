package com.amalitech.gpuconfigurator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDto {
    private List<DayOfWeek> dayOfWeeks;
    private List<BigDecimal> revenue;
}
