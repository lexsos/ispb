select count(radiusUserAttribute.id)
from RadiusUserAttributeDataSet as radiusUserAttribute
    left join radiusUserAttribute.user
    left join radiusUserAttribute.user.customer
    left join radiusUserAttribute.user.customer.building
    left join radiusUserAttribute.user.customer.building.street
    left join radiusUserAttribute.user.customer.building.street.city
{where_statement}
