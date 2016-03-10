Ext.define 'ISPBClient.controller.tariff.EditTariffWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['tariff.EditTariffWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.Tariff'
    windowWidget: 'editTariffWindow'

  validateForm: (form) ->
    tariffNameField = form.findField('name')

    if tariffNameField.getValue() == ""
      tariffNameField.markInvalid('Укажите название тарифа')
      return false

    if !tariffNameField.isDirty()
      return true

    if ISPBClient.utils.RpcProcedure.tariffNameExist(tariffNameField.getValue())
      tariffNameField.markInvalid('Тарифа с указанным именем уже существует')
      return false

    return true