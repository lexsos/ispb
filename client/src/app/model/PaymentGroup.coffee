Ext.define 'ISPBClient.model.PaymentGroup',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'createAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'comment', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/payment_group'
    reader: {type: 'json', root: 'paymentGroupList'}