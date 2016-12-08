package uk.ac.ncl.smartcam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.microsoft.azure.storage.table.TableEntity;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;
import uk.ac.ncl.smartcam.TableStorage;

public class NoSqlConsumer {
	
	private static final String AllMessages = "AllMessages";
	private static final long BatchLimit = 100L;
	private static final int MaxPollTime = 60; // maximum time between polls in seconds
	
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
		
		// Create HashMaps of Queues for Registrations and Sightings
		// This allows a queue to be created for every value of partition key
		// and ensures that when we use batch insert, we send the queue for each key
		// i.e. all the entities in the batch have the same partition key
		Map<String, Queue<TableEntity>> registrations = new HashMap<String, Queue<TableEntity>>();
		Map<String, Queue<TableEntity>> sightings = new HashMap<String, Queue<TableEntity>>();

		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(AllMessages);
			System.out.println("Subscription " + AllMessages + ":" + messageCount + " messages");
			
			// Clear the maps
			registrations.clear();
			sightings.clear();
			
			// Read in the messages (up to the batch limit)
			for (Long i=0L; i<messageCount && i<BatchLimit; i++) {
				message = service.receiveMessage(AllMessages);

				if (message instanceof Registration) {
					System.out.println("Registration:" + message.toString());
					
					// Insert into registrations map of queues
					Registration reg = (Registration)message;
					String partitionKey = reg.getPartitionKey();
					Queue<TableEntity> queue = registrations.get(partitionKey);
					// If there isn't already a queue for this partition key, create one
					if (queue == null) {
						queue = new LinkedList<TableEntity>();
						registrations.put(partitionKey, queue);
					}		
					queue.add((Registration)message);			
					
				} else if (message instanceof Sighting) {
					System.out.println("Sighting:" + message.toString());
					
					// Insert into sightings map of queues
					Sighting sight = (Sighting)message;
					String partitionKey = sight.getPartitionKey();
					// If there isn't already a queue for this partition key, create one
					Queue<TableEntity> queue = sightings.get(partitionKey);
					if (queue == null) {
						queue = new LinkedList<TableEntity>();
						sightings.put(partitionKey, queue);
					}
					queue.add((Sighting)message);
					
				} else {
					// Do nothing
				}
			}
			
			// Partition keys and records in each?
			for (String key: registrations.keySet()) {
				System.out.println("Registration key " + key + "," + registrations.get(key).size() + " values");
			}
			
			// Partition keys and records in each?
			for (String key: sightings.keySet()) {
				System.out.println("Sighting key " + key + "," + sightings.get(key).size() + " values");
			}
			
			// If there are any registrations queued, then insert them
			for (Queue<TableEntity> queue: registrations.values()) {
				tableService.batchInsert("registrations", queue);
			}
						
			// If there are any sightings queued, then insert them
			for (Queue<TableEntity> queue: sightings.values()) {
				tableService.batchInsert("sightings", queue);
			}
			
			try {
				// Simple formula that determines the polling time based on how many messages we last received
				// If we had a full maximum batch, then polling time is zero
				// If we had no messages, we wait the maximum polling time
				Thread.sleep(MaxPollTime*10*Math.max(0, BatchLimit-messageCount));
			} catch (InterruptedException e) {
				running = false;
			}
		}
		
	}

}
