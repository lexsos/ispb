from RadiusUserDataSet as radiusUser
    left join fetch radiusUser.customer
    left join fetch radiusUser.customer.building
    left join fetch radiusUser.customer.building.street
    left join fetch radiusUser.customer.building.street.city
{where_statement}
{sort_statement}