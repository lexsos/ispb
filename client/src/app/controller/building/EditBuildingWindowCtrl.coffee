Ext.define 'ISPBClient.controller.building.EditBuildingWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['building.EditBuildingWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.Building'
    windowWidget: 'editBuildingWindow'

  init: ->
    this.superclass.init.call(this)

    this.control
      'editBuildingWindow combobox[name=cityId]':
        change: this.onCityChange

  onCityChange: (element, newValue)->
    streetCombo = element.up('form').down('combobox[name=streetId]')
    streetStore = streetCombo.getStore()
    streetStore.clearFilter(true)
    streetStore.filter("cityId__eq", newValue)
    streetCombo.clearValue()

  validateForm: (form) ->
    streetIdField = form.findField("streetId")
    buildingNameField = form.findField("name")

    if streetIdField.getValue() == null
      streetIdField.markInvalid('Укажите улицу')
      return false

    if buildingNameField.getValue() == ""
      buildingNameField.markInvalid('Укажите номер здания')
      return false

    if !form.isDirty()
      return true

    if ISPBClient.utils.RpcProcedure.buildingNameExist(buildingNameField.getValue(), streetIdField.getValue())
      buildingNameField.markInvalid('Здание с указанным номером уже существует')
      return false

    return true
