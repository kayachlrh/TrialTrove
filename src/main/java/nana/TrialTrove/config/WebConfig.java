package nana.TrialTrove.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /uploads/** 경로를 물리적인 경로와 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/Users/nana/Documents/work/uploads/");
    }
}
