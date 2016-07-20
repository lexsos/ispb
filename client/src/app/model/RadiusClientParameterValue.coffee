Ext.define 'ISPBClient.model.RadiusClientParameterValue',
  extend: 'Ext.data.Model'
  fields: [
    { name: 'id', type: 'string' }
    { name: 'displayValue', type: 'string' }
    { name: 'servletType', type: 'string' }
    { name: 'servletParameter', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_client_parameter_value'
    reader: {type: 'json', root: 'radiusClientParameterValueList'}
