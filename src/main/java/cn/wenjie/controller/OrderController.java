package cn.wenjie.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wenjie.customExceptionResponse.CustomResponse;
import cn.wenjie.entity.Order;
import cn.wenjie.entity.OrderList;
import cn.wenjie.entity.OrderStatus;
import cn.wenjie.utils.Payment;
import cn.wenjie.utils.VerifyOrder;

//REST type of responses
@RestController
public class OrderController {

	/*
	 * *********************** POST: Create an order ************************
	 */

	/**
	 * Create an order. After received, first check and verify all the key items of the order,
	 * and then store the correct ones into the orderList 
	 * 
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/place/", method = RequestMethod.POST, consumes = "application/json")
	public Object place(@RequestBody String order){ 

		ObjectMapper mapper = new ObjectMapper(); 

		//first check the key items in the received order
		List<String> itemCheck = new VerifyOrder().verifyOrder(order);

		if  (!itemCheck.isEmpty()){

			//if items are missing, return bad request with a corresponding message
			CustomResponse response = new CustomResponse ("The following items are missing: "+ itemCheck);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.BAD_REQUEST);
		}else{
			try {
				//convert the String type into Order class
				Order recievedOrder = mapper.readValue(order, Order.class);

				//store the order in the orderList
				recievedOrder = OrderList.getInst().saveOrder(recievedOrder);

				//return the CREATED status code, as well as the required order info
				LinkedHashMap<String, String> successMsg = new LinkedHashMap<>();

				successMsg.put("orderID", String.valueOf(recievedOrder.getOrderID()));
				successMsg.put("date", (recievedOrder.getDate()));
				successMsg.put("total price", String.valueOf(recievedOrder.getTotalPrice()));

				return new ResponseEntity<Map<String, String>> (successMsg, HttpStatus.CREATED);

			} catch (IOException e) {
				e.printStackTrace();
			} 
		}

		CustomResponse response = new CustomResponse ("Failed to place the order, please check Welcome.html for further instructions");
		return new ResponseEntity<CustomResponse>(response,HttpStatus.BAD_REQUEST);
	}

	/*
	 * ***************** GET: Retrieve/display orders *************************************
	 */
	/**
	 * Display the orders stored in the OrderList. There are two ways of selecting orders: (1) by email + status, 
	 * (2)by email only. In both methods, the orders are displayed in a chronological order.
	 * 
	 * @param email
	 * @param stat 
	 * @return
	 */
	@RequestMapping(value = "/display/{email:.+}/{stat}", method = RequestMethod.GET)
	public Object display(@PathVariable String email, @PathVariable String stat){ 

		//first validate the received email, against the userList
		if (!OrderList.getInst().getUserList().contains(email)){

			CustomResponse response = new CustomResponse ("Provided email is not in the user list. Recieved email: "+ email);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.NOT_FOUND);
		}else{

			//filter will receive stat if stat is legal
			String filter ="";

			//check if the received stat is legal
			for (OrderStatus status: OrderStatus.values())
			{
				if (status.toString().equals(stat))
				{
					filter = stat;
					break;
				}
			}

			if (filter.equals(""))
			{
				//illegal stat, return corresponding message 
				CustomResponse response = new CustomResponse ("Provided status is not supported, "
						+ "allowed status are {ALL, OPEN, PAID, PAY_FAILED}.");
				return new ResponseEntity<CustomResponse>(response,HttpStatus.BAD_REQUEST);
			}else
			{
				//display the correct orders
				ArrayList<Order> display = OrderList.getInst().display(email, stat);
				return new ResponseEntity<ArrayList<Order>>(display, HttpStatus.OK);
			}

		}
	}

	//similar to above, only the stat is omitted, displaying all the orders from one user
	@RequestMapping(value = "/display/{email:.+}", method = RequestMethod.GET)
	public Object display(@PathVariable String email){ 

		// first verify the email against the userList
		if (!OrderList.getInst().getUserList().contains(email)){

			CustomResponse response = new CustomResponse ("Provided email is not in the user list. Recieved email: "+ email);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.NOT_FOUND);

		}else {
			
			//display all the orders from that user
			ArrayList<Order> display = OrderList.getInst().display(email);
			return new ResponseEntity<ArrayList<Order>>(display, HttpStatus.OK);
		}

	}

	/*
	 * ******************* PUT: Pay an order *********************
	 */
	/**
	 * Pay a placed order, by providing the order ID. The order ID is first checked against the order list, and then
	 * further proceed to the payment API
	 * 
	 * @param orderID
	 * @return
	 */
	@RequestMapping(value = "/pay/{orderID}", method = RequestMethod.PUT)
	public Object pay(@PathVariable Long orderID){ 
		
		//verify the received orderID, against the orderList
		if (!OrderList.getInst().getOrderIDList().contains(orderID)){
			
			CustomResponse response = new CustomResponse ("Provided order ID is not in the order list. Order Id: "+ orderID);
			return new ResponseEntity<CustomResponse>(response,HttpStatus.NOT_FOUND);
		}else {
			
			//get the corresponding order object from the orderList
			Order order = OrderList.getInst().getOrderbyID(orderID);
			
			//pay the order and return the HttpStasus code
			HttpStatus status = Payment.pay(order);

			//return the payment status of the order, as well as the HttpStatus code from the payment API
			LinkedHashMap<String, String> message = new LinkedHashMap<>();

			message.put("orderID", orderID.toString());
			message.put("Payment status", order.getStatus().toString());

			return new ResponseEntity<Map<String, String>> (message, status);
		}
	}


}
