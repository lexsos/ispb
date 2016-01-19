Ext.define 'ISPBClient.controller.city.AddWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['city.AddWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.City'
    windowWidget: 'addCityWindow'

  validateForm: (form) ->
    cityNameField = form.findField('name')

    if cityNameField.getValue() == ""
      cityNameField.markInvalid('Укажите имя города')
      return false

    if !form.isDirty()
      return true

    if ISPBClient.utils.RpcProcedure.cityNameExist(cityNameField.getValue())
      cityNameField.markInvalid('Город с указанным именем уже существует')
      return false

    return true