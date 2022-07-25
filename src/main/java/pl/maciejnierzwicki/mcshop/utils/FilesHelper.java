package pl.maciejnierzwicki.mcshop.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FilesHelper {
	
	public static void createDirectory(String path) {
		File file = new File(path);
		file.mkdirs();
	}
	
	public static void createEmptyFileIfNotPresent(String fullPath) throws Exception {
		try {
			File file = new File(fullPath);
			if(!file.exists()) {
				if(!file.createNewFile()) {
					throw new Exception("Cannot create file in location " + fullPath);
				}
			}
		}
		catch(IOException e) { 
			log.error(e.getMessage());
		}
	}
	
	public static String getTextContentFromFile(String path) {
		StringBuilder builder = new StringBuilder();
		try {
			File file = ResourceUtils.getFile(path);
			if(!file.exists()) { throw new FileNotFoundException("File not exists " + path); }
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
	
	public static void saveTextContentToFile(String content, String path) {
		try {
			File file = ResourceUtils.getFile(path);
			FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8);
			try(BufferedWriter bw = new BufferedWriter(fw)) {
				bw.write(content); 
			}
			fw.close();
		}
		catch(IOException e) {
			log.error(e.getMessage());
		}
	}

}
