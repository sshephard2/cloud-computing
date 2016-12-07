package uk.ac.ncl.smartcam;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;

public class NoSqlConsumer {

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

		while(true) {
			message = service.receiveDeleteMessage("AllMessages");
			
			if (message instanceof Registration) {
				System.out.println("Registration:" + message.toString());				
			} else if (message instanceof Sighting) {
				System.out.println("Sighting:" + message.toString());	
			} else {
				// Do nothing
			}
			
		}
		
	}

}
