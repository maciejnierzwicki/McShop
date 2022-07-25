package pl.maciejnierzwicki.mcshop.event;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.dbentity.AppEvent;
import pl.maciejnierzwicki.mcshop.dbentity.EventMeta;
import pl.maciejnierzwicki.mcshop.properties.MainProperties;
import pl.maciejnierzwicki.mcshop.service.EventMetaService;
import pl.maciejnierzwicki.mcshop.service.EventService;

@Component
@Slf4j
public class McShopEventListener {
	
	@Autowired
	private MainProperties properties;
	@Autowired
	private EventService eventService;
	@Autowired
	private EventMetaService eventMetaService;

	@EventListener
	public void onApplicationEvent(AppEvent event) {
		SimpleDateFormat sdf = new SimpleDateFormat(properties.getDateFormat());
		String date = sdf.format(event.getDate());
		log.debug("[" + date + "] EVENT: " + event.getType());
		
		EventMeta meta = event.getMeta();
		eventMetaService.save(meta);
		eventService.save(event);
		log.debug("Saved event data to db.");
		
	}

}
