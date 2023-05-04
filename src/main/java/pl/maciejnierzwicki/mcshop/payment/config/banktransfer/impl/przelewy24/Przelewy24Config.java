package pl.maciejnierzwicki.mcshop.payment.config.banktransfer.impl.przelewy24;

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
import pl.maciejnierzwicki.mcshop.web.form.admin.paymentconfiguration.banktransfer.impl.przelewy24.Przelewy24ConfigForm;

/***
 * Extension of {@link BankTransferConfig} which adds <a href="https://www.przelewy24.pl/">Przelewy24</a> support.<br>
 *
 */
@Data
@Component
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class Przelewy24Config extends BankTransferConfig {
	
	private int shopId;
	private String crc;
	private final String targetUrl = "https://sklep.przelewy24.pl/zakup.php";
	private List<String> allowedIPs = new ArrayList<>();
	private String przelewy24ConfigFilePath;
	
	private IPropertiesStorage propertiesStorage;
	
	@Autowired
	public void init(String przelewy24ConfigFilePath, IPropertiesStorage props) {
		this.propertiesStorage = props;
		this.providerName = BankTransferProvider.DOTPAY;
		this.przelewy24ConfigFilePath = przelewy24ConfigFilePath;
		try {
			propertiesStorage.copyFileFromJarIfNotExists(przelewy24ConfigFilePath);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Przelewy24ConfigForm toForm() {
		Przelewy24ConfigForm form = new Przelewy24ConfigForm();
		form.setShopId(this.shopId);
		form.setCrc(this.crc);
		return form;
	}

	@Override
	public void apply(BankTransferConfigForm form) {
		Przelewy24ConfigForm przelewy24_form = (Przelewy24ConfigForm) form;
		setShopId(przelewy24_form.getShopId());
		setCrc(przelewy24_form.getCrc());
	}
	
	public void apply(Properties properties) {
		this.shopId = Integer.parseInt(properties.getProperty("shopId"));
		this.allowedIPs = Arrays.asList(properties.getProperty("allowedIPs").split(","));
		this.crc = properties.getProperty("crc");
	}

	@Override
	public void saveToFile() {
		Properties props = new Properties();
		props.setProperty("crc", this.crc);
		props.setProperty("shopId", String.valueOf(this.shopId));
		props.setProperty("allowedIPs", StringUtils.join(this.allowedIPs, ','));
		try {
			propertiesStorage.save(props, przelewy24ConfigFilePath);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void loadFromFile() {
		Properties props = propertiesStorage.load(przelewy24ConfigFilePath);
		apply(props);
	}

}
