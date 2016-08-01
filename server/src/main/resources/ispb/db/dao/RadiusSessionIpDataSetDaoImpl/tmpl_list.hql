from RadiusSessionIpDataSet as radiusSessionIp
    left join fetch radiusSessionIp.session
    left join fetch radiusSessionIp.session.radiusClient
    left join fetch radiusSessionIp.session.customer
    left join fetch radiusSessionIp.session.customer.building
    left join fetch radiusSessionIp.session.customer.building.street
    left join fetch radiusSessionIp.session.customer.building.street.city
    left join fetch radiusSessionIp.session.radiusUser
    left join fetch radiusSessionIp.session.radiusUser.customer
    left join fetch radiusSessionIp.session.radiusUser.customer.building
    left join fetch radiusSessionIp.session.radiusUser.customer.building.street
    left join fetch radiusSessionIp.session.radiusUser.customer.building.street.city
{where_statement}
{sort_statement}