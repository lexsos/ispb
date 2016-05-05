update RadiusUserDataSet
set deleteAt = :deleteAt
where
    customer is null and
    deleteAt is null