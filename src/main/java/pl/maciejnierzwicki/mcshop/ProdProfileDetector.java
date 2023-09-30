package pl.maciejnierzwicki.mcshop;

import java.io.IOException;
import java.util.jar.JarFile;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.config.web.WebConfig;
import pl.maciejnierzwicki.mcshop.utils.FilesHelper;
import pl.maciejnierzwicki.mcshop.utils.JarUtils;

@Component
@Profile("prod")
@Slf4j
public class ProdProfileDetector {
	
	@PostConstruct
	private void preparePaymentProvidersDirectories() {
		FilesHelper.createDirectory("paymentconfig/sms/");
		FilesHelper.createDirectory("paymentconfig/banktransfer/");
	}
	
    /***
     * Calls methods provided by {@link JarUtils} to copy all template files into external directory named <strong>"templates"</strong>.
     */
	@PostConstruct
    private void copyTemplatesToOutsideJar() {
    	JarFile jar_file = JarUtils.jarForClass(WebConfig.class, null);
    	try {
			JarUtils.copyResourcesToDirectory(jar_file, "BOOT-INF/classes/templates", "/templates");
			log.debug("COPIED TEMPLATES");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
    }
	
}
