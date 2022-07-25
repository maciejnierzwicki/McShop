package pl.maciejnierzwicki.mcshop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.maciejnierzwicki.mcshop.dbentity.Category;
import pl.maciejnierzwicki.mcshop.dbentity.SMSCode;
import pl.maciejnierzwicki.mcshop.dbentity.ServerConfig;
import pl.maciejnierzwicki.mcshop.dbentity.Service;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long> {
	Service getById(Long id);
	Iterable<Service> findAllByCategory(Category category);
	Iterable<Service> findAllByCategoryAndEnabledIsTrue(Category category);
	Iterable<Service> findAllBySmsCode(SMSCode smsCode);
	Iterable<Service> findAllByServer(ServerConfig server);
	Iterable<Service> findAllByServerOrderByPositionAsc(ServerConfig server);
	Iterable<Service> findAllByServerAndEnabledIsTrueOrderByPositionAsc(ServerConfig server);
	Iterable<Service> findAllByServerAndCategory(ServerConfig server, Category category);
	Iterable<Service> findAllByServerAndCategoryOrderByPositionAsc(ServerConfig server, Category category);
	Iterable<Service> findAllByServerAndCategoryAndEnabledIsTrueOrderByPositionAsc(ServerConfig server, Category category);
	Iterable<Service> findByOrderByPositionAsc();

}
