package pl.maciejnierzwicki.mcshop.properties.storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
public class TextStorageDEV implements ITextStorage {
	
	public String load(String absoluteFilePath) {
		// Not loading from file in dev profile.
		return null;
	}

	public void save(String content, String absoluteFilePath) {
		// Not doing anyting in dev profile.
	}

	public void copyFileFromJarIfNotExists(String filePath) {
		// Not doing anyting in dev profile.
	}

}
