from PaymentDataSet as payment
    left join fetch payment.group
    left join fetch payment.customer
    left join fetch payment.customer.building
    left join fetch payment.customer.building.street
    left join fetch payment.customer.building.street.city
{where_statement}
{sort_statement}