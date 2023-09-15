// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.dataproviderservice.controllers;

import com.github.miha303.heatscrape.common.model.HeatingData;
import com.github.miha303.heatscrape.common.service.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataProviderServiceController {

    @Autowired DataProviderService dataProviderService;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public HeatingData getData() {
        return dataProviderService.pullData();
    }
}
