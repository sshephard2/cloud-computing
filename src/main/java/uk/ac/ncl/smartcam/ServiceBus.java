package uk.ac.ncl.smartcam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;

public class ServiceBus {
	
	private ServiceBusContract service;

	/**
	 * Microsoft Azure Service Bus
	 * @param namespace
	 * @param key
	 */
	public ServiceBus(String namespace, String key) {
		
		Configuration config =
			    ServiceBusConfiguration.configureWithSASAuthentication(
			      namespace, "RootManageSharedAccessKey", key, ".servicebus.windows.net"
			      );

		this.service = ServiceBusService.create(config);
	}
	
	public void sendMessage(Object msgObject) {
		try {
			// Serialize to JSON
			ObjectMapper mapper = new ObjectMapper();
			String jsonMessage = mapper.writeValueAsString(msgObject);
			System.out.println("Sending:" + jsonMessage);
			
			// Create message
			BrokeredMessage message = new BrokeredMessage(jsonMessage);
			
			// Set message type to Registration
			message.setProperty("messagetype", "Registration");
			
			// Send message to the topic
			try {
				service.sendTopicMessage("cameratopic", message);
			} catch (ServiceException e) {
				// do nothing
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

}
