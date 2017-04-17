package cn.wenjie.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.wenjie.customExceptionResponse.CustomResponse;

public class VerifyOrder {

	/**
	 *  verify the received order request, by checking all the required key items. 
	 *	return the item names that are missing.
	 *
	 * @param order
	 * @return
	 */
	public List<String> verifyOrder(String order){

		// list of all the required items (key words)
		List<String> userItems = new ArrayList(Arrays.asList("email","currency","postCode", "countryCode","products"));

		ObjectMapper mapper = new ObjectMapper(); 
		
		try {
			//convert the received String to Map 
			Map mapOrder = mapper.readValue(order, Map.class);
			
			//loop over the userItems and remove those found in the received order
			Iterator<String> userIterator = userItems.iterator();  
			while(userIterator.hasNext()){  
				String st = userIterator.next(); 
				if(mapOrder.keySet().contains(st)){  
					userIterator.remove();  
				}  
			}  
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//return the remaining userItems: if userItem is empty, then all the items are included in the received order 
		return userItems;
	}
}
