from CityDataSet as city
where
    city.deleteAt is null
    and city.name = :name
order by
    city.name