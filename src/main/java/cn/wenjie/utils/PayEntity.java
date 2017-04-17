package cn.wenjie.utils;

/*
 * Defines the structure of the request to be sent to the payment API 
 */

public class PayEntity {
	
	/** payment currency */
	private String currency;
	
	/** amount, i.e. the totalPrice in an order */
	private Double amount;
			
	
	
	public PayEntity(String currency, Double amount) {
		super();
		this.currency = currency;
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}