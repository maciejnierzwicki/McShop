/**
 * 
 */
package pl.maciejnierzwicki.mcshop.dbentity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "EventMeta")
@Data
public class EventMeta {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "actor_id", referencedColumnName = "id")
	private User actor;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToOne
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private Service service;
	
	@OneToOne
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;
	
	@OneToOne
	@JoinColumn(name = "server_id", referencedColumnName = "id")
	private ServerConfig server;
	
	@OneToOne
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;
	
}
