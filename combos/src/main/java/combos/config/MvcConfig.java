package combos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Le dice a Spring Boot: "Todo lo que empiece con /uploads/, búscalo en la carpeta local 'uploads'"
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}