Ext.define 'ISPBClient.model.RadiusClient',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'ip4Address', type: 'string' }
    { name: 'secret', type: 'string' }
    { name: 'rejectInactive', type: 'boolean' }
    { name: 'clientType', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_client'
    reader: {type: 'json', root: 'radiusClientList'}