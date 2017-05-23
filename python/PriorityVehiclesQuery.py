# Priority Speeding Vehicles Query

import numpy as np
import pandas as pd
pd.set_option('display.expand_frame_repr', False)

from azure.storage.table import TableService, Entity

# Import credentials

from AzureCredentials import table_accountname, table_accountkey

# Create a table service object

table_service = TableService(
	account_name = table_accountname,
	account_key = table_accountkey)            

# Priority Speeding Vehicles

sv_list = []
speedingvehicles = table_service.query_entities('speedingvehicles', filter="Priority eq 'PRIORITY'", select='Registration')
for sv in speedingvehicles:
    if not sv.Registration in sv_list:
        sv_list.append(sv.Registration)  

# All Vehicle Sightings

s_array = np.empty([0,6])
for reg in sv_list:
    sightings = table_service.query_entities('sightings', filter="Registration eq '" + reg + "'")
    for s in sightings:
        cameras = table_service.query_entities('registrations', filter="RowKey eq '" + s.Id + "'")
        c = list(cameras)
        if (len(c) > 0):
            s_row = [s.Registration, s.Timestamp.strftime('%d/%m/%Y %H:%M:%S'), s.Speed, c[0].Id, c[0].Street, c[0].Town]
            s_array = np.vstack([s_array, s_row])

s_df = pd.DataFrame(data=s_array, columns=['Registration', 'Timestamp', 'Speed', 'Camera Id', 'Street', 'Town']).sort_values(['Registration', 'Timestamp'])

print(s_df)
