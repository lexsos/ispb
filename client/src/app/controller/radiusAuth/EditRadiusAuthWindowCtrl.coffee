Ext.define 'ISPBClient.controller.radiusAuth.EditRadiusAuthWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['radiusAuth.EditRadiusAuthWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.RadiusAuth'
    windowWidget: 'editRadiusAuthWindow'

  init: ->
    this.superclass.init.call(this)

    this.control
      'editRadiusAuthWindow combobox[name=cityId]':
        change: this.onCityChange

      'editRadiusAuthWindow combobox[name=streetId]':
        change: this.onStreetChange

      'editRadiusAuthWindow combobox[name=buildingId]':
        change: this.onBuildingChange

  validateForm: (form) ->
    userNameField = form.findField("userName")
    ip4AddressField = form.findField("ip4Address")
    customerIdField = form.findField("customerId")

    if userNameField.getValue() == ""
      userNameField.markInvalid('Укажите логин абонента')
      return false

    if customerIdField.getValue() == null
      customerIdField.markInvalid('Укажите номер договора')
      return false

    # TODO: make exist check

    #if userNameField.isDirty() and
    #  userNameField.markInvalid('Указанный логин уже существует')
    #  return false

    #if ip4AddressField.isDirty() and ip4AddressField.getValue() != "" and
    #  ip4AddressField.markInvalid('Указанный адрес уже используется')
    #  return false

    return true

  onCityChange: (element, newValue, oldValue) ->
    streetCombo = element.up('form').down('combobox[name=streetId]')
    streetStore = streetCombo.getStore()
    streetStore.clearFilter(true)
    streetStore.filter("cityId__eq", newValue)
    streetCombo.clearValue()
    if oldValue
      streetCombo.clearValue()

  onStreetChange: (element, newValue, oldValue) ->
    buildingCombo = element.up('form').down('combobox[name=buildingId]')
    buildingStore = buildingCombo.getStore()
    buildingStore.clearFilter(true)
    buildingStore.filter("streetId__eq", newValue)
    if oldValue
      buildingCombo.clearValue()

  onBuildingChange: (element, newValue, oldValue) ->
    customerCombo = element.up('form').down('combobox[name=customerId]')
    customerStore = customerCombo.getStore()
    customerStore.clearFilter(true)
    customerStore.filter("buildingId__eq", newValue)
    if oldValue
      customerCombo.clearValue()
