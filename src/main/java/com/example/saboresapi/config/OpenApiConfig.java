package com.example.saboresapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Sabores API - Sistema de Gestión de Restaurante")
                        .version("1.0.0")
                        .description("""
                                API REST completa para gestión de restaurante con las siguientes funcionalidades:

                                **Características principales:**
                                - 🔐 Autenticación JWT con roles (ADMIN, VENDEDOR, CLIENTE)
                                - 👥 Gestión de usuarios con encriptación BCrypt
                                - 📦 CRUD completo de productos y categorías
                                - 🧾 Sistema de boletas y detalles de venta
                                - 🖼️ Gestión de imágenes de productos

                                **Roles y permisos:**
                                - **ADMIN**: Acceso completo a todos los endpoints
                                - **VENDEDOR**: Consulta de boletas y detalles
                                - **CLIENTE**: Creación de boletas y detalles

                                **Uso de la API:**
                                1. Registrarse o iniciar sesión en `/api/v1/auth/login`
                                2. Copiar el token JWT recibido
                                3. Hacer clic en el botón "Authorize" (🔓) arriba
                                4. Pegar el token en el campo "Value" (sin "Bearer")
                                5. Ahora puedes usar todos los endpoints autorizados
                                """)
                        .contact(new Contact()
                                .name("Equipo Sabores de Chile")
                                .email("contacto@sabores.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingrese el token JWT obtenido del endpoint /api/v1/auth/login")));
    }
}
