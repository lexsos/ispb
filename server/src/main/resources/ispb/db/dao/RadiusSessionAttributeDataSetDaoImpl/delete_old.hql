delete RadiusSessionAttributeDataSet as radiusSessionAttribute
where radiusSessionAttribute.session.id in (select session.id from RadiusSessionDataSet as session where session.createAt < :olderThen)