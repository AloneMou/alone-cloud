package com.alone.coder.framework.security.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Slf4j
@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AloneResourceServerConfiguration {

}
