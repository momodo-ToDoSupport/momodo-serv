package com.momodo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // CORS 설정 적용할 API URI
                .allowedOrigins("https://momodo-front.vercel.app:443")            // 허용할 출처
                .allowCredentials(true);        // 인증 요청 허용
    }
}
