from StreetDataSet as street
    left join fetch street.city
where
    street.deleteAt is null
order by
    street.city.name,
    street.name
