select count(tariffRadiusAttribute.id)
from TariffRadiusAttributeDataSet as tariffRadiusAttribute
    left join tariffRadiusAttribute.tariff
{where_statement}
