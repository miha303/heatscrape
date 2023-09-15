// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.common.service;

import com.github.miha303.heatscrape.common.model.HeatingData;

public interface HeatingDataService {
    HeatingData getHeatingData() throws HeatingDataNotAvailableException;

    HeatingData saveHeatingData(HeatingData heatingData);

    String transformToPrometheusMetrics(HeatingData heatingData);
}
