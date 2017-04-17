package cn.wenjie.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class OrderList {
	
	/** single instance of the OrderList object, ensure a single orderList on the server*/
	private static final OrderList singletonInstance = new OrderList();
	
	/** unique locker for the synchronize control of multi-thread run */
	private static Object lock = new Object();

	/** order list for storing all the corrected saved orders */
	private HashMap<Long, Order> orderList = new HashMap<>();
	
	/** user list,i.e. email list, generated when saving the orders to order list */
	private HashSet<String> userList = new HashSet<>();

	/** unique order ID of a saved order */
	private Long orderID;

	
	
	private OrderList(){
		orderID = (long) 0;
	}
	
	public static OrderList getInst(){
		return singletonInstance;
	}
	
	public HashSet<String> getUserList() {
		return userList;
	}
	
	//get the collection of saved order ID
	public Collection<Long> getOrderIDList(){
		return orderList.keySet();
	} 

	
	/**
	 * return a corresponding order by an order ID
	 * 
	 * @param orderID
	 * @return
	 */
	public Order getOrderbyID(Long orderID){
		return orderList.get(orderID);
	}

	
	/**
	 * save an order to the orderList, adding date(time), a newly generated order ID, 
	 * status of OPEN and also add the user email to the userList collection
	 * 	
	 * @param order
	 * @return
	 */
	public Order saveOrder(Order order){
		
		Date date = new Date();
		order.setDate(date);
		order.setStatus(OrderStatus.OPEN);
		
		//ensure the unique orderID
		synchronized(lock){
			orderID ++;
		}
	
		order.setOrderID(orderID);
		orderList.put(orderID, order);
		userList.add(order.getEmail());
		
		return order;
	}
	
	
	/**
	 * Select and return a list of orders by a given email, the list
	 * is sorted in a chronological order.
	 * 
	 * @param email
	 * @return
	 */
	public ArrayList<Order> display(String email){

		ArrayList<Order> displayList = new ArrayList<>();
		
		//search inside the orderList, find one with the given email
		for (Map.Entry<Long, Order> entry: orderList.entrySet()){

			if(entry.getValue().getEmail().equals(email)) {

				displayList.add(entry.getValue());
			};

		}
		
		//sort date order
		Collections.sort(displayList, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return o1.dateToSec().compareTo(o2.dateToSec());
			}
		});

		return displayList;
	}
	
	
	/**
	 * Select and return a list of orders by a given email and order status, the list
	 * is sorted in a chronological order.
	 * 
	 * @param email
	 * @param stat 
	 * @return 
	 */
	public ArrayList<Order> display(String email, String stat){

		ArrayList<Order> displayList = new ArrayList<>();

		for (Map.Entry<Long, Order> entry: orderList.entrySet()){
			
			//search inside the orderList, find one with the given email and status
			if(entry.getValue().getEmail().equals(email) &&
					entry.getValue().getStatus().equals( OrderStatus.valueOf(stat))) {

				displayList.add(entry.getValue());
			};

		}
		
		//sort the date order
		Collections.sort(displayList, new Comparator<Order>() {
			public int compare(Order o1, Order o2) {
				return o1.dateToSec().compareTo(o2.dateToSec());
			}
		});

		return displayList;
	}
	
}

