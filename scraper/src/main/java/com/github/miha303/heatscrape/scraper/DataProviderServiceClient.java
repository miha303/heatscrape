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
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@EnableDiscoveryClient
public class DataProviderServiceClient {
    @Autowired private Client client;

    @FeignClient(name = "data-provider-service")
    interface Client {

        @RequestMapping(path = "/data", method = RequestMethod.GET)
        @ResponseBody
        HeatingData getData();
    }

    public HeatingData getData() {
        return client.getData();
    }
}
