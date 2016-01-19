Ext.define 'ISPBClient.utils.DictionaryGridCtrl',
  extend: 'Ext.app.Controller'

  config:
    gridWidget: null
    editWindowWidget: null
    deleteMessageTmpl: null
    deleteFieldName: null

  init: ->
    this.control this.getGridWidget() + ' button[action=refresh]',
      click: this.onRefreshClick

    this.control this.getGridWidget() + ' button[action=add]',
      click: this.onAddClick

    this.control this.getGridWidget() + ' button[action=edit]',
      click: this.onEditClick

    this.control this.getGridWidget() + ' button[action=delete]',
      click: this.onDeleteClick

    this.control this.getGridWidget(),
      itemdblclick: this.onItemDblClick

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onRefreshClick: (element)->
    element.up('grid').getStore().load()

  onAddClick: (element)->
    view = Ext.widget(this.getEditWindowWidget())
    view.setStore(element.up('grid').getStore())

  onEditClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget(this.getEditWindowWidget())
      view.down('form').loadRecord(record)
      view.setStore(element.up('grid').getStore())

  onItemDblClick: (grid, record) ->
    view = Ext.widget(this.getEditWindowWidget())
    view.down('form').loadRecord(record);
    view.setStore(grid.getStore())

  onDeleteClick: (element) ->
    record = this.getSelectedRecord(element)
    store = element.up('grid').getStore()
    if record
      tpl = new Ext.XTemplate(this.getDeleteMessageTmpl())
      msg = tpl.apply
        name: record.get(this.getDeleteFieldName())
      Ext.MessageBox.confirm 'Удаление', msg, (btn) ->
        if btn == 'yes'
          store.remove(record)
          store.sync()