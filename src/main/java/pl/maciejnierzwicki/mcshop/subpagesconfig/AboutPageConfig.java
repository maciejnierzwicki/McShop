package pl.maciejnierzwicki.mcshop.subpagesconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.properties.storage.ITextStorage;

@Component
public class AboutPageConfig extends PageConfig {

	@Autowired
	public AboutPageConfig(@Autowired String aboutSubpageFilePath, @Autowired ITextStorage storage) {
		super(aboutSubpageFilePath, storage);
	}

}
