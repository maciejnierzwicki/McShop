package pl.maciejnierzwicki.mcshop.dbentity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import pl.maciejnierzwicki.mcshop.orderdata.OrderStatus;
import pl.maciejnierzwicki.mcshop.orderdata.OrderType;
import pl.maciejnierzwicki.mcshop.orderdata.PaymentMethod;

@Entity
@Data
//@Entity(name = "Orders")
@Table(name = "Orders")
public class Order {
	
	public Order() {}
	public Order(OrderType orderType) {
		this.orderType = orderType;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	
	@Column(name = "order_type")
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.WAITING_PAYMENT;
	
	private Double finalPrice = 0.0;
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod = null;
	
	@OneToOne
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private Service service;
	
	@Column(name = "player_name")
	private String playerName;
	
	/** AMOUNT OF FUNDS TO ADD */
	private Double amount;

}
