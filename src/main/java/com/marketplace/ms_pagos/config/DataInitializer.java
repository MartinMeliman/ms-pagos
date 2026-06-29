package com.marketplace.ms_pagos.config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Slf4j @Component
public class DataInitializer implements CommandLineRunner {
    @Override public void run(String... args){ log.info(">>> ms-pagos listo en puerto 8087."); }
}
