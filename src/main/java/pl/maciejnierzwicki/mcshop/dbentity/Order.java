package pl.maciejnierzwicki.mcshop.dbentity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;

@Data
@Entity(name = "Orders")
@Table(name = "Orders")
public class Order {
	
	public Order() {}
	public Order(OrderType orderType) {
		this.orderType = orderType;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "order_type")
	private OrderType orderType;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	private OrderStatus status = OrderStatus.WAITING_PAYMENT;
	
	private Double finalPrice = 0.0;
	
	private PaymentMethod paymentMethod = null;
	
	@OneToOne
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private Service service;
	
	@Column(name = "player_name")
	private String playerName;
	
	/* AMOUNT OF FUNDS TO ADD */
	private Double amount;

}
