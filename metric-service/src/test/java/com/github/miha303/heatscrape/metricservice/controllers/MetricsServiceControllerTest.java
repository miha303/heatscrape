// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.metricservice.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.miha303.heatscrape.common.model.HeatingData;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MetricsServiceControllerTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void controller() {
        String response =
                restTemplate.getForObject("http://localhost:" + port + "/metrics", String.class);
        assertThat(response).isNull();

        HeatingData heatingData =
                new HeatingData(
                        BigDecimal.valueOf(1),
                        BigDecimal.valueOf(2),
                        66,
                        BigDecimal.valueOf(3),
                        33,
                        true,
                        false,
                        BigDecimal.valueOf(4),
                        BigDecimal.valueOf(5),
                        BigDecimal.valueOf(6),
                        BigDecimal.valueOf(7),
                        BigDecimal.valueOf(8),
                        BigDecimal.valueOf(9),
                        false);
        restTemplate.put("http://localhost:" + port + "/heatingData", heatingData);
        response = restTemplate.getForObject("http://localhost:" + port + "/metrics", String.class);
        assertThat(response)
                .contains(
                        "out_flow_temperature 1\n"
                                + "return_flow_temperature 2\n"
                                + "fan_speed 66\n"
                                + "ionization_current 3\n"
                                + "power 33\n"
                                + "heating_required 1\n"
                                + "dhw_heating_required 0\n"
                                + "outside_temperature 4\n"
                                + "dwh_temperature 5\n"
                                + "outflow_temperature_circuit_b 6\n"
                                + "outflow_temperature_circuit_c 7\n"
                                + "room_temperature_b 8\n"
                                + "room_temperature_c 9\n"
                                + "flame 0");
    }
}
