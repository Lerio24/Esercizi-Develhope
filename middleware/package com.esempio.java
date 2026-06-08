package com.esempio.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

// ==========================================
// 1. CONTROLLERS
// ==========================================

@RestController
class BasicController {
    
    // GET http://localhost:8080/time
    @GetMapping("/time")
    public String getCurrentTime() {
        return "Current date/time: " + LocalDateTime.now();
    }
}

@RestController
class LegacyController {

    // GET http://localhost:8080/legacy
    @GetMapping("/legacy")
    public String getLegacyMessage() {
        return "This is just old code";
    }
}

// ==========================================
// 2. INTERCEPTORS / MIDDLEWARE
// ==========================================

@Component
class APILoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Estrae l'header User-Agent e lo stampa in console
        String userAgent = request.getHeader("User-Agent");
        System.out.println("[API LOG] Incoming request from User-Agent: " + userAgent);
        return true; // Continua l'esecuzione verso il controller
    }
}

@Component
class LegacyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Stampa di controllo nel terminale
        System.out.println("[LEGACY INTERCEPTOR] Blocking request to legacy endpoint.");
        
        // Imposta lo status HTTP su 410 Gone
        response.setStatus(HttpStatus.GONE.value()); // 410
        response.getWriter().write("Error: This endpoint is gone and no longer available.");
        
        return false; // Blocca la chiamata, impedendo di raggiungere il LegacyController
    }
}

// ==========================================
// 3. CONFIGURATION (Registrazione Interceptor)
// ==========================================

@Configuration
class WebConfig implements WebMvcConfigurer {

    private final APILoggingInterceptor apiLoggingInterceptor;
    private final LegacyInterceptor legacyInterceptor;

    public WebConfig(APILoggingInterceptor apiLoggingInterceptor, LegacyInterceptor legacyInterceptor) {
        this.apiLoggingInterceptor = apiLoggingInterceptor;
        this.legacyInterceptor = legacyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. APILoggingInterceptor si applica a tutte le rotte dell'applicazione
        registry.addInterceptor(apiLoggingInterceptor)
                .addPathPatterns("/**");

        // 2. LegacyInterceptor si attiva ESCLUSIVAMENTE sulla rotta /legacy
        registry.addInterceptor(legacyInterceptor)
                .addPathPatterns("/legacy");
    }
}