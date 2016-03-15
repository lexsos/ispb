Ext.define 'ISPBClient.model.CustomerStatus',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'createAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'applyAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'processed', type: 'boolean' }
    { name: 'status', type: 'string' }
    { name: 'cause', type: 'string' }
    { name: 'customerId', type: 'int'}
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/customer_status'
    reader: {type: 'json', root: 'statusList'}