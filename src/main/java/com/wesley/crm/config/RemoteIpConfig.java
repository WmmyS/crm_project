package com.wesley.crm.config;

import org.apache.catalina.valves.RemoteIpValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RemoteIpConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> remoteIpValveCustomizer() {
        return factory -> {
            RemoteIpValve valve = new RemoteIpValve();
            valve.setRemoteIpHeader("X-Forwarded-For");
            valve.setProxiesHeader("X-Forwarded-By");
            valve.setProtocolHeader("X-Forwarded-Proto");
            valve.setPortHeader("X-Forwarded-Port");
            valve.setInternalProxies("10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "192\\.168\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "169\\.254\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "127\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "172\\.1[6-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "172\\.2[0-9]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "172\\.3[0-1]{1}\\.\\d{1,3}\\.\\d{1,3}|" +
                                   "0:0:0:0:0:0:0:1|::1");
            factory.addEngineValves(valve);
        };
    }
}