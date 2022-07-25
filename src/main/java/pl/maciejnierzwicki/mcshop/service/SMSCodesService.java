package pl.maciejnierzwicki.mcshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;
import pl.maciejnierzwicki.mcshop.repository.SMSCodesRepository;

@Service
public class SMSCodesService {
	
	@Autowired
	private SMSCodesRepository smsCodesRepo;
	
	@Cacheable(value = "smsCodes")
	public SMSCode getById(Long id) {
		return smsCodesRepo.findById(id).orElse(null);
	}

	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> getAll() {
		return smsCodesRepo.findAll();
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findAllByProviderName(SMSProvider providerName) {
		return smsCodesRepo.findAllByProviderName(providerName);
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> getAllSMSCodesFromCheapest() {
		return smsCodesRepo.findByOrderByPriceNetAsc();
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findByProviderNameOrderByPriceNetAsc(SMSProvider providerName) {
		return smsCodesRepo.findByProviderNameOrderByPriceNetAsc(providerName);
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findAllByFundsToAdd(Double value) {
		return smsCodesRepo.findAllByFundsToAdd(value);
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findAllByProviderNameAndFundsToAdd(SMSProvider providerName, Double value) {
		return smsCodesRepo.findAllByProviderNameAndFundsToAdd(providerName, value);
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findAllByFundsToAddIsGreaterThanOrderByFundsToAddAsc(Double value) {
		return smsCodesRepo.findAllByFundsToAddIsGreaterThanOrderByFundsToAddAsc(value);
	}
	
	@Cacheable(value = "smsCodes")
	public Iterable<SMSCode> findAllByProviderNameAndFundsToAddIsGreaterThanOrderByFundsToAddAsc(SMSProvider providerName, Double value) {
		return smsCodesRepo.findAllByProviderNameAndFundsToAddIsGreaterThanOrderByFundsToAddAsc(providerName, value);
	}
	
	@CacheEvict(value = "smsCodes", allEntries = true)
	public SMSCode save(SMSCode code) {
		return smsCodesRepo.save(code);
	}
	
	@CacheEvict(value = "smsCodes", allEntries = true)
	public Iterable<SMSCode> saveAll(Iterable<SMSCode> smsCodes) {
		return smsCodesRepo.saveAll(smsCodes);
	};
	
	@CacheEvict(value = "smsCodes", allEntries = true)
	public void delete(SMSCode smsCode) {
		smsCodesRepo.delete(smsCode);
	}

}
