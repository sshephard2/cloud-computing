package uk.ac.ncl.smartcam;

import java.util.LinkedList;
import java.util.Queue;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;
import uk.ac.ncl.smartcam.TableStorage;

public class NoSqlConsumer {
	
	private static final String AllMessages = "AllMessages";
	private static final long BatchLimit = 100L;
	private static final int SleepTime = 10000; // 10 seconds
	
	public static void main(String[] args) {
		
		// Connect to Azure Service Bus
		ServiceBus service;
		try {
			service = new ServiceBus();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		Object message;
		Long messageCount;
		Queue<Registration> registrations = new LinkedList<Registration>();
		Queue<Sighting> sightings = new LinkedList<Sighting>();

		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(AllMessages);
			System.out.println("Subscription " + AllMessages + ":" + messageCount + " messages");
			
			// Read in all the messages
			for (Long i=0L; i<messageCount && i<BatchLimit; i++) {
				message = service.receiveMessage(AllMessages);

				if (message instanceof Registration) {
					System.out.println("Registration:" + message.toString());
					
					// Insert into registrations table
					tableService.singleInsert("registrations", (Registration)message);
					
				} else if (message instanceof Sighting) {
					System.out.println("Sighting:" + message.toString());
					
					// Insert into sightings table
					tableService.singleInsert("sightings", (Sighting)message);
					
				} else {
					// Do nothing
				}
			}
			
			try {
				Thread.sleep(SleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				running = false;
			}
		}
		
	}

}
