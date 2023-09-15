// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.common.model;

import java.math.BigDecimal;

public record HeatingData(
        BigDecimal outFlowTemperature,
        BigDecimal returnFlowTemperature,
        int fanSpeed,
        BigDecimal ionizationCurrent,
        int power,
        boolean heatingRequired,
        boolean dhwHeatingRequired,
        BigDecimal outsideTemperature,
        BigDecimal dwhTemperature,
        BigDecimal outflowTemperatureCircuitB,
        BigDecimal outflowTemperatureCircuitC,
        BigDecimal roomTemperatureB,
        BigDecimal roomTemperatureC,
        boolean flame) {}
