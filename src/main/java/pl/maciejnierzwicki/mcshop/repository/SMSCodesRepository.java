package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.payment.config.sms.SMSProvider;

@Repository
public interface SMSCodesRepository extends CrudRepository<SMSCode, Long> {
	
	// by id
	SMSCode getById(Long id);
	
	// all
	Iterable<SMSCode> findAll();
	
	// all by provider name
	Iterable<SMSCode> findAllByProviderName(SMSProvider providerName);
	
	// by net price ascending
	Iterable<SMSCode> findByOrderByPriceNetAsc();
	
	// by provider name, order by net price ascending
	Iterable<SMSCode> findByProviderNameOrderByPriceNetAsc(SMSProvider providerName);
	
	// by funds to add
	Iterable<SMSCode> findAllByFundsToAdd(Double value);
	
	// by provider name and funds to add
	Iterable<SMSCode> findAllByProviderNameAndFundsToAdd(SMSProvider providerName, Double value);
	
	// by funds to add greater than given parameter, order by 'funds to add' ascending
	Iterable<SMSCode> findAllByFundsToAddIsGreaterThanOrderByFundsToAddAsc(Double value);
	
	// by provider name and with 'funds to add' greater than given parameter, order by 'funds to add' ascending
	Iterable<SMSCode> findAllByProviderNameAndFundsToAddIsGreaterThanOrderByFundsToAddAsc(SMSProvider providerName, Double value);
	
	// save
	<S extends SMSCode> S save(S entity);
	
	// save all
	<S extends SMSCode> Iterable<S> saveAll(Iterable<S> entities);
	
	// delete
	void delete(SMSCode smsCode);
}