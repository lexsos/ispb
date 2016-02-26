Ext.define 'ISPBClient.model.Payment',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'createAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'applyAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'paymentSum', type: 'float' }
    { name: 'processed', type: 'boolean' }

    { name: 'paymentGroupComment', type: 'string' }
    { name: 'customerContractNumber', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/payment'
    reader: {type: 'json', root: 'paymentList'}