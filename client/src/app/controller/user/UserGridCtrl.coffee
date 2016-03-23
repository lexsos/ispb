Ext.define 'ISPBClient.controller.user.UserGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['user.UserGrid']
  models: ['User']

  config:
    gridWidget: 'UserGrid'
    editWindowWidget: 'editUserWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить пользователя {name}?'
    deleteFieldName: 'login'