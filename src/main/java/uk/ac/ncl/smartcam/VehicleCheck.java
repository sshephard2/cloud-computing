package uk.ac.ncl.smartcam;

import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;

public class VehicleCheck {
	
	private static final String Sightings = "AllSightings";
	
	/**
	 * Simulated Vehicle Check
	 * @param vehicleRegistration
	 * @return
	 * @throws InterruptedException
	 */
	private static boolean isVehicleStolen(String vehicleRegistration) throws InterruptedException
	{
	    // Thread.sleep(5000);
	    return (Math.random() < 0.95);
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		// Connect to Azure Service Bus
		ServiceBus service;
		try {
			service = new ServiceBus();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		Object message;
		Long messageCount;
		
		// Run until interrupted
		boolean running = true;
		
		while(running) {
			
			// How many messages in the subscription?
			messageCount = service.messageCount(Sightings);
			System.out.println("Subscription " + Sightings + ":" + messageCount + " messages");
			
			message = service.receiveMessage(Sightings);

			if (message instanceof Sighting) {
				System.out.println("Sighting:" + message.toString());

				// Read Sighting message
				Sighting sight = (Sighting)message;
				String registration = sight.getRegistration();

				// Perform Vehicle Check
				System.out.println("Vehicle " + registration + (isVehicleStolen(registration) ? " is STOLEN" : "OK"));

			} else {
				// Do nothing
			}

		}
		
	}

}
