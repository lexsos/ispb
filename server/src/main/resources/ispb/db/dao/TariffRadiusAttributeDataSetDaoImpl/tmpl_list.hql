from TariffRadiusAttributeDataSet as tariffRadiusAttribute
    left join fetch tariffRadiusAttribute.tariff
{where_statement}
{sort_statement}