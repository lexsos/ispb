from RadiusSessionAttributeDataSet as radiusSessionAttribute
    left join fetch radiusSessionAttribute.session
    left join fetch radiusSessionAttribute.session.radiusClient
    left join fetch radiusSessionAttribute.session.customer
    left join fetch radiusSessionAttribute.session.customer.building
    left join fetch radiusSessionAttribute.session.customer.building.street
    left join fetch radiusSessionAttribute.session.customer.building.street.city
    left join fetch radiusSessionAttribute.session.radiusUser
    left join fetch radiusSessionAttribute.session.radiusUser.customer
    left join fetch radiusSessionAttribute.session.radiusUser.customer.building
    left join fetch radiusSessionAttribute.session.radiusUser.customer.building.street
    left join fetch radiusSessionAttribute.session.radiusUser.customer.building.street.city
{where_statement}
{sort_statement}