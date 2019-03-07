package cn.blmdz.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 API生成
 * 
 * @default_ui
 * http://domain/swagger-ui.html
 * @bootstrap_ui
 * http://domain/doc.html
 * 
 * @author xpoll
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 指定当前包路径
                .apis(RequestHandlerSelectors.basePackage("cn.blmdz"))
                // 扫描所有 .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("lalala")
                .description("lalala")
                .version("1.0")
                .termsOfServiceUrl("http://domain")
                .contact(new Contact("千酌一梦醉独殇", "https://xpoll.blmdz.cn", "blmdz521@126.com"))
                .build();
    }
}
