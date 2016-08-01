select count(radiusSession.id)
from RadiusSessionDataSet as radiusSession
    left join radiusSession.radiusClient
    left join radiusSession.radiusUser
    left join radiusSession.radiusUser.customer
    left join radiusSession.radiusUser.customer.building
    left join radiusSession.radiusUser.customer.building.street
    left join radiusSession.radiusUser.customer.building.street.city
    left join radiusSession.customer
    left join radiusSession.customer.building
    left join radiusSession.customer.building.street
    left join radiusSession.customer.building.street.city
{where_statement}