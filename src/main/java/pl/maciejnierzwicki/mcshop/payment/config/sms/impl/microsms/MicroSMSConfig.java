package pl.maciejnierzwicki.mcshop.payment.config.sms.impl.microsms;

import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSConfig;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.SMSConfigForm;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.sms.impl.microsms.MicroSMSConfigForm;

/***
 * Extension of {@link SMSConfig} which adds <a href="https://microsms.pl/">MicroSMS</a> support.
 *
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class MicroSMSConfig extends SMSConfig {
	
	private int userId = 1;
	private int serviceId = 1;
	private IPropertiesStorage propertiesStorage;
	private String microSMSConfigFilePath;
	
	@Autowired
	public void init(String microSMSConfigFilePath, IPropertiesStorage props) {
		this.providerName = SMSProvider.MICROSMS;
		this.microSMSConfigFilePath = microSMSConfigFilePath;
		this.propertiesStorage = props;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(microSMSConfigFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public MicroSMSConfigForm toForm() {
		MicroSMSConfigForm form = new MicroSMSConfigForm();
		form.setUserId(userId);
		form.setServiceId(serviceId);
		return form;
	}

	@Override
	public void apply(SMSConfigForm form) {
		MicroSMSConfigForm microsms_form = (MicroSMSConfigForm) form;
		setUserId(microsms_form.getUserId());
		setServiceId(microsms_form.getServiceId());
	}
	
	public void apply(Properties properties) {
		this.userId = Integer.parseInt(properties.getProperty("userId"));
		this.serviceId = Integer.parseInt(properties.getProperty("serviceId"));
	}

	@Override
	public void saveToFile() {
		Properties props = new Properties();
		props.setProperty("userId", String.valueOf(this.userId));
		props.setProperty("serviceId", String.valueOf(this.serviceId));
		try {
			propertiesStorage.save(props, microSMSConfigFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void loadFromFile() {
		Properties props = propertiesStorage.load(microSMSConfigFilePath);
		apply(props);
		
	}

}
