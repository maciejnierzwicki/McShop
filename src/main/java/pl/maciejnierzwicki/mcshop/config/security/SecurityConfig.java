package pl.maciejnierzwicki.mcshop.config.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Profiles;
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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

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
    
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
    	return new MvcRequestMatcher.Builder(introspector);
    }
	
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
	
	@SuppressWarnings("deprecation")
	@Bean
	@Profile("prod")
	public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		http.sessionManagement(management -> management.maximumSessions(1).sessionRegistry(sessionRegistry()).expiredUrl("/login"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/paymentvalidation/**"), antMatcher("/order/finish**"))).authorizeRequests(requests -> requests
                .requestMatchers(antMatcher("/admin/**")).hasAuthority("ADMIN")
                .requestMatchers(antMatcher("/account/**")).authenticated()
                .requestMatchers(antMatcher("/")).permitAll()
                .requestMatchers(antMatcher("/**")).permitAll())
                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/account"))
                .logout(logout -> logout.logoutSuccessUrl("/"));
        http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
	    return http.build();
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	@Profile("test | default")
	public SecurityFilterChain filterChainTest(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		http.sessionManagement(management -> management.maximumSessions(1).sessionRegistry(sessionRegistry()).expiredUrl("/login"));
		http.csrf(csrf -> csrf.ignoringRequestMatchers(antMatcher("/paymentvalidation/**"), antMatcher("/order/finish**"))).authorizeRequests(requests -> requests
                .requestMatchers(antMatcher("/admin/**")).hasAuthority("ADMIN")
                .requestMatchers(antMatcher("/account/**")).authenticated()
                .requestMatchers(antMatcher("/")).permitAll()
                .requestMatchers(antMatcher("/**")).permitAll())
                .formLogin(login -> login.loginPage("/login").defaultSuccessUrl("/account"))
                .logout(logout -> logout.logoutSuccessUrl("/"));
	    return http.build();
	}

}
