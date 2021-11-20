package org.imgoing.api.config;

import lombok.RequiredArgsConstructor;
import org.imgoing.api.application.interceptor.PlanPermissionInterceptor;
import org.imgoing.api.application.interceptor.RoutinePermissionInterceptor;
import org.imgoing.api.application.interceptor.TaskPermissionInterceptor;
import org.imgoing.api.application.resolver.UserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserArgumentResolver userArgumentResolver;
    private final TaskPermissionInterceptor taskPermissionInterceptor;
    private final RoutinePermissionInterceptor routinePermissionInterceptor;
    private final PlanPermissionInterceptor planPermissionInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(taskPermissionInterceptor).addPathPatterns("/api/v1/tasks/**");
        registry.addInterceptor(routinePermissionInterceptor).addPathPatterns("/api/v1/routines/**");
        registry.addInterceptor(planPermissionInterceptor).addPathPatterns("/api/v1/plans/**");
    }
}
