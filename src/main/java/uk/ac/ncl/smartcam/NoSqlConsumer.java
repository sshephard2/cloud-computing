package uk.ac.ncl.smartcam;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;

public class NoSqlConsumer {
	
	private static final String AllMessages = "AllMessages";

	public static void main(String[] args) {
		
		// Connect to Azure Service Bus
		ServiceBus service;
		try {
			service = new ServiceBus();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		
		Object message;
		Long messageCount;

		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(AllMessages);
			System.out.println("Subscription " + AllMessages + ":" + messageCount + " messages");
			
			// Read in all the messages
			for (Long i=0L; i<messageCount; i++) {
				message = service.receiveMessage(AllMessages);

				if (message instanceof Registration) {
					System.out.println("Registration:" + message.toString());				
				} else if (message instanceof Sighting) {
					System.out.println("Sighting:" + message.toString());	
				} else {
					// Do nothing
				}
			}
			
			try {
				Thread.sleep(10000);  // Sleep for 10s
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
		}
		
	}

}
