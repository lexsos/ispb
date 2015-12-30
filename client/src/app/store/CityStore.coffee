Ext.define 'ISPBClient.store.CityStore',
  extend: 'Ext.data.Store'

  model: 'ISPBClient.model.City'
  autoLoad: true

  proxy:
    type: 'rest'
    url : 'rest_api/0.1/city'
    reader: {type: 'json', root: 'city_list'}
