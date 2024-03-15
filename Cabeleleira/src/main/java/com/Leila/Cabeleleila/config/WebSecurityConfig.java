package com.Leila.Cabeleleila.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// Código importante -> Utiliza-se para remover o bloqueio em outros endereços
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desabilitar para fins de teste
                .cors().and() // Se estiver usando configurações de CORS, ajuste conforme necessário
                .authorizeRequests()
                .antMatchers("/**").permitAll() // Permitir todas as requisições para qualquer URL
                .anyRequest().authenticated();
    }
}
