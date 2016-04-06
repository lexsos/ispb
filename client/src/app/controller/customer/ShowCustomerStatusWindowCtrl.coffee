Ext.define 'ISPBClient.controller.customer.ShowCustomerStatusWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.ShowCustomerStatusWindow']
  models: ['CustomerStatus']

  init: ->
    this.control
      'showCustomerStatusWindow button[action=delete]':
        click: this.onDeleteClick

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onDeleteClick: (element)->
    record = this.getSelectedRecord(element)

    if !record
      return

    if record.get('processed')
      Ext.MessageBox.alert 'Ошибка', 'Данную запись удалить не возможно т.к. она уже обработана системой!'
      return

    Ext.MessageBox.confirm 'Удаление', 'Удалить выбранную запись?', (btn) ->
      if btn == 'yes'
        store = element.up('grid').getStore()
        store.remove(record)
        store.sync()