Ext.define 'ISPBClient.controller.tariff.EditTariffWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['tariff.EditTariffWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.Tariff'
    windowWidget: 'editTariffWindow'

  validateForm: (form) ->
    return true