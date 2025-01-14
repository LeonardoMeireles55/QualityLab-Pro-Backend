package leonardo.labutilities.qualitylabpro.configs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(
				"http://localhost:3000, https://quality-lab-pro.vercel.app, https://68.183.141.155, https://68.183.141.155, https://leomeireles-dev.xyz, https://leomeireles-dev.xyz")
				.allowedMethods("GET", "POST").allowedHeaders("*").allowCredentials(true)
				.maxAge(3600);
	}
}
