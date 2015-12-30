from BuildingDataSet as building
    left join fetch building.street
    left join fetch building.street.city
where
    building.deleteAt is null
order by
    building.street.city.name,
    building.street.name,
    building.name