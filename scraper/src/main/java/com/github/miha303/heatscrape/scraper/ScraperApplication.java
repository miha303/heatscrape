// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.scraper;

import com.github.miha303.heatscrape.common.model.HeatingData;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ScraperApplication implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ScraperApplication.class);

    @Autowired private Config.ScraperConfig config;
    @Autowired private DataProviderServiceClient dataProviderServiceClient;
    @Autowired private MetricServiceClient metricServiceClient;

    public static void main(String[] args) {
        SpringApplication.run(ScraperApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        LOG.info("Starting periodic data refresh");
                        try {
                            HeatingData heatingData = dataProviderServiceClient.getData();
                            if (heatingData != null) {
                                LOG.info("Got data from boiler, setting to metric service");
                                metricServiceClient.setData(heatingData);
                            } else {
                                LOG.warn("No heating data received from boiler");
                            }
                        } catch (Exception e) {
                            LOG.error("Error occurred in periodic data refresh", e);
                        }
                        LOG.info("Finished periodic data refresh");
                    }
                },
                config.delay(),
                config.interval());
    }
}
