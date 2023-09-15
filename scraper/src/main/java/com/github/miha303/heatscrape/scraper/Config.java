// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.scraper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan
@EnableCaching
@EnableFeignClients
public class Config {

    @ConfigurationProperties(prefix = "scraper")
    public record ScraperConfig(int delay, int interval) {}
}
