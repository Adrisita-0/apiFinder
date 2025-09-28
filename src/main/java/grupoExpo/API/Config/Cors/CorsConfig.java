package grupoExpo.API.Config.Cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration // Marca esta clase como configuración de Spring (se ejecuta al iniciar la app)
public class CorsConfig {

    //  Primer método: configura un Filtro CORS global
    @Bean
    public CorsFilter corsFilter() {
        // Fuente de configuraciones CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir enviar credenciales (cookies, tokens, etc.)
        config.setAllowCredentials(true);

        // Dominios que tienen permitido hacer peticiones al backend
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("https://localhost");
        config.addAllowedOrigin("http://localhost:3000"); // React
        config.addAllowedOrigin("http://localhost:8080"); // Vue
        config.addAllowedOrigin("http://localhost:4200"); // Angular
        config.addAllowedOrigin("https://learn-api-steel.vercel.app/"); // Producción

        // Métodos HTTP permitidos
        config.addAllowedMethod("GET");     // Consultar datos
        config.addAllowedMethod("POST");    // Enviar datos
        config.addAllowedMethod("PUT");     // Reemplazar datos
        config.addAllowedMethod("DELETE");  // Eliminar datos
        config.addAllowedMethod("OPTIONS"); // Verificación previa
        config.addAllowedMethod("PATCH");   // Actualizar parcialmente

        // Cabeceras permitidas en las peticiones
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("X-Requested-With");
        config.addAllowedHeader("Access-Control-Request-Method");
        config.addAllowedHeader("Access-Control-Request-Headers");
        config.addAllowedHeader("Cookie");
        config.addAllowedHeader("Set-Cookie");

        // Cabeceras que estarán disponibles en la respuesta del backend
        config.setExposedHeaders(Arrays.asList(
                "Set-Cookie", "Cookie", "Authorization", "Content-Disposition"
        ));

        // Tiempo en segundos que el navegador guarda la configuración (1 hora)
        config.setMaxAge(3600L);

        // Se aplican estas reglas a todas las rutas del backend
        source.registerCorsConfiguration("/**", config);

        // Devuelve el filtro con las configuraciones
        return new CorsFilter(source);
    }

    // 🔹 Segundo método: configuración CORS para integrarlo con Spring Security
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Permitir credenciales (cookies/tokens)
        configuration.setAllowCredentials(true);

        // Dominios permitidos
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedOrigin("https://localhost");
        configuration.addAllowedOrigin("http://localhost");
        configuration.addAllowedOrigin("https://*.herokuapp.com"); // apps desplegadas en Heroku
        configuration.addAllowedOrigin("https://learn-api-steel.vercel.app/"); // producción

        // Permitir todos los métodos (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedMethod("*");

        // Permitir todas las cabeceras
        configuration.addAllowedHeader("*");

        // Cabeceras expuestas al cliente (el frontend puede leerlas en la respuesta)
        configuration.addExposedHeader("Set-Cookie");
        configuration.addExposedHeader("Cookie");
        configuration.addExposedHeader("Authorization");

        // Se aplican las configuraciones a todas las rutas
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
