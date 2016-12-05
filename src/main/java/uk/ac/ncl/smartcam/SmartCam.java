/**
 * 
 */
package uk.ac.ncl.smartcam;

import java.util.Date;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;

/**
 * Smart Camera runnable application
 *  * @author Stephen Shephard
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
		
		Registration smartCam = new Registration(id, street, town, speedlimit);
	}

}
