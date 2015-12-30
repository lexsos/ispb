from UserDataSet as user
where
    user.deleteAt is null
    and user.login = :login