package dev.albertv.projects.api.core.config;

import dev.albertv.projects.api.core.properties.PortfolioPropertiesConfig;
import dev.albertv.projects.api.service.web.PortfolioWebService;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebServiceConfig {

    private <E> WebClient buildWebClient(final String baseUrl, final Class<E> webServiceInterface) {
        return WebClient
            .builder()
            .baseUrl(baseUrl)
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
            .filter((req, next) -> {
                LoggerFactory.getLogger(webServiceInterface).info("{} {}", req.method(), req.url());
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
    PortfolioWebService portfolioWebService(final PortfolioPropertiesConfig portfolioPropertiesConfig) {
        final Class<PortfolioWebService> clazz = PortfolioWebService.class;
        return buildWebService(
            buildWebClient(portfolioPropertiesConfig.api().url(), clazz),
            clazz
        );
    }

}
