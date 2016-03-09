Ext.define 'ISPBClient.model.Tariff',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
    { name: 'dailyPayment', type: 'float' }
    { name: 'offThreshold', type: 'float' }
    { name: 'autoDailyPayment', type: 'boolean' }
    { name: 'upRate', type: 'float' }
    { name: 'downRate', type: 'float' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/tariff'
    reader: {type: 'json', root: 'tariffList'}