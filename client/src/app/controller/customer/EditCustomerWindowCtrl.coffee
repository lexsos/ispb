Ext.define 'ISPBClient.controller.customer.EditCustomerWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.EditCustomerWindow']
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