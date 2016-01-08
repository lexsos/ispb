Ext.define 'ISPBClient.model.City',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/city'
    reader: {type: 'json', root: 'city_list'}