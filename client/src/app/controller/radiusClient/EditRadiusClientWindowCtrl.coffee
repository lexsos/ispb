Ext.define 'ISPBClient.controller.radiusClient.EditRadiusClientWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['radiusClient.EditRadiusClientWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.RadiusClient'
    windowWidget: 'editRadiusClientWindow'

  validateForm: (form) ->
    cityField = form.findField('cityId')
    return true