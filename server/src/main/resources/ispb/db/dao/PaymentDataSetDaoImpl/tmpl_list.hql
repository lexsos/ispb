from PaymentDataSet as payment
    left join fetch payment.group
    left join fetch payment.customer
{where_statement}
{sort_statement}