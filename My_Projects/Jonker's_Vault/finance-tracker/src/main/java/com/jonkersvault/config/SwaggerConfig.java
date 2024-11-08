package com.jonkersvault.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // Define development server
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080"); // Adjust the URL if different
        devServer.setDescription("Development server URL");

        // Define production server
        Server prodServer = new Server();
        prodServer.setUrl("https://your-prod-url.com"); // Replace with your production URL
        prodServer.setDescription("Production server URL");

        // Contact information
        Contact contact = new Contact();
        contact.setName("Jonker's Vault Support");
        contact.setEmail("jonkerpierre637@gmail.com");
        contact.setUrl("https://www.jonkersvault.com");

        // License information
        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        // API Info
        Info info = new Info()
                .title("Jonker's Vault API")
                .version("1.0")
                .description("This API provides endpoints for Jonker's Vault application.")
                .termsOfService("https://www.jonkersvault.com/terms")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }
}
