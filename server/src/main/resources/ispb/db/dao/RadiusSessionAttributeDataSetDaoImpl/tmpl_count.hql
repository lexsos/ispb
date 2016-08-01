select count (radiusSessionAttribute.id)
from RadiusSessionAttributeDataSet as radiusSessionAttribute
    left join radiusSessionAttribute.session
    left join radiusSessionAttribute.session.radiusClient
    left join radiusSessionAttribute.session.customer
    left join radiusSessionAttribute.session.customer.building
    left join radiusSessionAttribute.session.customer.building.street
    left join radiusSessionAttribute.session.customer.building.street.city
    left join radiusSessionAttribute.session.radiusUser
    left join radiusSessionAttribute.session.radiusUser.customer
    left join radiusSessionAttribute.session.radiusUser.customer.building
    left join radiusSessionAttribute.session.radiusUser.customer.building.street
    left join radiusSessionAttribute.session.radiusUser.customer.building.street.city
{where_statement}