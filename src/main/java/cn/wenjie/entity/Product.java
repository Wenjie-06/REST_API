package cn.wenjie.entity;

public class Product {
	
	/** product ID */
	private Long productID;
	
	/** product quantity */
	private Long quantity;
	
	/** product unit Price */
	private double unitPrice;
	
	public Long getProductID() {
		return productID;
	}
	public void setProductID(Long productID) {
		this.productID = productID;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
}
