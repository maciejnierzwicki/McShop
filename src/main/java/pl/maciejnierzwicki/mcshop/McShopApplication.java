package pl.maciejnierzwicki.mcshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class McShopApplication {
	
	private static ConfigurableApplicationContext context;
	
	@Autowired
	private McShopInitializer initializer;
	
	
	public McShopInitializer getMcShopInitializer() {
		return initializer;
	}
	
	@Bean
	public ThreadPoolTaskScheduler getScheduler(){
	    return new ThreadPoolTaskScheduler();
	}
	

	public static void main(String[] args) {
		context = SpringApplication.run(McShopApplication.class, args);
	}
	
    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(McShopApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }
    
    public static void stop() {
    	context.close();
    }

}
