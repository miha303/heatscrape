// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.scraper;

import com.github.miha303.heatscrape.common.model.HeatingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Configuration
@EnableDiscoveryClient
public class MetricServiceClient {
    @Autowired private Client client;

    @FeignClient(name = "metric-service")
    interface Client {

        @RequestMapping(path = "/heatingData", method = RequestMethod.PUT)
        HeatingData setData(HeatingData data);
    }

    public HeatingData setData(HeatingData data) {
        return client.setData(data);
    }
}
