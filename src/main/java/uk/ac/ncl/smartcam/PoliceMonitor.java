package uk.ac.ncl.smartcam;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.microsoft.azure.storage.table.TableEntity;

import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.SpeedingVehicle;
import uk.ac.ncl.smartcam.ServiceBus;
import uk.ac.ncl.smartcam.TableStorage;

public class PoliceMonitor {
	
	private static final String Speeding = "Speeding";
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
		
		// Create HashMaps of Queues for SpeedingVehicles
		// This allows a queue to be created for every value of partition key
		// and ensures that when we use batch insert, we send the queue for each key
		// i.e. all the entities in the batch have the same partition key
		Map<String, Queue<TableEntity>> speedingVehicles = new HashMap<String, Queue<TableEntity>>();

		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(Speeding);
			System.out.println("Subscription " + Speeding + ":" + messageCount + " messages");
			
			// Clear the maps
			speedingVehicles.clear();
			
			// Read in the messages (up to the batch limit)
			for (Long i=0L; i<messageCount && i<BatchLimit; i++) {
				message = service.receiveMessage(Speeding);

				if (message instanceof Sighting) {
					System.out.println("Sighting:" + message.toString());
					
					// Convert Sighting message to SpeedingVehicle entity
					Sighting sight = (Sighting)message;
					String id = sight.getId();
					Date timestamp = sight.getTimestamp();
					String registration = sight.getRegistration();
					String vehicletype = sight.getVehicletype();
					int speed = sight.getSpeed();
					int speedlimit = sight.getSpeedlimit();
					String priority = "";
					
					// Is the vehicle speed more than 10% over the speedlimit?
					// Using integer arithmetic, if 11*speed>10*speedlimit
					// Then speed is greater than speedlimit*1.1
					if (10*speed > 11*speedlimit) {
						priority = "PRIORITY";
						System.out.println(priority);
					}
					
					SpeedingVehicle speeding = new SpeedingVehicle(id, registration, vehicletype, speed, speedlimit, priority);
					speeding.setTimestamp(timestamp); // Overwrite with the timestamp we received				

					// Insert into sightings map of queues
					String partitionKey = speeding.getPartitionKey();
					// If there isn't already a queue for this partition key, create one
					Queue<TableEntity> queue = speedingVehicles.get(partitionKey);
					if (queue == null) {
						queue = new LinkedList<TableEntity>();
						speedingVehicles.put(partitionKey, queue);
					}
					queue.add(speeding);
					
				} else {
					// Do nothing
				}
			}
					
			// Partition keys and records in each?
			for (String key: speedingVehicles.keySet()) {
				System.out.println("SpeedingVehicle key " + key + "," + speedingVehicles.get(key).size() + " values");
			}
						
			// If there are any speedingvehicles queued, then insert them
			for (Queue<TableEntity> queue: speedingVehicles.values()) {
				tableService.batchInsert("speedingvehicles", queue);
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
