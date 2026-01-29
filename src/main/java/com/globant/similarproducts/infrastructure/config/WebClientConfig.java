package com.globant.similarproducts.infrastructure.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient externalProductApiWebClient(ExternalProductApiProperties props) {

        HttpClient httpClient = HttpClient.create()
                // timeout de conexión TCP
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, props.getTimeoutMs())
                // timeout total para recibir la respuesta (cabeceras/body)
                .responseTimeout(Duration.ofMillis(props.getTimeoutMs()))
                // timeouts de lectura/escritura a nivel de socket
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(props.getTimeoutMs(), TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(props.getTimeoutMs(), TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}