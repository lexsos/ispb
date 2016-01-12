Ext.define 'ISPBClient.store.StreetStore',
  extend: 'Ext.data.Store'

  model: 'ISPBClient.model.Street'
  autoLoad: false
  groupField: 'cityName'