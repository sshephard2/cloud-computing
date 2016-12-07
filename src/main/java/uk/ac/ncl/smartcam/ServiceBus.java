package uk.ac.ncl.smartcam;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;

public class ServiceBus {
	
	private ServiceBusContract service;
	
	// Internal queue to store messages if offline
	private Queue<BrokeredMessage> msgQueue;

	/**
	 * Microsoft Azure Service Bus
	 * @throws Exception 
	 */
	public ServiceBus() throws Exception {
		
        // Retrieve the connection string
        Properties prop = new Properties();
        try {
            InputStream propertyStream = ServiceBus.class.getClassLoader().getResourceAsStream("config.properties");
            if (propertyStream != null) {
                prop.load(propertyStream);
            }
            else {
                throw new RuntimeException();
            }
        } catch (RuntimeException|IOException e) {
            System.out.println("\nFailed to load config.properties file.");
            throw e;
        }

        String namespace = prop.getProperty("ServiceBusNamespace");
        String key = prop.getProperty("ServiceBusKey");
            
		Configuration config =
			    ServiceBusConfiguration.configureWithSASAuthentication(
			      namespace, "RootManageSharedAccessKey", key, ".servicebus.windows.net"
			      );

		this.service = ServiceBusService.create(config);
		this.msgQueue = new LinkedList<BrokeredMessage>();
	}
	
	/**
	 * Send a message object (serialised to a JSON string) to the topic
	 * @param msgObject
	 * @param msgType
	 * @param speeding
	 */
	public void sendMessage(Object msgObject, String msgType, boolean speeding) {
		BrokeredMessage message;

		boolean checkQueue = true;
		
		// If there are any queued messages, attempt to send them first
		while (checkQueue && msgQueue.size() > 0) {
			
			// Read first element in queue but don't remove yet
			message = msgQueue.peek();
			
			// Attempt to send message to the topic
			try {
				System.out.println("Sending queued:" + message.getBody());
				service.sendTopicMessage("cameratopic", message);
				
				// If queue element was successfully sent, remove it
				message = msgQueue.remove();
				
			} catch (ServiceException e) {
				// If message can't be sent, then stop reading the queue, drop out to the main code
				// so that the new message to be sent is queued
				checkQueue = false;
			}
		}
		
		try {
			// Serialize to JSON
			ObjectMapper mapper = new ObjectMapper();
			String jsonMessage = mapper.writeValueAsString(msgObject);
			System.out.println("Sending<" + msgType + ">:" + jsonMessage);
			
			// Create message
			message = new BrokeredMessage(jsonMessage);
			
			// Set message type
			message.setProperty("messagetype", msgType);
			
			// If message type is "Sighting", set speeding flag
			if (msgType.equals("Sighting")) {
				message.setProperty("speeding", speeding);
			}
			
			// Send message to the topic
			try {
				service.sendTopicMessage("cameratopic", message);
			} catch (ServiceException e) {
				// If message can't be sent, then queue it
				System.out.println("Queueing:" + message.getBody());
				msgQueue.add(message);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Receive and delete a message from a subscription
	 * @param subscription
	 * @return
	 */
	public Object receiveDeleteMessage(String subscription) {
		Object msgObject = null;

		try
		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveAndDelete();

			ReceiveSubscriptionMessageResult resultSubMsg =
					service.receiveSubscriptionMessage("cameratopic", subscription, opts);
			BrokeredMessage message = resultSubMsg.getValue();
			if (message != null && message.getMessageId() != null)
			{
				System.out.println("MessageID: " + message.getMessageId());
				// Display the topic message.
				System.out.print("From topic: ");
				byte[] b = new byte[200];
				String s = null;
				int numRead = message.getBody().read(b);
				while (-1 != numRead)
				{
					s = new String(b);
					s = s.trim();
					System.out.print(s);
					numRead = message.getBody().read(b);
				}
				System.out.println();
				System.out.println("Custom Property: " +
						message.getProperty("messagetype"));
				msgObject = message.getBody();
			}  
			else  
			{
				System.out.println("No message");
			}
		}
		catch (ServiceException e) {
			System.out.print("ServiceException encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		catch (Exception e) {
			System.out.print("Generic exception encountered: ");
			System.out.println(e.getMessage());
			System.exit(-1);
		}

		return msgObject;
	}
}
