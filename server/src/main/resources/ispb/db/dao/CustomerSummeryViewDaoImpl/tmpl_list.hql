from CustomerSummeryView as customerView
    left join fetch customerView.customer
    left join fetch customerView.customer.building
    left join fetch customerView.customer.building.street
    left join fetch customerView.customer.building.street.city
{where_statement}
order by
    customerView.customer.surname,
    customerView.customer.name
