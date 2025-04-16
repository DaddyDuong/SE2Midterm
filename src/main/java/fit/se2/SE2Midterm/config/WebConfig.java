package fit.se2.SE2Midterm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
                
        // Add resource handler for uploads directory
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("classpath:/static/uploads/", "file:uploads/");
    }
}