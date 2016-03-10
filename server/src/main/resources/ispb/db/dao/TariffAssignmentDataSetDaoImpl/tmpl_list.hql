from TariffAssignmentDataSet as tariffAssignment
    left join fetch tariffAssignment.tariff
    left join fetch tariffAssignment.customer
    left join fetch tariffAssignment.customer.building
    left join fetch tariffAssignment.customer.building.street
    left join fetch tariffAssignment.customer.building.street.city
{where_statement}
{sort_statement}