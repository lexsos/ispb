Ext.define 'ISPBClient.model.User',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'login', type: 'string' }
    { name: 'name', type: 'string' }
    { name: 'surname', type: 'string' }
    { name: 'accessLevel', type: 'int' }
    { name: 'active', type: 'boolean' }

    { name: 'password', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/user'
    reader: {type: 'json', root: 'userList'}