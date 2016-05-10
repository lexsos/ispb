Ext.define 'ISPBClient.model.RadiusDictionary',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'attributeName', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/radius_dictionary'
    reader: {type: 'json', root: 'radiusAttributeList'}