select count(сustomerStatus.id)
from CustomerStatusDataSet as сustomerStatus
    left join сustomerStatus.customer
    left join сustomerStatus.customer.building
    left join сustomerStatus.customer.building.street
    left join сustomerStatus.customer.building.street.city
{where_statement}