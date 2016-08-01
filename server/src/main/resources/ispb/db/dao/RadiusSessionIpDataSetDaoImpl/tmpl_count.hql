select count (radiusSessionIp.id)
from RadiusSessionIpDataSet as radiusSessionIp
    left join radiusSessionIp.session
    left join radiusSessionIp.session.radiusClient
    left join radiusSessionIp.session.customer
    left join radiusSessionIp.session.customer.building
    left join radiusSessionIp.session.customer.building.street
    left join radiusSessionIp.session.customer.building.street.city
    left join radiusSessionIp.session.radiusUser
    left join radiusSessionIp.session.radiusUser.customer
    left join radiusSessionIp.session.radiusUser.customer.building
    left join radiusSessionIp.session.radiusUser.customer.building.street
    left join radiusSessionIp.session.radiusUser.customer.building.street.city
{where_statement}