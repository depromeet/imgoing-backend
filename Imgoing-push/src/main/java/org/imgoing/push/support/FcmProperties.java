package org.imgoing.push.support;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Getter
@Configuration
public class FcmProperties {
    @Value("${fcm.properties.path}")
    private String serviceAccountPath;
    @Value("${fcm.properties.url}")
    private String apiUrl;
}