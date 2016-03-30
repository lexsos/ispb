Ext.define 'ISPBClient.controller.payment.EditPaymentGroupWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['payment.EditPaymentGroupWindow']

  init: ->
    this.control
      'editPaymentGroupWindow button[action=cancel]':
        click: this.onCancelClick

      'editPaymentGroupWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    form   = window.down('form').getForm()
    store =  window.getStore()
    record = form.getRecord()
    values = form.getValues()

    if (record)
      record.set(values)

    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()