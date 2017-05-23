# Create Microsoft Azure Service Bus Topic and Subscriptions

from azure.servicebus import ServiceBusService, Message, Topic, Rule, DEFAULT_RULE_NAME

# Import credentials

from AzureCredentials import svcbus_namespace, svcbus_keyname, svcbus_keyvalue

# Create a ServiceBusService object

bus_service = ServiceBusService(
    service_namespace = svcbus_namespace,
    shared_access_key_name = svcbus_keyname,
    shared_access_key_value = svcbus_keyvalue)

# Test messages

msg = Message('Camera registration'.encode('utf-8'), custom_properties={'messagetype':'Registration'})
bus_service.send_topic_message('cameratopic', msg)

msg = Message('Camera sighting'.encode('utf-8'), custom_properties={'messagetype':'Sighting'})
bus_service.send_topic_message('cameratopic', msg)

msg = Message('Camera sighting - speeding'.encode('utf-8'), custom_properties={'messagetype':'Sighting', 'speeding':True})
bus_service.send_topic_message('cameratopic', msg)
