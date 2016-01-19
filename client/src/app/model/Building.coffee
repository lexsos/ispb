Ext.define 'ISPBClient.model.Building',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
    { name: 'cityId', type: 'int' }
    { name: 'cityName', type: 'string' }
    { name: 'streetId', type: 'int' }
    { name: 'streetName', type: 'string' }
    { name: 'qualifiedStreetName', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/building'
    reader: {type: 'json', root: 'buildingList'}