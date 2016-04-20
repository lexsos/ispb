Ext.define 'ISPBClient.controller.tariff.EditTariffRadiusAttributeWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['tariff.EditTariffRadiusAttributeWindow']
  models: ['TariffRadiusAttribute']

  init: ->
    this.control
      'editTariffRadiusAttributeWindow button[action=add]':
        click: this.onAddClick

      'editTariffRadiusAttributeWindow button[action=delete]':
        click: this.onDeleteClick

      'editTariffRadiusAttributeWindow button[action=save]':
        click: this.onSaveClick

      'editTariffRadiusAttributeWindow button[action=cancel]':
        click: this.onCancelClick

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onAddClick: (element) ->
    store = element.up('grid').getStore()
    tariffId = element.up('window').getTariff().getId()

    record = Ext.create('ISPBClient.model.TariffRadiusAttribute')
    record.set('tariffId', tariffId)
    record.set('attributeName', 'name')
    record.set('attributeValue', 'value')
    record.set('condition', 'ALWAYS')

    store.add(record)

  onDeleteClick: (element) ->
    record = this.getSelectedRecord(element)
    store = element.up('grid').getStore()
    if (record)
      store.remove(record)

  onSaveClick: (element) ->
    store = element.up('window').down('grid').getStore()
    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить изменения!'
    element.up('window').close()

  onCancelClick: (element) ->
    element.up('window').close()
