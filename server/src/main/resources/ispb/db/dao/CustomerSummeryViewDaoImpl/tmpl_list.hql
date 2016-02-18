from CustomerSummeryView as customerView
    left join fetch customerView.customer
    left join fetch customerView.customer.building
    left join fetch customerView.customer.building.street
    left join fetch customerView.customer.building.street.city
{where_statement}
{sort_statement}
