Ext.define 'ISPBClient.controller.customer.EditCustomerWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.EditCustomerWindow']
  models: ['Payment']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'editCustomerWindow button[action=cancel]':
        click: this.onCancelClick

      'editCustomerWindow button[action=save]':
        click: this.onSaveClick

      'editCustomerWindow combobox[name=cityId]':
        change: this.onCityChange

      'editCustomerWindow combobox[name=streetId]':
        change: this.onStreetChange

  validateForm: (form) ->
    contractNumberField = form.findField("contractNumber")
    nameField = form.findField("name")
    cityIdField = form.findField("cityId")
    streetIdField = form.findField("streetId")
    buildingIdField = form.findField("buildingId")

    if contractNumberField.getValue() == ""
      contractNumberField.markInvalid('Укажите номер договора')
      return false

    if nameField.getValue() == ""
      nameField.markInvalid('Укажите имя абонента')
      return false

    if cityIdField.getValue() == null
      cityIdField.markInvalid('Укажите город')
      return false

    if streetIdField.getValue() == null
      streetIdField.markInvalid('Укажите улицу')
      return false

    if buildingIdField.getValue() == null
      buildingIdField.markInvalid('Укажите здание')
      return false

    if contractNumberField.isDirty() and ISPBClient.utils.RpcProcedure.contractNumberExist(contractNumberField.getValue())
      contractNumberField.markInvalid('Указанный номер договора уже существует')
      return false

    return true

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    form   = window.down('form').getForm()
    store =  window.getStore()
    record = form.getRecord()
    values = form.getValues()

    if !this.validateForm(form)
      return

    record.set(values)
    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()

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