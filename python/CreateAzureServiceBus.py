# Create Microsoft Azure Service Bus Topic and Subscriptions

from azure.servicebus import ServiceBusService, Message, Topic, Rule, DEFAULT_RULE_NAME

# Import credentials

from AzureCredentials import svcbus_namespace, svcbus_keyname, svcbus_keyvalue

# Create a ServiceBusService object

bus_service = ServiceBusService(
    service_namespace = svcbus_namespace,
    shared_access_key_name = svcbus_keyname,
    shared_access_key_value = svcbus_keyvalue)

# Create a Topic

bus_service.create_topic('cameratopic')

# Create Subscriptions

# NoSQL Consumer application retrieves all registration and sighting messages

bus_service.create_subscription('cameratopic', 'AllMessages')

# Police Monitor application retrieves sighting messages where speed>speedlimit

bus_service.create_subscription('cameratopic', 'Speeding')

rule = Rule()
rule.filter_type = 'SqlFilter'
rule.filter_expression = 'speeding = True'

bus_service.create_rule('cameratopic', 'Speeding', 'SpeedingFilter', rule)
bus_service.delete_rule('cameratopic', 'Speeding', DEFAULT_RULE_NAME)

# Vehicle check application retrieves all sighting messages

bus_service.create_subscription('cameratopic', 'VehCheck')

bus_service.create_rule('cameratopic', 'VehCheck', 'SpeedingFilter', rule)
bus_service.delete_rule('cameratopic', 'VehCheck', DEFAULT_RULE_NAME)



