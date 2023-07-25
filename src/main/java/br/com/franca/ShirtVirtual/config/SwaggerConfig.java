package br.com.franca.ShirtVirtual.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import java.util.Arrays;
import java.util.List;

@Component
@Configuration
@EnableSwagger2
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-07-20T17:03:50.911851568Z[GMT]")
public class SwaggerConfig implements WebMvcConfigurer {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private ApiKey apiKey(){
        return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(org.threeten.bp.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.threeten.bp.OffsetDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                .tags(
                        new Tag("Acessos", "Endpoints para gerenciamento de acessos"),
                        new Tag("Avaliacao-Produto", "Endpoints para gerenciamento de avaliação do produto"),
                        new Tag("Categoria-Produto", "Endpoints para gerenciamento de categorias"),
                        new Tag("Contas-Pagar", "Endpoints para gerenciamento de contas a pagar"),
                        new Tag("Contas-Receber", "Endpoints para gerenciamento de contas a receber"),
                        new Tag("Cupom-Desconto", "Endpoints para gerenciamento de cupom desconto"),
                        new Tag("Forma-Pagamento", "Endpoints para gerenciamento de fromas de pagamento"),
                        new Tag("Imagem-Produto", "Endpoints para gerenciamento de imagem produto"),
                        new Tag("Marca-Produto", "Endpoints para gerenciamento de marcas"),
                        new Tag("Nota-Fiscal-Compra", "Endpoints para gerenciamento de nota fiscal de compra"),
                        new Tag("Nota-Item-Produto", "Endpoints para gerenciamento de nota item produto"),
                        new Tag("Pessoa-Juridica", "Endpoints para gerenciamento de pessoas juridicas"),
                        new Tag("Pessoa-Fisica", "Endpoints para gerenciamento de pessoas fisicas"),
                        new Tag("Produtos", "Endpoints para gerenciamento de produtos"),
                        new Tag("Status-Rastreio", "Endpoints para gerenciamento de status de rastreio"),
                        new Tag("Venda-Compra-Loja-Virtual", "Endpoints para gerenciamento de venda"),
                        new Tag("Consultas-Externas", "Endpoints para consultas externas de CPF / CNPJ e CEP"),
                        new Tag("Gerador-Cpf", "Endpoints para consultas externas de CPF / CNPJ e CEP"),
                        new Tag("Gerador-Cnpj", "Endpoints para consultas externas de CPF / CNPJ e CEP")

                );
    }

    private SecurityContext securityContext(){
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth(){
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API - Ecommerce Loja Virtual")
                .description("Documentação da API - Ecommerce Loja Virtual")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("Tiago de França", "", "tiagofranca.rita@gmail.com"))
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}