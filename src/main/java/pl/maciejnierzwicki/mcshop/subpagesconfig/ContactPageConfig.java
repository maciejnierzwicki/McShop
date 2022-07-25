package pl.maciejnierzwicki.mcshop.subpagesconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.properties.storage.ITextStorage;

@Component
public class ContactPageConfig extends PageConfig {
	
	@Autowired
	public ContactPageConfig(@Autowired String contactSubpageFilePath, @Autowired ITextStorage storage) {
		super(contactSubpageFilePath, storage);
	}

}
