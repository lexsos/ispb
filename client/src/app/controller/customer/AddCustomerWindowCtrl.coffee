Ext.define 'ISPBClient.controller.customer.AddCustomerWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.AddCustomerWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'addCustomerWindow button[action=cancel]':
        click: this.onCancelClick

      'addCustomerWindow button[action=save]':
        click: this.onSaveClick

      'addCustomerWindow combobox[name=cityId]':
        change: this.onCityChange

      'addCustomerWindow combobox[name=streetId]':
        change: this.onStreetChange

  onCancelClick: (element) ->
    element.up('window').close()

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

  validateForm: (form, tabPanel) ->
    contractNumberField = form.findField("contractNumber")
    nameField = form.findField("name")
    cityIdField = form.findField("cityId")
    streetIdField = form.findField("streetId")
    buildingIdField = form.findField("buildingId")
    tariffIdField = form.findField("tariffId")
    statusField = form.findField("status")

    if contractNumberField.getValue() == ""
      contractNumberField.markInvalid('Укажите номер договора')
      tabPanel.setActiveTab(0)
      return false

    if ISPBClient.utils.RpcProcedure.contractNumberExist(contractNumberField.getValue())
      contractNumberField.markInvalid('Указанный номер договора уже существует')
      tabPanel.setActiveTab(0)
      return false

    if nameField.getValue() == ""
      nameField.markInvalid('Укажите имя абонента')
      tabPanel.setActiveTab(0)
      return false

    if cityIdField.getValue() == null
      cityIdField.markInvalid('Укажите город')
      tabPanel.setActiveTab(0)
      return false

    if streetIdField.getValue() == null
      streetIdField.markInvalid('Укажите улицу')
      tabPanel.setActiveTab(0)
      return false

    if buildingIdField.getValue() == null
      buildingIdField.markInvalid('Укажите здание')
      tabPanel.setActiveTab(0)
      return false

    if tariffIdField.getValue() == null
      tariffIdField.markInvalid('Укажите тариф')
      tabPanel.setActiveTab(1)
      return false

    if statusField.getValue() == null
      statusField.markInvalid('Укажите статус')
      tabPanel.setActiveTab(1)
      return false

    return true

  onSaveClick: (element) ->
    window = element.up('window')
    form   = window.down('form').getForm()
    store =  window.getStore()
    values = form.getValues()
    tabPanel = window.down('form tabpanel')

    if !this.validateForm(form, tabPanel)
      return

    record = Ext.create('ISPBClient.model.Customer')
    record.set(values)
    store.add(record)
    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()