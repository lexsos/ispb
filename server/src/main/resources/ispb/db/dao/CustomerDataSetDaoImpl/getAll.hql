from CustomerDataSet as customer
    left join fetch customer.building
    left join fetch customer.building.street
    left join fetch customer.building.street.city
where
    customer.deleteAt is null
order by
    customer.contractNumber