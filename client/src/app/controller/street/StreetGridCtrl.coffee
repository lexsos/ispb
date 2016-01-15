Ext.define 'ISPBClient.controller.street.StreetGridCtrl',
  extend: 'Ext.app.Controller'

  views: ['street.StreetGrid']
  models: ['Street']

  init: ->
    this.control
      'StreetGrid button[action=refresh]':
        click: this.onRefreshClick

      'StreetGrid button[action=add]':
        click: this.onAddClick

      'StreetGrid button[action=edit]':
        click: this.onEditClick

      'StreetGrid button[action=delete]':
        click: this.onDeleteClick

      'StreetGrid':
        itemdblclick: this.onItemDblClick

  getSelectedRecord: (control) ->
    grid = control.up('StreetGrid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onRefreshClick: (btn) ->
    btn.up('StreetGrid').getStore().load()

  onAddClick: (btn) ->
    view = Ext.widget('editStreetWindow')
    view.setStore(btn.up('StreetGrid').getStore())

  onEditClick: (btn) ->
    record = this.getSelectedRecord(btn)
    if record
      view = Ext.widget('editStreetWindow')
      view.down('form').loadRecord(record)
      view.setStore(btn.up('StreetGrid').getStore())

  onDeleteClick: (btn) ->
    record = this.getSelectedRecord(btn)
    store = btn.up('StreetGrid').getStore()
    if record
      Ext.MessageBox.confirm 'Удаление', 'Вы действительно хотите удалить улицу ' + record.get('name') + '?', (btn) ->
        if btn == 'yes'
          store.remove(record)
          store.sync()

  onItemDblClick: (grid, record) ->
    view = Ext.widget('editStreetWindow')
    view.down('form').loadRecord(record);
    view.setStore(grid.getStore())