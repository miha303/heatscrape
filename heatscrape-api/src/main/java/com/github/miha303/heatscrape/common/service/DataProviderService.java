// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.common.service;

import com.github.miha303.heatscrape.common.model.HeatingData;

public interface DataProviderService {
    HeatingData pullData();
}
