package com.alcw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to all paths under /api/
                .allowedOrigins(
                        "http://localhost:5173", // Your Vite/React frontend development server
                        "https://alc-app-alpha.vercel.app"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allows all headers in the actual request
                .exposedHeaders("Authorization")
                .allowCredentials(true); // If your frontend sends cookies, auth headers, etc.
    }
}

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/")
//                .allowedOrigins(
//                        "http://localhost:5173",  // Dev environment
//                        "https://your-production-frontend.com"  // Add your production URL
//                )
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .exposedHeaders("Authorization")  // Explicitly expose Auth headers
//                .allowCredentials(true)
//                .maxAge(3600);  // Cache preflight response for 1 hour
//    }
//}
