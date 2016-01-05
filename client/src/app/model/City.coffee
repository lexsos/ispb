Ext.define 'ISPBClient.model.City',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/0.1/city'
    reader: {type: 'json', root: 'city_list'}