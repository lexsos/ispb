Ext.define 'ISPBClient.utils.DictionaryEditWindowCtrl',
  extend: 'Ext.app.Controller'

  config:
    modelClass: null
    windowWidget: null

  init: ->
    this.control this.getWindowWidget() + ' button[action=cancel]',
        click: this.onCancelClick

    this.control this.getWindowWidget() + ' button[action=save]',
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  validateForm: (form) ->
    return true

  onSaveClick: (element) ->
    window = element.up('window')
    form   = window.down('form').getForm()
    store =  window.getStore()
    record = form.getRecord()
    values = form.getValues()

    if !this.validateForm(form)
      return

    if (!record)
      record = Ext.create(this.getModelClass())
      record.set(values)
      store.add(record)
    else
      record.set(values)

    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить запись!'
    window.close()