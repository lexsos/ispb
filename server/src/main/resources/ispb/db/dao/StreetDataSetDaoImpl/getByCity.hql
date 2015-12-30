from StreetDataSet as street
    left join fetch street.city
where
    street.deleteAt is null
    and street.city = :city
order by
    street.city.name,
    street.name
