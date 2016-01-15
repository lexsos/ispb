Ext.define 'ISPBClient.controller.street.EditStreetWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['street.EditStreetWindow']

  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'editStreetWindow button[action=cancel]':
        click: this.onCancelClick

      'editStreetWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (btn) ->
    btn.up('editStreetWindow').close()

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

  onSaveClick: (btn) ->
    window = btn.up('editStreetWindow')
    form   = window.down('form').getForm()
    record = form.getRecord()
    values = form.getValues()
    store =  window.getStore()

    if !this.validateForm(form)
      return

    if (!record)
      record = Ext.create('ISPBClient.model.Street')
      record.set(values)
      store.add(record)
    else
      record.set(values)

    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()