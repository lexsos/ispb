Ext.define 'ISPBClient.model.Customer',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'name', type: 'string' }
    { name: 'surname', type: 'string' }
    { name: 'patronymic', type: 'string' }
    { name: 'passport', type: 'string' }
    { name: 'phone', type: 'string' }
    { name: 'comment', type: 'string' }
    { name: 'contractNumber', type: 'string' }
    { name: 'createAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }

    { name: 'buildingId', type: 'int' }
    { name: 'room', type: 'string' }
    { name: 'buildingName', type: 'string' }
    { name: 'streetName', type: 'string' }
    { name: 'cityName', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/customer'
    reader: {type: 'json', root: 'customerList'}