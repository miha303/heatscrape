// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.metricservice.controllers;

import com.github.miha303.heatscrape.common.model.HeatingData;
import com.github.miha303.heatscrape.common.service.HeatingDataNotAvailableException;
import com.github.miha303.heatscrape.common.service.HeatingDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsServiceController {

    @Autowired HeatingDataService heatingDataService;

    @RequestMapping(
            value = "/metrics",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getData() throws HeatingDataNotAvailableException {
        HeatingData heatingData = heatingDataService.getHeatingData();

        return heatingDataService.transformToPrometheusMetrics(heatingData);
    }

    @PutMapping("/heatingData")
    public HeatingData putHeatingData(@RequestBody HeatingData data) {
        return heatingDataService.saveHeatingData(data);
    }
}
