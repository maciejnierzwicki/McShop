package pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.dotpay;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferConfig;
import pl.maciejnierzwicki.mcshop.payment.config.banktransfer.BankTransferProvider;
import pl.maciejnierzwicki.mcshop.properties.storage.IPropertiesStorage;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.BankTransferConfigForm;
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.impl.dotpay.DotPayConfigForm;

/***
 * Extension of {@link BankTransferConfig} which adds <a href="https://www.dotpay.pl/">DotPay</a> support.<br>
 *
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class DotPayConfig extends BankTransferConfig {
	
	private int shopId;
	private final String targetUrl = "https://ssl.dotpay.pl/t2/";
	private List<String> allowedIPs = new ArrayList<>();
	private String dotPayConfigFilePath;
	
	private IPropertiesStorage propertiesStorage;
	
	@Autowired
	public void init(String dotPayConfigFilePath, IPropertiesStorage props) {
		this.propertiesStorage = props;
		this.providerName = BankTransferProvider.DOTPAY;
		this.dotPayConfigFilePath = dotPayConfigFilePath;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(dotPayConfigFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public DotPayConfigForm toForm() {
		DotPayConfigForm form = new DotPayConfigForm();
		form.setShopId(this.shopId);
		return form;
	}

	@Override
	public void apply(BankTransferConfigForm form) {
		DotPayConfigForm dotpay_form = (DotPayConfigForm) form;
		setShopId(dotpay_form.getShopId());
		
	}
	
	public void apply(Properties properties) {
		this.shopId = Integer.parseInt(properties.getProperty("shopId"));
		this.allowedIPs = Arrays.asList(properties.getProperty("allowedIPs").split(","));
	}

	@Override
	public void saveToFile() {
		Properties props = new Properties();
		props.setProperty("shopId", String.valueOf(this.shopId));
		props.setProperty("allowedIPs", StringUtils.join(this.allowedIPs, ','));
		try {
			propertiesStorage.save(props, dotPayConfigFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void loadFromFile() {
		Properties props = propertiesStorage.load(dotPayConfigFilePath);
		apply(props);
	}

}
