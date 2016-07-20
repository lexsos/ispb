Ext.define 'ISPBClient.model.RadiusClientParameter',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'radiusClientId', type: 'int' }
    { name: 'parameterName', type: 'string' }
    { name: 'parameterValue', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_client_parameter'
    reader: {type: 'json', root: 'radiusClientParameterList'}