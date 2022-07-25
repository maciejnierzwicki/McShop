package pl.maciejnierzwicki.mcshop.subpagesconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.maciejnierzwicki.mcshop.properties.storage.ITextStorage;

@Component
public class RulesPageConfig extends PageConfig {
	
	@Autowired
	public RulesPageConfig(@Autowired String rulesSubpageFilePath, @Autowired ITextStorage storage) {
		super(rulesSubpageFilePath, storage);
	}
	

}
