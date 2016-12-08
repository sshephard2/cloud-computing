package uk.ac.ncl.smartcam;

import java.util.LinkedList;
import java.util.Queue;

import com.microsoft.azure.storage.table.TableEntity;

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
		Queue<TableEntity> registrations = new LinkedList<TableEntity>();
		Queue<TableEntity> sightings = new LinkedList<TableEntity>();

		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(AllMessages);
			System.out.println("Subscription " + AllMessages + ":" + messageCount + " messages");
			
			// Clear the queues
			registrations.clear();
			sightings.clear();
			
			// Read in the messages (up to the batch limit)
			for (Long i=0L; i<messageCount && i<BatchLimit; i++) {
				message = service.receiveMessage(AllMessages);

				if (message instanceof Registration) {
					System.out.println("Registration:" + message.toString());
					
					// Insert into registrations queue
					registrations.add((Registration)message);			
					
				} else if (message instanceof Sighting) {
					System.out.println("Sighting:" + message.toString());
					
					// Insert into sightings queue
					sightings.add((Sighting)message);
					
				} else {
					// Do nothing
				}
			}
			
			// If there are any registrations queued, then insert them
			if (registrations.size() > 0 ) {
				tableService.batchInsert("registrations", registrations);
			}
			
			// If there are any sightings queued, then insert them
			if (sightings.size() > 0) {
				tableService.batchInsert("sightings", sightings);
			}
			
			try {
				Thread.sleep(SleepTime);
			} catch (InterruptedException e) {
				running = false;
			}
		}
		
	}

}
