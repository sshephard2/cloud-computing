/**
 * 
 */
package uk.ac.ncl.smartcam;

import java.util.Random;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;

/**
 * Smart Camera runnable application
 * @author Stephen Shephard
 *
 */
public class SmartCam {

	/**
	 * Supply as arguments: camera id, street, town/city, speed limit, rate of traffic (vehicles per minute)
	 * @param args - id, street, town, speedlimit, rate
	 */
	public static void main(String[] args) {
		
		Long id;
		String street;
		String town;
		int speedlimit;
		int rate;
		
		// Process the input arguments
		if (args.length <5) {
			throw new IllegalArgumentException("Requires arguments id, street, town, speedlimit, rate");
		}
		
		try {
			id = Long.parseLong(args[0]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Camera id argument must be a valid number");
		}
		
		street = args[1];
		town = args[2];
		
		try {
			speedlimit = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Speed limit argument must be a valid number");
		}

		try {
			rate = Integer.parseInt(args[4]);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Rate argument must be a valid number");
		}
			
		// Connect to Azure Service Bus
		ServiceBus service;
		try {
			service = new ServiceBus();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		// Create Smart Camera Registration object and send it
		Registration smartCam = new Registration(id, street, town, speedlimit);
		service.sendMessage(smartCam, "Registration", false);
		
		// Now run repeatedly, sending the specified rate of vehicles per minute
		boolean running = true;
		
		// Random number generator
		Random rndGen = new Random();
		
		// Sightings variables
		int speed;
		String registration;
		String vehicletype;
		boolean speeding;
		
		while(running) {
			// Create random sighting
			
			// Vehicle type
			switch(rndGen.nextInt(3)) {
				case 0: vehicletype="Car";
				break;
				
				case 1: vehicletype="Truck";
				break;
				
				default: vehicletype="Motorcycle";
				break;				
			}
			
			// Registration
			registration = "AB";
			registration = registration + rndGen.nextInt(10);
			registration = registration + rndGen.nextInt(10);
			registration = registration + " DEF";
			
			// Speed
			speed = rndGen.nextInt(2*speedlimit);
			
			// Create Sighting object
			Sighting camSighting = new Sighting(id, registration, vehicletype, speed);

			// If speed is greater than speedlimit, then set speeding flag to true
			speeding = (speed > speedlimit);
			
			// Send sighting message
			service.sendMessage(camSighting, "Sighting", speeding);
			
			try {
				Thread.sleep(60000/rate); // rate is vehicles per minute, so sleep for 60,000ms/rate
			} catch (InterruptedException e) {
				running = false;
			}			
		}
	}

}
