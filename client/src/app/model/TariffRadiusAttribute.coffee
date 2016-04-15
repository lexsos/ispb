Ext.define 'ISPBClient.model.TariffRadiusAttribute',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'tariffId', type: 'int' }
    { name: 'attributeName', type: 'string' }
    { name: 'attributeValue', type: 'string' }
    { name: 'condition', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/tariff_radius_attribute'
    reader: {type: 'json', root: 'tariffAttributeList'}