select sum (payment.paymentSum)
from PaymentDataSet as payment
where
    payment.applyAt <= :applyAt and
    payment.customer.id = :customerId and
    payment.processed = true and
    payment.deleteAt is null