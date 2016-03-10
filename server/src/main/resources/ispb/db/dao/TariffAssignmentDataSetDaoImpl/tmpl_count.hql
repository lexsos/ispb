select count(tariffAssignment.id)
from TariffAssignmentDataSet as tariffAssignment
    left join tariffAssignment.tariff
    left join tariffAssignment.customer
    left join tariffAssignment.customer.building
    left join tariffAssignment.customer.building.street
    left join tariffAssignment.customer.building.street.city
{where_statement}
