package pl.maciejnierzwicki.mcshop.payment.validation.impl.microsms;

import lombok.Data;

@Data
public class MicroSMSResponseData {
	
	private int status;
	private String used;
	private int service;
	private int number;
	private String phone;
	private String reply;
	private String errorCode;
	private String message;

}
