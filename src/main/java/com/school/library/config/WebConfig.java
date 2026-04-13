package com.school.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Thay allowedOrigins("*") thành allowedOriginPatterns("*")
//                .allowedOriginPatterns("")
                .allowedOrigins("https://library-hcm-64ve.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để xem/tải PDF
        // Lưu ý: Thay đường dẫn "file:..." bằng đường dẫn thực tế trên Mac của bạn
        registry.addResourceHandler("/uploads/pdfs/**")
                .addResourceLocations("file:/Users/mac/iCloud Drive (Archive) - 1/Documents/reactjs/tutuong-HCM/src/pdfs/");
    }
}