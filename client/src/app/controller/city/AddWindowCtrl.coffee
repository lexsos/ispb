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
    form   = window.down('form')
    record = form.getRecord()
    values = form.getValues()
    record.set(values)
    window.close()