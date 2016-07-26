Ext.define 'ISPBClient.model.RadiusClientParameterName',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'parameterName', type: 'string' }
    { name: 'displayName', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_client_parameter_name'
    reader: {type: 'json', root: 'radiusClientParameterNameList'}
