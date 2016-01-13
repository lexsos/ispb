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

  validateForm: (from) ->
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