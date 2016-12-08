package uk.ac.ncl.smartcam;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;

public class TableStorage {
	
	private String storageConnectionString;
	private CloudTableClient tableClient;

	/**
	 * Create connection to TableStorage
	 * @throws Exception
	 */
	public TableStorage() throws Exception {

        // Retrieve the connection string
        Properties prop = new Properties();
        try {
            InputStream propertyStream = ServiceBus.class.getClassLoader().getResourceAsStream("config.properties");
            if (propertyStream != null) {
                prop.load(propertyStream);
            }
            else {
                throw new RuntimeException();
            }
        } catch (RuntimeException|IOException e) {
            System.out.println("\nFailed to load config.properties file.");
            throw e;
        }
		
        String acName = prop.getProperty("TableAccountName");
        String acKey = prop.getProperty("TableAccountKey");
        
        this.storageConnectionString =
        		"DefaultEndpointsProtocol=http;" +
        		"AccountName=" + acName + ";" +
        		"AccountKey=" + acKey;
        
        try
        {
            // Retrieve storage account from connection-string
            CloudStorageAccount storageAccount =
                CloudStorageAccount.parse(storageConnectionString);

            // Create the table client
            this.tableClient = storageAccount.createCloudTableClient();
        }
        catch (Exception e)
        {
            // Output the stack trace
            e.printStackTrace();
        }

	}
	
	/**
	 * Insert or Replace entity in table
	 * @param table name of the table
	 * @param entity entity to insert/replace
	 */
	public void tableInsert(String table, TableEntity entity) {
		
	    try {
	    	// Create a cloud table object for the table
			CloudTable cloudTable = tableClient.getTableReference(table);
			
		    // Create an operation to add the new entity to the table
		    TableOperation insertEntity = TableOperation.insertOrReplace(entity);

		    // Submit the operation to the table service
		    cloudTable.execute(insertEntity);
		    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}