# Create Microsoft Azure NoSQL tables

from azure.storage.table import TableService, Entity

# Import credentials

from AzureCredentials import table_accountname, table_accountkey

# Create tables

table_service = TableService(
	account_name = table_accountname,
	account_key = table_accountkey)

# Registrations

table_service.create_table('registrations')

# Sightings

table_service.create_table('sightings')

# Speeding Vehicles

table_service.create_table('speedingvehicles')
