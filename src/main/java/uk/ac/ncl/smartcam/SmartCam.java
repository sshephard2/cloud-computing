/**
 * 
 */
package uk.ac.ncl.smartcam;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.ServiceBus;

/**
 * Smart Camera runnable application
 *  * @author Stephen Shephard
 *
 */
public class SmartCam {
	
	private static final String namespace = "sshephard2";
	private static final String apikey = "u7/GuimIja/8ija5GP4sCWfBjcAqQ6/KXQ3SLDRqf4U=";

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
		ServiceBus service = new ServiceBus(namespace, apikey);
		
		// Create Smart Camera Registration object
		Registration smartCam = new Registration(id, street, town, speedlimit);
		service.sendMessage(smartCam);
	}

}
