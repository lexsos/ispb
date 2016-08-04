delete RadiusSessionIpDataSet as sessionIp
where sessionIp.session.id in (select session.id from RadiusSessionDataSet as session where session.createAt < :olderThen)