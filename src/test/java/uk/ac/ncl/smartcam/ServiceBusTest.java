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
	
	@BeforeClass
	public static void clearSubscriptions() {
		// Consume messages from all subscriptions ready for testing
		try {
			ServiceBus service = new ServiceBus();
			long messageCount;
			messageCount = service.messageCount("AllMessages");
			for (long i=0L; i<messageCount; i++) {
				// Consume message and throw away
				Object message = service.receiveMessage("AllMessages");
			}
			messageCount = service.messageCount("Speeding");
			for (long i=0L; i<messageCount; i++) {
				// Consume message and throw away
				Object message = service.receiveMessage("Speeding");
			}
			messageCount = service.messageCount("AllSightings");
			for (long i=0L; i<messageCount; i++) {
				// Consume message and throw away
				Object message = service.receiveMessage("AllSightings");
			}
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}

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
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#sendMessage(java.lang.Object, java.lang.String, boolean)}.
	 */
	@Test
	public void testSendReceiveSightingMessage() {
		// Create Smart Camera Sighting object
		Sighting sight = new Sighting(9999L, "TE01 TES", "Car", 29, 30);
		ServiceBus service;
		try {
			service = new ServiceBus();
			// Send message
			service.sendMessage(sight, "Sighting", false);
			
			// Receive message from AllMessages
			Sighting message = (Sighting)service.receiveMessage("AllMessages");
			assertEquals("Failure - messages are not equal", message.getRowKey(), sight.getRowKey());
			
			// Receive message from AllSightings
			message = (Sighting)service.receiveMessage("AllSightings");
			assertEquals("Failure - messages are not equal", message.getRowKey(), sight.getRowKey());
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#sendMessage(java.lang.Object, java.lang.String, boolean)}.
	 */
	@Test
	public void testSendReceiveSpeedingMessage() {
		// Create Smart Camera Sighting object
		Sighting sight = new Sighting(12001L, "TE02 TES", "Motorcycle", 40, 30);
		ServiceBus service;
		try {
			service = new ServiceBus();
			// Send message
			service.sendMessage(sight, "Sighting", true);
			
			// Receive message from AllMessages
			Sighting message = (Sighting)service.receiveMessage("AllMessages");
			assertEquals("Failure - messages are not equal", message.getRowKey(), sight.getRowKey());
			
			// Receive message from AllSightings
			message = (Sighting)service.receiveMessage("AllSightings");
			assertEquals("Failure - messages are not equal", message.getRowKey(), sight.getRowKey());
			
			// Receive message from Speeding
			message = (Sighting)service.receiveMessage("Speeding");
			assertEquals("Failure - messages are not equal", message.getRowKey(), sight.getRowKey());
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#messageCount(java.lang.String)}.
	 */
	@Test
	public void testMessageCountRegistrationMessages() {
		// Create Smart Camera Registration object
		Registration[] smartCam = new Registration[10];
		for (int i=0; i<10; i++) {
			smartCam[i] = new Registration(Long.valueOf(i), "Northumberland Street", "Newcastle", 30+(i/3*10));
		}
		ServiceBus service;
		try {
			service = new ServiceBus();
			for (int i=0; i<10; i++) {
				// Send messages
				service.sendMessage(smartCam[i], "Registration", false);
			}
			// Message count
			long messageCount = service.messageCount("AllMessages");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			messageCount = service.messageCount("AllSightings");
			assertEquals("Failure - count is incorrect", 0L, messageCount);
			messageCount = service.messageCount("Speeding");
			assertEquals("Failure - count is incorrect", 0L, messageCount);
			
			// Tidy up after test
			clearSubscriptions();
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#messageCount(java.lang.String)}.
	 */
	@Test
	public void testMessageCountSightingMessages() {
		// Create Smart Camera Sighting object
		Sighting[] sight = new Sighting[10];
		for (int i=0; i<10; i++) {
			sight[i] = new Sighting(Long.valueOf(i), "TE0" + i + " TES", "Truck", 30+(i/3*10)-i, 30+(i/3*10));
		}
		ServiceBus service;
		try {
			service = new ServiceBus();
			for (int i=0; i<10; i++) {
				// Send messages
				service.sendMessage(sight[i], "Sighting", false);
			}
			// Message count
			long messageCount = service.messageCount("AllMessages");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			messageCount = service.messageCount("AllSightings");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			messageCount = service.messageCount("Speeding");
			assertEquals("Failure - count is incorrect", 0L, messageCount);
			
			// Tidy up after test
			clearSubscriptions();
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.ServiceBus#messageCount(java.lang.String)}.
	 */
	@Test
	public void testMessageCountSpeedingMessages() {
		// Create Smart Camera Sighting object
		Sighting[] sight = new Sighting[10];
		for (int i=0; i<10; i++) {
			sight[i] = new Sighting(Long.valueOf(i), "TE0" + i + " TES", "Car", 31+(i/3*10)+i, 30+(i/3*10));
		}
		ServiceBus service;
		try {
			service = new ServiceBus();
			for (int i=0; i<10; i++) {
				// Send messages
				service.sendMessage(sight[i], "Sighting", true);
			}
			// Message count
			long messageCount = service.messageCount("AllMessages");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			messageCount = service.messageCount("AllSightings");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			messageCount = service.messageCount("Speeding");
			assertEquals("Failure - count is incorrect", 10L, messageCount);
			
			// Tidy up after test
			clearSubscriptions();
			
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}

}
