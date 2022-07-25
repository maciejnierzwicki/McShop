package pl.maciejnierzwicki.mcshop.subpagesconfig;

import java.io.FileNotFoundException;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.properties.storage.ITextStorage;

@Slf4j
public class PageConfig {
	
	private String filePath;
	private String content;
	private ITextStorage storage;
	
	/***
	 * Constructs new PageConfig object and loads content from file pointed by filePath.
	 * @param filePath relative path to file containing text
	 */
	public PageConfig(String filePath, ITextStorage storage) {
		this.storage = storage;
		this.filePath = filePath;
		try {
			storage.copyFileFromJarIfNotExists(filePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		setContent(storage.load(filePath));
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void loadContentFromFile() {
		setContent(storage.load(filePath));
	}
	
	public void saveContentToFile() {
		if(content != null) {
			try {
				storage.save(content, filePath);
			} catch (FileNotFoundException e) {
				log.error(e.getMessage());
			}
		}
	}

}
