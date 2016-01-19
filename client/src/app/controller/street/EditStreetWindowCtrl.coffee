Ext.define 'ISPBClient.controller.street.EditStreetWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['street.EditStreetWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.Street'
    windowWidget: 'editStreetWindow'

  validateForm: (form) ->
    cityField = form.findField('cityId')
    streetNameField = form.findField('name')

    if cityField.getValue() == null
      cityField.markInvalid('Укажите город')
      return false

    if streetNameField.getValue() == ""
      streetNameField.markInvalid('Укажите имя улицы')
      return false

    if !form.isDirty()
      return true

    if ISPBClient.utils.RpcProcedure.streetNameExist(streetNameField.getValue(), cityField.getValue())
      streetNameField.markInvalid('Улица с указанным именем уже существует')
      return false

    return true