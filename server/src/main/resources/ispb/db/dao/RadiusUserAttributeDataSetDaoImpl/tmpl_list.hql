from RadiusUserAttributeDataSet as radiusUserAttribute
    left join fetch radiusUserAttribute.user
    left join fetch radiusUserAttribute.user.customer
    left join fetch radiusUserAttribute.user.customer.building
    left join fetch radiusUserAttribute.user.customer.building.street
    left join fetch radiusUserAttribute.user.customer.building.street.city
{where_statement}
{sort_statement}