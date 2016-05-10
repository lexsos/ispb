Ext.define 'ISPBClient.controller.radiusAuth.EditRadiusAuthAttributeWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['radiusAuth.EditRadiusAuthAttributeWindow']
  models: ['RadiusAuthAttribute', 'RadiusDictionary']


  init: ->
    this.control
      'editRadiusAuthAttributeWindow button[action=add]':
        click: this.onAddClick

      'editRadiusAuthAttributeWindow button[action=delete]':
        click: this.onDeleteClick

      'editRadiusAuthAttributeWindow button[action=save]':
        click: this.onSaveClick

      'editRadiusAuthAttributeWindow button[action=cancel]':
        click: this.onCancelClick

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onAddClick: (element) ->
    store = element.up('grid').getStore()
    userId = element.up('window').getRadiusAuth().getId()

    record = Ext.create('ISPBClient.model.RadiusAuthAttribute')
    record.set('userId', userId)
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
