package pl.maciejnierzwicki.mcshop.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import pl.maciejnierzwicki.mcshop.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(8);
	}
	
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
	
    /*
	@Bean("authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
    */
	
	@Bean("authenticationManager")
	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userService) 
	  throws Exception {
	    return http.getSharedObject(AuthenticationManagerBuilder.class)
	      .userDetailsService(userDetailsService)
	      .passwordEncoder(encoder())
	      .and()
	      .build();
	}
/*
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	*/
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {	
		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()).expiredUrl("/login");
		http.csrf().ignoringAntMatchers("/urlc/**", "/order/finish**").and().authorizeRequests()
		.antMatchers("/admin/**").hasAuthority("ADMIN")
		.antMatchers("/account/**").authenticated()
		.antMatchers("/", "/**").permitAll()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/account")
		.and()
			.logout().logoutSuccessUrl("/");
	}
	*/
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry()).expiredUrl("/login");
		http.csrf().ignoringRequestMatchers("/urlc/**", "/order/finish**").and().authorizeRequests()
		.requestMatchers("/admin/**").hasAuthority("ADMIN")
		.requestMatchers("/account/**").authenticated()
		.requestMatchers("/", "/**").permitAll()
		.and()
			.formLogin().loginPage("/login").defaultSuccessUrl("/account")
		.and()
			.logout().logoutSuccessUrl("/");

	    return http.build();
	}

}
