package pl.maciejnierzwicki.mcshop.config.filespath;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilesPathConfig {
	
	@Value("${files.main-properties-file-path}")
	private String mainPropertiesFilePath;
	
	@Value("${files.database-file-path}")
	private String databaseFilePath;
	
	@Value("${files.payment-providers-properties-file-path}")
	private String paymentProvidersFilePath;
	
	@Value("${files.payment-providers-dir-path}")
	private String paymentProvidersDirPath;
	
	@Value("${files.banktransfer-providers-dir-path}")
	private String paymentProvidersBankTransferDirPath;
	
	@Value("${files.sms-providers-dir-path}")
	private String paymentProvidersSMSDirPath;
	
	@Value("${files.subpages-properties-file-path}")
	private String subpagesConfigFilePath;

	@Value("${files.templates-dir-path}")
	private String templatesDirPath;
	
	@Value("${files.setup-file-path}")
	private String setupFilePath;
	
	@Value("${files.rules-file-path}")
	private String rulesSubpageFilePath;
	
	@Value("${files.contact-file-path}")
	private String contactSubpageFilePath;
	
	@Value("${files.about-file-path}")
	private String aboutSubpageFilePath;
	
	@Value("${files.dotpay-banktransfer-file-path}")
	private String dotPayConfigFilePath;
	
	@Value("${files.przelewy24-banktransfer-file-path}")
	private String przelewy24ConfigFilePath;
	
	@Value("${files.microsms-sms-file-path}")
	private String microSMSConfigFilePath;
	
	@Bean
	public String mainPropertiesFilePath() {
		return mainPropertiesFilePath;
	}
	
	@Bean
	public String databaseFilePath() {
		return databaseFilePath;
	}
	
	@Bean
	public String paymentProvidersFilePath() {
		return paymentProvidersFilePath;
	}
	
	@Bean
	public String paymentProvidersDirPath() {
		return paymentProvidersDirPath;
	}
	
	@Bean
	public String paymentProvidersBankTransferDirPath() {
		return paymentProvidersBankTransferDirPath;
	}
	
	@Bean
	public String paymentProvidersSMSDirPath() {
		return paymentProvidersSMSDirPath;
	}
	
	@Bean
	public String subpagesConfigFilePath() {
		return subpagesConfigFilePath;
	}
	
	@Bean
	public String templatesDirPath() {
		return templatesDirPath;
	}
	
	@Bean
	public String setupFilePath() {
		return setupFilePath;
	}

	@Bean
	public String rulesSubpageFilePath() {
		return rulesSubpageFilePath;
	}
	
	@Bean
	public String contactSubpageFilePath() {
		return contactSubpageFilePath;
	}
	
	@Bean
	public String aboutSubpageFilePath() {
		return aboutSubpageFilePath;
	}
	
	@Bean
	public String dotPayConfigFilePath() {
		return dotPayConfigFilePath;
	}
	
	@Bean
	public String przelewy24ConfigFilePath() {
		return przelewy24ConfigFilePath;
	}
	
	@Bean
	public String microSMSConfigFilePath() {
		return microSMSConfigFilePath;
	}
}
