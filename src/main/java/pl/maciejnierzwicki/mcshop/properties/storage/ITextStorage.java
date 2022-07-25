package pl.maciejnierzwicki.mcshop.properties.storage;

import java.io.FileNotFoundException;

import org.springframework.stereotype.Component;

@Component
public interface ITextStorage {
	
	/***
	 * 
	 * @param absoluteFilePath
	 * @return {@link String} (might be null)
	 */
	String load(String absoluteFilePath);
	
	/***
	 * 
	 * @param content
	 * @param absoluteFilePath
	 * @throws FileNotFoundException 
	 */
	void save(String content, String absoluteFilePath) throws FileNotFoundException;
	
	void copyFileFromJarIfNotExists(String filePath) throws Exception;

}
