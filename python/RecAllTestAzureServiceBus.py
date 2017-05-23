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

msg = bus_service.receive_subscription_message('cameratopic', 'AllMessages', peek_lock=False)
print(msg.body)
