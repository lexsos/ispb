Ext.define 'ISPBClient.controller.user.EditUserWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['user.EditUserWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.User'
    windowWidget: 'editUserWindow'

  validateForm: (form) ->
    return true