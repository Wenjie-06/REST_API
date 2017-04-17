package cn.wenjie.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	
	/** ID of an order*/
	private Long orderID;
	
	/** date and time when an order is saved to the orderList */
	private Date date;
	
	/** total price of an order, auto created when getTotalPrice() is called */
	private Double totalPrice;
	
	/** order status: OPEN/PAID/PAY_FAILED */
	private OrderStatus status;
	
	/** currency */
	private String currency;
	
	/** Email: user ID */
	private String email;
	
	/** post code: shipping info */
	private String postCode;
	
	/** country code: shipping info */
	private String countryCode;
	
	/** product list, contains the product id, quantity and unit price */
	private List<Product> products = new ArrayList<Product>();
	
	
		
	public Long getOrderID() {
		return orderID;
	}
	public void setOrderID(Long orderID) {
		this.orderID = orderID;
	}
	public String getDate() {
		return date.toString();
	}
	public Date dateToSec() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public Double getTotalPrice(){
		
		Double totalPrice = (double) 0;
		
		for (Product product: products){
			totalPrice += product.getUnitPrice() * product.getQuantity();
		}
		this.totalPrice = totalPrice;
		return totalPrice;
	}


}
