package pl.maciejnierzwicki.mcshop.properties.storage;

import java.util.Properties;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("!prod")
public class PropertiesStorageDEV implements IPropertiesStorage {
	
	public Properties load(String absoluteFilePath) {
		// Not loading from file in dev profile.
		return null;
	}

	public void save(Properties properties, String absoluteFilePath) {
		// Not doing anyting in dev profile.
	}

	public void copyFileFromJarIfNotExists(String filePath) {
		// Not doing anyting in dev profile.
	}

}
