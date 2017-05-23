# Camera Registrations Query

import numpy as np
import pandas as pd

from azure.storage.table import TableService, Entity

# Import credentials

from AzureCredentials import table_accountname, table_accountkey

# Create a table service object

table_service = TableService(
	account_name = table_accountname,
	account_key = table_accountkey)

# Registrations

reg_array = np.empty([0,5])
registrations = table_service.query_entities('registrations')
for r in registrations:
    r_row = [r.Id, r.Timestamp.strftime('%d/%m/%Y %H:%M:%S'), r.Street, r.Town, r.Speedlimit]
    reg_array = np.vstack([reg_array, r_row])

reg_df = pd.DataFrame(data=reg_array, columns=['Id', 'Timestamp', 'Street', 'Town', 'Speedlimit']).sort_values(['Timestamp', 'Id'])

print(reg_df)
