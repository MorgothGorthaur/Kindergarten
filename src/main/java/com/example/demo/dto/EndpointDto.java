package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndpointDto {
    @Value("${project.login.url}")
    private String loginUrl;
    @Value("${project.refresh.url}")
    private String refreshUrl;
}
