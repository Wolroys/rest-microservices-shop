package com.wolroys.orderservice.configuration;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfiguration {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String secretId;

    private PayPalEnvironment environment;


    @PostConstruct
    public void init(){
        this.environment = new PayPalEnvironment.Sandbox(clientId, secretId);
    }

    @Bean
    public PayPalHttpClient payPalClient(){
        return new PayPalHttpClient(environment);
    }
}
