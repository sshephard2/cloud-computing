# Smart Camera

## Installation

To set up the Camera topic and subscriptions for the first time, run the Python script:

    CreateAzureServiceBus.py

To set up the Azure table storage, run the Python script:

    CreateAzureTableStorage.py

In addition there are a series of Python scripts that can perform simple tests to validate that the topic and subscriptions have been created correctly, by sending and consuming messages.

    SendTestAzureServiceBus.py
    RecAllTestAzureServiceBus.py
    RecVehCheckTestAzureServiceBus.py
    RecSpeedingTestAzureServiceBus.py

Before building the Smart Camera applications, the `config.properties` file under `src/main/resources` must be configured with the Microsoft Azure Service Bus and Table Account login details:

	# Service Bus API config details
	
	ServiceBusNamespace = <your namespace>
	ServiceBusKey = <your namespace key>
	Topic = cameratopic
	ReceiveTimeout = 5
	TableAccountName = <your table account name>
	TableAccountKey = <your table account key>

The applications may then be built using Maven:

    mvn clean package

After this, there is a bug in the Microsoft Azure libraries that causes a duplicate config file to be added to the jar file produced.  Open the jar file and remove the smaller version of `com.microsoft.windowsazure.core.Builder$Exports` from the `META-INF\services\` directory.

## Running

To start up a Smart Camera and pass in the command line parameters camera id, street, town/city, speed limit, rate of traffic (vehicles per minute):

    java -cp target\SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies.jar uk.ac.ncl.smartcam.SmartCam id street town speedlimit rate

To start the NoSQL Consumer worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.NoSqlConsumer
    
To start the Police Monitor worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.PoliceMonitor

## Queries

The queries are implemented in Python.

To query for all camera registrations use:

    CameraRegistrationsQuery.py

To query for all sightings of all priority speeding vehicles use:

    PriorityVehiclesQuery.py
	