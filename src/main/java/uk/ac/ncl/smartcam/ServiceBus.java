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
import com.microsoft.windowsazure.services.servicebus.models.GetSubscriptionResult;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveMessageOptions;
import com.microsoft.windowsazure.services.servicebus.models.ReceiveSubscriptionMessageResult;
import com.microsoft.windowsazure.services.servicebus.models.SubscriptionInfo;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;

public class ServiceBus {
	
	private ServiceBusContract service;
	private Queue<BrokeredMessage> msgQueue; // Internal queue to store messages if offline
	private String topic;
	private int receiveTimeout;

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
        this.topic = prop.getProperty("Topic");
        this.receiveTimeout = Integer.parseInt(prop.getProperty("ReceiveTimeout"));
            
		Configuration config =
			    ServiceBusConfiguration.configureWithSASAuthentication(
			      namespace, "RootManageSharedAccessKey", key, ".servicebus.windows.net"
			      );

		this.service = ServiceBusService.create(config);
		this.msgQueue = new LinkedList<BrokeredMessage>();
	}
	
	/**
	 * Send a message object (serialised to a JSON string) to the topic
	 * @param msgObject the message as an object
	 * @param msgType the type of message (Registration|Sighting)
	 * @param speeding a flag to say whether or not this Sighting is a speeding vehicle
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
				service.sendTopicMessage(topic, message);
				
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
				service.sendTopicMessage(topic, message);
			} catch (ServiceException e) {
				// If message can't be sent, then queue it
				System.out.println("Queueing:" + message.getBody());
				msgQueue.add(message);
			}
		} catch (JsonProcessingException e) {
			// e.printStackTrace();
		}
	}
	
	/**
	 * Count the number of messages on a subscription
	 * @param subscription name of subscription
	 * @return the number of messages or null in the event of error
	 */
	public Long messageCount(String subscription) {
		Long count = null;
		try {
			SubscriptionInfo s = service.getSubscription(topic, subscription).getValue();
			count = s.getMessageCount();
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
		return count;
	}
	
	/**
	 * Receive a message from a subscription
	 * @param subscription name of subscription
	 * @return the message as an object or null if no valid message is received
	 */
	public Object receiveMessage(String subscription) {
		Object msgObject = null;
		String msgType;
		ObjectMapper mapper = new ObjectMapper();
		
		ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
		opts.setPeekLock(); // Peeklock does not automatically delete messages
		opts.setTimeout(receiveTimeout); // Set timeout

		try
		{
			// Receive a message from the selected subscription
			ReceiveSubscriptionMessageResult resultSubMsg =
					service.receiveSubscriptionMessage(topic, subscription, opts);
			BrokeredMessage message = resultSubMsg.getValue();
			if (message != null && message.getMessageId() != null)
			{
				// Delete message now, once safely received
				// But if it is an invalid message, it will not block the susbcription
				service.deleteMessage(message);
				
				// Get the message type
				msgType = (String)message.getProperty("messagetype");				
				if (msgType.equals("Sighting")) {
					msgObject = mapper.readValue(message.getBody(), Sighting.class);
				} else if (msgType.equals("Registration")) {
					msgObject = mapper.readValue(message.getBody(), Registration.class);
				}				

			}  
			else
			{
				// Will return null msgObject
			}
		}
		catch (ServiceException e) {
			System.out.print("ServiceException encountered: ");
			System.out.println(e.getMessage());
			// Will return null msgObject
		}
		catch (Exception e) {
			System.out.print("Generic exception encountered: ");
			System.out.println(e.getMessage());
			// Will return null msgObject
		}

		return msgObject;
	}
}
