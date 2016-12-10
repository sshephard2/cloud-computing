/**
 * 
 */
package uk.ac.ncl.smartcam;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;

/**
 * @author s.shephard2
 *
 */
public class ServiceBusTest {

	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#sendMessage(java.lang.Object, java.lang.String, boolean)}.
	 */
	@Test
	public void testSendReceiveRegistrationMessage() {
		// Create Smart Camera Registration object
		Registration smartCam = new Registration(191L, "The Side", "Newcastle", 30);
		ServiceBus service;
		try {
			service = new ServiceBus();
			// Send message
			service.sendMessage(smartCam, "Registration", false);
			// Receive message
			Registration message = (Registration)service.receiveMessage("AllMessages");
			assertEquals("Failure - messages are not equal", message.getRowKey(), smartCam.getRowKey());
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}

	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#messageCount(java.lang.String)}.
	 */
	@Test
	public void testMessageCount() {
		fail("Not yet implemented"); // TODO
	}

}
