from CityDataSet as city
where
    city.deleteAt is null
order by
    city.name