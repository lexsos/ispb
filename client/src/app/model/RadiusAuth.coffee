Ext.define 'ISPBClient.model.RadiusAuth',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'createAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'userName', type: 'string' }
    { name: 'password', type: 'string' }
    { name: 'ip4Address', type: 'string' }
    { name: 'customerId', type: 'int' }

    { name: 'customerQualifiedName', type: 'string' }
    { name: 'customerQualifiedAddress', type: 'string' }
    { name: 'contractNumber', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_auth'
    reader: {type: 'json', root: 'radiusAuthList'}