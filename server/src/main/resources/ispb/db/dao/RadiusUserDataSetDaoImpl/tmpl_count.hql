select count (radiusUser.id)
from RadiusUserDataSet as radiusUser
    left join radiusUser.customer
    left join radiusUser.customer.building
    left join radiusUser.customer.building.street
    left join radiusUser.customer.building.street.city
{where_statement}
