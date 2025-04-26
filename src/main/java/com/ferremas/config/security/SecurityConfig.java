package com.ferremas.config.security;


import com.ferremas.serviceImpl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {



	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;



	@Bean
	    public static PasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }

	   @Bean
	    public MessageSource messageSource() {
	        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
	        messageSource.setBasename("classpath:messages");
	        messageSource.setDefaultEncoding("UTF-8");
	        return messageSource;
	    }

	@Bean
	MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector){
		   return new MvcRequestMatcher.Builder(introspector);
	}




	    @Bean
	    DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService);
	        authProvider.setPasswordEncoder(passwordEncoder());

	        return authProvider;
	    }

	    @Bean
	    public SecurityContextRepository securityContextRepository() {
	        return new HttpSessionSecurityContextRepository();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(
	            UserDetailsService userDetailsService,
	            PasswordEncoder passwordEncoder) {
	        var provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(userDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);
	        return new ProviderManager(authenticationProvider());
	    }

	@Bean
	public HttpFirewall allowUrlEncodedDoubleSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedDoubleSlash(true); // Permitir "//"
		firewall.setAllowBackSlash(true);             // (opcional)
		return firewall;
	}

	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http,MvcRequestMatcher.Builder mvc) throws Exception {
	        http


	            .csrf(AbstractHttpConfigurer::disable)
	            .authorizeHttpRequests(authorizeRequests ->
	                authorizeRequests
	                	.requestMatchers("/perfil/**").authenticated()
						.requestMatchers(mvc.pattern("/home/login.xhtml")).permitAll()
						//.requestMatchers("/admin/**").hasAuthority("ROLE_ADMINISTRADOR")
						.requestMatchers("/bodega/**").hasAuthority("ROLE_BODEGUERO")
						.requestMatchers("/contador/**").hasAuthority("ROLE_CONTADOR")
						.requestMatchers("/empleado/**").hasAnyRole("Empleado", "Administrador")
						.requestMatchers("/cliente/**").hasRole("Cliente")
						.requestMatchers("/paypal/**", "/home/**").permitAll()
	                    //.requestMatchers("/**").permitAll()
	                    .anyRequest().permitAll()
	            )

	            .exceptionHandling(exceptionHandling ->
	                exceptionHandling

	                    .accessDeniedPage("/home/index.xhtml")

	            )
	            .formLogin(formLogin ->
	                formLogin
	                	.loginPage("/home/login.xhtml")
	                	.loginProcessingUrl("/login")
	                    .defaultSuccessUrl("/")
						.permitAll().successHandler(authenticationSuccessHandler)
	            )
	            .logout(logout ->
	                logout
	                    .invalidateHttpSession(true)
	                    .clearAuthentication(true)
	                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                    .logoutSuccessUrl("/")
	                    .permitAll()
	            ).sessionManagement(session ->
							session.sessionFixation().none() // 🔒 importante para mantener el carrito
					);

	        return http.build();
	    }
}
