package cn.wenjie.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import cn.wenjie.entity.Order;
import cn.wenjie.entity.OrderStatus;

public class Payment {

	
	/**
	 * Pay for a given order object 
	 * 
	 * @param order
	 * @return
	 */
	public static HttpStatus pay(Order order) {
		
		System.out.println("Paying for the order ID: " + order.getOrderID());
		
		//URL of the payment API
		final String paymentURL = "https://payment-mock.us-east.stage.cf.yaas.io/hybris/payments";
		
		//entity to be submitted for the payment, includes: "currency" and "amount"(totalPrice)
		PayEntity payEntity = new PayEntity(order.getCurrency(), order.getTotalPrice());
		
		//REST template for sending request
		RestTemplate restTemp = new RestTemplate();
		
		try{
			//sending payment request with the predefined payEntity
			ResponseEntity<String> response = restTemp.postForEntity(paymentURL, payEntity, String.class);
			
			//received HttpStatus code
			HttpStatus status = response.getStatusCode();
			
			//Update the order status according to the received HttpStatus 
			if (status == HttpStatus.CREATED){
				order.setStatus(OrderStatus.PAID);
			}else{
				order.setStatus(OrderStatus.PAY_FAILED);
			}
			
			return status;

		} catch (HttpStatusCodeException e) {
			e.printStackTrace();
			
			//If the payment received an exception, then capture the HttpStatus code, update the Order status
			//and return the HttpStatus code
			
			if (e.getStatusCode() == HttpStatus.CREATED){
				order.setStatus(OrderStatus.PAID);
			}else{
				order.setStatus(OrderStatus.PAY_FAILED);
			}
			return e.getStatusCode();
			
		}
	}


}
