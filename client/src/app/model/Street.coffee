Ext.define 'ISPBClient.model.Street',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
    { name: 'cityId', type: 'int' }
    { name: 'cityName', type: 'string' }

  ]

  proxy:
    type: 'rest'
    url : '/api/rest/street'
    reader: {type: 'json', root: 'streetList'}