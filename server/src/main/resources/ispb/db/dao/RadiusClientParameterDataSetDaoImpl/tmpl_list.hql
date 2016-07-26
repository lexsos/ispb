from RadiusClientParameterDataSet as clientParameter
    left join fetch clientParameter.client
{where_statement}
{sort_statement}