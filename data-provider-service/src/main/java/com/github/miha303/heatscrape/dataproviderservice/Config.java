// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.dataproviderservice;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@EnableCaching
public class Config {

    @ConfigurationProperties(prefix = "boiler")
    public record ServiceConfig(String address, int port) {}
}
