select count(payment.id)
from PaymentDataSet as payment
    left join payment.group
    left join payment.customer
{where_statement}
