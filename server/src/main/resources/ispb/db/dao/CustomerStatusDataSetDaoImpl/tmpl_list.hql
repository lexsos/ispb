from CustomerStatusDataSet as сustomerStatus
    left join fetch сustomerStatus.customer
    left join fetch сustomerStatus.customer.building
    left join fetch сustomerStatus.customer.building.street
    left join fetch сustomerStatus.customer.building.street.city
{where_statement}
{sort_statement}