# Smart Camera

## Installation

To set up the Camera topic and subscriptions for the first time, run the Python script:

    CreateAzureServiceBus.py

To set up the Azure table storage, run the Python script:

    CreateAzureTableStorage.py

## Running

To start up a Smart Camera and pass in the command line parameters camera id, street, town/city, speed limit, rate of traffic (vehicles per minute):

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.SmartCam id street town speedlimit rate

To start the NoSQL Consumer worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.NoSqlConsumer
    
To start the Police Monitor worker application on a virtual machine:

    java -cp target/SmartCam-0.0.1-SNAPSHOT-jar-with-dependencies uk.ac.ncl.smartcam.PoliceMonitor