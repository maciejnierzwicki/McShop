package pl.maciejnierzwicki.mcshop.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

/***
 * Utility class with methods to test connection with databases.
 * @author Maciej Nierzwicki
 *
 */
@Component
public class DatabaseUtil {
	
	/***
	 * Attempts connection to MySQL database with provided details and returns connection result as boolean.
	 * 
	 * @param address - database connection address
	 * @param dbname - database name
	 * @param port - database port
	 * @param username - database user name
	 * @param password - database user password
	 * 
	 * @return true if connection to database has occurred, false otherwise.
	 */
	public static boolean testMySQLConnection(String address, String dbname, int port, String username, String password) {
		Configuration config = new Configuration();
		config.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		config.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		config.setProperty("hibernate.connection.url", "jdbc:mysql://" + address + ":" + port + "/" + dbname);
		config.setProperty("hibernate.connection.username", username);
		config.setProperty("hibernate.connection.password", password);
		
		SessionFactory factory;
		try {
			factory = config.buildSessionFactory();
		}
		catch(Exception e) {
			return false;
		}
		Session session = factory.openSession();
		boolean result = session.isOpen();
		session.close();
		return result;
	}
}
