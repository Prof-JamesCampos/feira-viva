package br.com.feiraviva.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI feiraVivaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Feira Viva API")
                        .version("0.9.0")
                        .description("Backend monolítico MVC do e-commerce Feira Viva. "
                                + "Documentação gerada automaticamente por springdoc-openapi.")
                        .contact(new Contact()
                                .name("Fábrica de Software")
                                .email("fabrica@feira-viva.local"))
                        .license(new License().name("Uso educacional")))
                .tags(List.of(
                        new Tag().name("Catálogo").description("Produtos e categorias"),
                        new Tag().name("Clientes").description("Cadastro e endereços"),
                        new Tag().name("Carrinho").description("Itens, cupom e estratégia de frete"),
                        new Tag().name("Pedidos").description("Finalização, histórico e cancelamento")
                ));
    }
}