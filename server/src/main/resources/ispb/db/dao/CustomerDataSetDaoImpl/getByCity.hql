from CustomerDataSet as customer
    left join fetch customer.building
    left join fetch customer.building.street
    left join fetch customer.building.street.city
where
    customer.deleteAt is null
    and customer.building.street.city = :city
order by
    customer.contractNumber