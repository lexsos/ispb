Ext.define 'ISPBClient.controller.city.AddWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['city.AddWindow']

  init: ->
    this.control
      'addCityWindow button[action=cancel]':
        click: this.onCancelClick

      'addCityWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (btn) ->
    btn.up('addCityWindow').close()

  onSaveClick: (btn) ->
    window = btn.up('addCityWindow')
    form   = window.down('form').getForm()
    record = form.getRecord()
    values = form.getValues()
    store = Ext.widget('CityGrid').getStore()

    if !form.isValid()
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