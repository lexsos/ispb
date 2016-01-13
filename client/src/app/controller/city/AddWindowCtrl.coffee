Ext.define 'ISPBClient.controller.city.AddWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['city.AddWindow']

  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'addCityWindow button[action=cancel]':
        click: this.onCancelClick

      'addCityWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (btn) ->
    btn.up('addCityWindow').close()

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

  onSaveClick: (btn) ->
    window = btn.up('addCityWindow')
    form   = window.down('form').getForm()
    record = form.getRecord()
    values = form.getValues()
    store =  window.getStore()

    if !this.validateForm(form)
      return

    if (!record)
      record = Ext.create('ISPBClient.model.City')
      record.set(values)
      store.add(record)
    else
      record.set(values)

    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()