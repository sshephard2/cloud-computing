# Smart Camera

## Installation

To set up the Camera topic and subscriptions for the first time, run the Python script:

    CreateAzureServiceBus.py

To set up the Azure table storage, run the Python script:

    CreateAzureTableStorage.py

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

## Running

To start up a Smart Camera and pass in the command line parameters camera id, street, town/city, speed limit, rate of traffic (vehicles per minute):

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.SmartCam id street town speedlimit rate

To start the NoSQL Consumer worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.NoSqlConsumer
    
To start the Police Monitor worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.PoliceMonitor