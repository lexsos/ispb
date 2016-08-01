from RadiusSessionDataSet as radiusSession
    left join fetch radiusSession.radiusClient
    left join fetch radiusSession.radiusUser
    left join fetch radiusSession.radiusUser.customer
    left join fetch radiusSession.radiusUser.customer.building
    left join fetch radiusSession.radiusUser.customer.building.street
    left join fetch radiusSession.radiusUser.customer.building.street.city
    left join fetch radiusSession.customer
    left join fetch radiusSession.customer.building
    left join fetch radiusSession.customer.building.street
    left join fetch radiusSession.customer.building.street.city
{where_statement}
{sort_statement}