package com.eviive.personalapi.config;

import com.eviive.personalapi.properties.PortfolioPropertiesConfig;
import com.eviive.personalapi.service.web.PortfolioWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@Slf4j
public class WebServiceConfig {

    private WebClient buildWebClient(final String baseUrl) {
        return WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
            .filter((req, next) -> {
                log.info("{} {}", req.method(), req.url());
                return next.exchange(req);
            })
            .build();
    }

    private <E> E buildWebService(final WebClient webClient, final Class<E> webServiceInterface) {
        return HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(webClient))
            .build()
            .createClient(webServiceInterface);
    }

    @Bean
    public PortfolioWebService portfolioWebService(final PortfolioPropertiesConfig portfolioPropertiesConfig) {
        return buildWebService(
            buildWebClient(portfolioPropertiesConfig.api().url()),
            PortfolioWebService.class
        );
    }

}
