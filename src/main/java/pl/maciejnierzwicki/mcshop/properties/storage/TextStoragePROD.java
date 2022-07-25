package pl.maciejnierzwicki.mcshop.properties.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Profile("prod")
@Slf4j
public class TextStoragePROD implements ITextStorage {
	
	/***
	 * 
	 * @param absoluteFilePath
	 * @return {@link Properties} or null if cannot find a file
	 */
	public String load(String filePath) {
		StringBuilder builder = new StringBuilder();
		try {
			File file = new File(filePath);
			if(!file.exists()) { return builder.toString(); }
			FileReader fr = new FileReader(file, StandardCharsets.UTF_8);
			try(BufferedReader br = new BufferedReader(fr)) {
				int c;
				while((c = br.read()) != -1) {
					builder.append((char) c);
				}
			}
			fr.close();
		}
		catch(IOException e) {
			log.error(e.getMessage());
		}
		return builder.toString();
	}
	
	/***
	 * 
	 * @param properties
	 * @param absoluteFilePath
	 * @throws FileNotFoundException 
	 */
	public void save(String content, String absoluteFilePath) throws FileNotFoundException {
		File file = new File(absoluteFilePath);
		if(!file.exists()) { throw new FileNotFoundException("Cannot find target file to store properties"); }
		try(FileOutputStream fos = new FileOutputStream(file)) {
			byte[] bytes = content.getBytes();
			fos.write(bytes);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}
	
	public void copyFileFromJarIfNotExists(String filePath) throws Exception {
		File file = new File(filePath);
		if(file.exists()) {
			return;
		}
		try {
			if(!file.createNewFile()) {
				throw new Exception("Cannot create file in location " + filePath);
			}
			if(!filePath.startsWith("/")) {
				filePath = "/" + filePath;
			}
			InputStream is = getClass().getResourceAsStream(filePath);
			byte[] bytes = is.readAllBytes();
			is.close();
			try(FileOutputStream fos = new FileOutputStream(file)) {
				fos.write(bytes);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
	}

}
