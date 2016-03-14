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
    { name: 'buildingId', type: 'int' }
    { name: 'room', type: 'string' }

    { name: 'cityId', type: 'int' }
    { name: 'streetId', type: 'int' }
    { name: 'balance', type: 'float' }
    { name: 'qualifiedName', type: 'string' }
    { name: 'qualifiedAddress', type: 'string' }
    { name: 'tariffId', type: 'int' }
    { name: 'tariffName', type: 'string' }
    { name: 'status', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/customer'
    reader: {type: 'json', root: 'customerList'}