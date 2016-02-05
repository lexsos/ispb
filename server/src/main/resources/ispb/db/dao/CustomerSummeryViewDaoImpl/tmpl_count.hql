select count (customerView.id)
from CustomerSummeryView as customerView
    left join customerView.customer
    left join customerView.customer.building
    left join customerView.customer.building.street
    left join customerView.customer.building.street.city
{where_statement}
