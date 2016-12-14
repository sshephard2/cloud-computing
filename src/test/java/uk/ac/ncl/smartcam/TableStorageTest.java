/**
 * 
 */
package uk.ac.ncl.smartcam;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import com.microsoft.azure.storage.table.TableEntity;
import com.microsoft.azure.storage.table.TableOperation;

import uk.ac.ncl.smartcam.Registration;
import uk.ac.ncl.smartcam.Sighting;
import uk.ac.ncl.smartcam.SpeedingVehicle;

/**
 * @author s.shephard2
 *
 */
public class TableStorageTest {

	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#singleInsert(java.lang.String, com.microsoft.azure.storage.table.TableEntity)}.
	 */
	@Test
	public void testSingleInsertRegistration() {
		// Create Smart Camera Registration object
		Registration smartCam = new Registration(191L, "The Side", "Newcastle", 30);
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Write to table
			tableService.singleInsert("registrations", smartCam);
			
			// Retrieve back from table
			TableOperation query = TableOperation.retrieve(smartCam.getPartitionKey(), smartCam.getRowKey(), Registration.class);
			Registration result = (Registration)tableService.query("registrations", query);
			
			assertEquals("Failure to write/retrieve object", smartCam.getRowKey(), result.getRowKey());
						
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}

	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#singleInsert(java.lang.String, com.microsoft.azure.storage.table.TableEntity)}.
	 */
	@Test
	public void testSingleInsertSighting() {
		// Create Smart Camera Sighting object
		Sighting sight = new Sighting("TEST9999", "TE01 TES", "Car", 29, 30);
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Write to table
			tableService.singleInsert("sightings", sight);
			
			// Retrieve back from table
			TableOperation query = TableOperation.retrieve(sight.getPartitionKey(), sight.getRowKey(), Sighting.class);
			Sighting result = (Sighting)tableService.query("sightings", query);
			
			assertEquals("Failure to write/retrieve object", sight.getRowKey(), result.getRowKey());
						
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#singleInsert(java.lang.String, com.microsoft.azure.storage.table.TableEntity)}.
	 */
	@Test
	public void testSingleInsertSpeedingVehicle() {
		// Create Smart Camera SpeedingVehicle object
		SpeedingVehicle speeding = new SpeedingVehicle("TEST9999", "TE01 TES", "Car", 40, 30, "PRIORITY");
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Write to table
			tableService.singleInsert("speedingvehicles", speeding);
			
			// Retrieve back from table
			TableOperation query = TableOperation.retrieve(speeding.getPartitionKey(), speeding.getRowKey(), SpeedingVehicle.class);
			SpeedingVehicle result = (SpeedingVehicle)tableService.query("speedingvehicles", query);
			
			assertEquals("Failure to write/retrieve object", speeding.getRowKey(), result.getRowKey());
						
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#batchInsert(java.lang.String, java.util.Queue)}.
	 */
	@Test
	public void testBatchInsertRegistration() {
		// Create Smart Camera Registration objects
		Queue<TableEntity> queue = new LinkedList<TableEntity>();
		for (int i=0; i<10; i++) {
			queue.add(new Registration(Long.valueOf(i), "Northumberland Street", "Newcastle", 30+(i/3*10)));
		}
		Queue<TableEntity> queueCopy = new LinkedList<TableEntity>(queue);
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Batch write to table
			tableService.batchInsert("registrations", queue);
			
			for(TableEntity entity: queueCopy) {
				// Retrieve back from table
				TableOperation query = TableOperation.retrieve(entity.getPartitionKey(), entity.getRowKey(), Registration.class);
				TableEntity result = tableService.query("registrations", query);
				assertEquals("Failure to write/retrieve object", entity.getRowKey(), result.getRowKey());
			}
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}

	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#batchInsert(java.lang.String, java.util.Queue)}.
	 */
	@Test
	public void testBatchInsertSighting() {
		// Create Smart Camera Sighting objects
		Queue<TableEntity> queue = new LinkedList<TableEntity>();
		for (int i=0; i<10; i++) {
			queue.add(new Sighting("TEST" + i, "TE01 TE" + (char) (i + 'A'), "Truck", 30+(i/3*10)-i, 30+(i/3*10)));
		}
		Queue<TableEntity> queueCopy = new LinkedList<TableEntity>(queue);
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Batch write to table
			tableService.batchInsert("sightings", queue);
			
			for(TableEntity entity: queueCopy) {
				// Retrieve back from table
				TableOperation query = TableOperation.retrieve(entity.getPartitionKey(), entity.getRowKey(), Sighting.class);
				TableEntity result = tableService.query("sightings", query);
				assertEquals("Failure to write/retrieve object", entity.getRowKey(), result.getRowKey());
			}
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
	/**
	 * Test method for {@link uk.ac.ncl.smartcam.TableStorage#batchInsert(java.lang.String, java.util.Queue)}.
	 */
	@Test
	public void testBatchInsertSpeedingVehicle() {
		// Create Smart Camera SpeedingVehicle objects
		Queue<TableEntity> queue = new LinkedList<TableEntity>();
		for (int i=0; i<10; i++) {
			queue.add(new SpeedingVehicle("TEST"+ i , "TE01 TE" + (char) (i + 'A'), "Car", 40+(i/3*10)+i, 30+(i/3*10), "PRIORITY"));
		}
		Queue<TableEntity> queueCopy = new LinkedList<TableEntity>(queue);
		
		// Connect to Azure Table Storage
		TableStorage tableService;
		try {
			tableService = new TableStorage();
			
			// Batch write to table
			tableService.batchInsert("speedingvehicles", queue);
			
			for(TableEntity entity: queueCopy) {
				// Retrieve back from table
				TableOperation query = TableOperation.retrieve(entity.getPartitionKey(), entity.getRowKey(), SpeedingVehicle.class);
				TableEntity result = tableService.query("speedingvehicles", query);
				assertEquals("Failure to write/retrieve object", entity.getRowKey(), result.getRowKey());
			}
		} catch (Exception e) {
			fail("Unexpected exception:" + e.getStackTrace());
		}
	}
	
}
