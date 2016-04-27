Ext.define 'ISPBClient.model.RadiusAuthAttribute',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'userId', type: 'int' }
    { name: 'attributeName', type: 'string' }
    { name: 'attributeValue', type: 'string' }
    { name: 'condition', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_auth_attribute'
    reader: {type: 'json', root: 'RadiusAuthAttributeList'}