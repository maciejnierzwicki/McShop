package pl.maciejnierzwicki.mcshop.config.web;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.UrlTemplateResolver;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer, ApplicationContextAware {
	
	@Autowired
	private AppSetupModeInterceptor setup_mode_interceptor;
	@Autowired
	private AppNotSetupModeInterceptor normal_mode_interceptor;
	@Autowired
	private ServerListToModelAppender server_list_appender;
	@Autowired
	private AppPropertiesToModelAppender app_properties_appender;
	@Autowired
	private InvalidOrderAccessRedirection invalid_order_redirection;
	@Autowired
	private PaymentProvidersToModelAppender payments_providers_appender;
	@Autowired
	private SessionOrderCleaner session_order_cleaner;
	@Autowired
	private String templatesDirPath;
	@Value("${spring.thymeleaf.cache}")
	private boolean templateCaching;
	
	private ApplicationContext applicationContext;
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
		
	}
	
	/***
	 * Configures and includes all custom implementations of {@link HandlerInterceptor} to be used by app.
	 */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(setup_mode_interceptor).addPathPatterns("/**").excludePathPatterns("/setup/**", "/css/**", "/js/**", "/images/**", "/bootstrap-custom/**", "/serversColumn");
        registry.addInterceptor(normal_mode_interceptor).addPathPatterns("/setup/**");
        registry.addInterceptor(server_list_appender);
        registry.addInterceptor(app_properties_appender);
        registry.addInterceptor(invalid_order_redirection).addPathPatterns("/order/**");
        registry.addInterceptor(payments_providers_appender).addPathPatterns("/account/addfunds/**", "/order/**");
        registry.addInterceptor(session_order_cleaner).addPathPatterns("/**").excludePathPatterns("/service/**", "/order/**", "/css/**", "/js/**", "/images/**", "/bootstrap-custom/**", "/serversColumn", "/account/orders/**", "/account/addfunds/**");
    }
    
    /***
     * Prepares template engine to be used by app.<br>
     * This includes copying template files outside jar if app is running in production profile.
     * @return {@link SpringTemplateEngine} 
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        UrlTemplateResolver urlResolver = new UrlTemplateResolver();
        urlResolver.setPrefix("");
        urlResolver.setSuffix("");
        urlResolver.setCacheable(templateCaching);
        log.debug("template caching: " + templateCaching);
        urlResolver.setCharacterEncoding("UTF-8");
        templateEngine.addTemplateResolver(urlResolver);
        templateEngine.addDialect(springSecurityDialect());
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(templateCaching);
        templateResolver.setPrefix(templatesDirPath);

        log.debug("Templates dir path: " + templatesDirPath);
        templateResolver.setSuffix(".html");
        templateEngine.addTemplateResolver(templateResolver);
        return templateEngine;
    }
    
    /***
     * Method necessary to make use of Spring Security in Thymeleaf templates.
     * @return {@link SpringSecurityDialect}
     */
    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }


}
