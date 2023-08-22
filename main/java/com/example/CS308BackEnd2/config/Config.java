package com.example.CS308BackEnd2.config;


import com.example.CS308BackEnd2.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class Config {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public Config(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


}
