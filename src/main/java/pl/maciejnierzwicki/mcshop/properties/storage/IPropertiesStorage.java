package pl.maciejnierzwicki.mcshop.properties.storage;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public interface IPropertiesStorage {
	
	/***
	 * 
	 * @param absoluteFilePath
	 * @return {@link Properties} (might be null)
	 */
	Properties load(String absoluteFilePath);
	
	/***
	 * 
	 * @param properties
	 * @param absoluteFilePath
	 * @throws FileNotFoundException 
	 */
	void save(Properties properties, String absoluteFilePath) throws FileNotFoundException;
	
	void copyFileFromJarIfNotExists(String filePath) throws Exception;

}
