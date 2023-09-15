// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.dataproviderservice;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.miha303.heatscrape.dataproviderservice.controllers.DataProviderServiceController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DataProviderServiceApplicationTests {

    @Autowired DataProviderServiceController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
