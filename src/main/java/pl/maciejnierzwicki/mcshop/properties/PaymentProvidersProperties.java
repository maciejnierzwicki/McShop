package pl.maciejnierzwicki.mcshop.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferProvider;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.PaymentProvidersPropertiesForm;

@Component
@Data
@Slf4j
public class PaymentProvidersProperties {
	
	private BankTransferProvider bankTransferProvider = null;
	private SMSProvider smsProvider = null;
	private String paymentProvidersFilePath;
	private String paymentProvidersDirPath;
	
	private IPropertiesStorage propertiesStorage;
	
	@Autowired
	public PaymentProvidersProperties(String paymentProvidersFilePath, String paymentProvidersDirPath, IPropertiesStorage propertiesStorage) {
		this.paymentProvidersFilePath = paymentProvidersFilePath;
		this.paymentProvidersDirPath = paymentProvidersDirPath;
		this.propertiesStorage = propertiesStorage;
		
		try {
			String full_dir_path = new File(".").getAbsolutePath() + "/" + paymentProvidersDirPath;
			File dir = new File(full_dir_path);
			if(!dir.exists()) { dir.mkdirs(); }
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		
		try {
			propertiesStorage.copyFileFromJarIfNotExists(paymentProvidersFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		Properties props = propertiesStorage.load(paymentProvidersFilePath);
		if(props != null) {
			apply(props);
		}
	}
	
	public void apply(PaymentProvidersPropertiesForm form) {
		this.bankTransferProvider = form.getBankTransferProvider();
		this.smsProvider = form.getSmsProvider();
	}
	
	public void apply(Properties properties) {
		this.bankTransferProvider = EnumUtils.getEnum(BankTransferProvider.class, properties.getProperty("banktransfer").toUpperCase());
		this.smsProvider = EnumUtils.getEnum(SMSProvider.class, properties.getProperty("sms").toUpperCase());
	}
	
	public PaymentProvidersPropertiesForm toForm() {
		PaymentProvidersPropertiesForm form = new PaymentProvidersPropertiesForm();
		form.setBankTransferProvider(getBankTransferProvider());
		form.setSmsProvider(getSmsProvider());
		return form;
	}
	
	public void save() {
		Properties props = new Properties();
		props.setProperty("banktransfer", String.valueOf(getBankTransferProvider()));
		props.setProperty("sms", String.valueOf(getSmsProvider()));
		try {
			propertiesStorage.save(props, paymentProvidersFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	
}
