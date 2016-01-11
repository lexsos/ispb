Ext.define 'ISPBClient.controller.city.CityGridCtrl',
  extend: 'Ext.app.Controller'

  views: ['city.CityGrid']
  stores: ['CityStore']
  models: ['City']

  init: ->
    this.control
      'CityGrid button[action=refresh]':
        click: this.onRefreshClick

      'CityGrid button[action=add]':
        click: this.onAddClick

      'CityGrid button[action=edit]':
        click: this.onEditClick

      'CityGrid button[action=delete]':
        click: this.onDeletelick

      'CityGrid':
        itemdblclick: this.onItemDblClick

  getSelectedRecord: (control) ->
    grid = control.up('CityGrid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onItemDblClick: (grid, record) ->
    view = Ext.widget('addCityWindow')
    view.down('form').loadRecord(record);

  onAddClick: ->
    view = Ext.widget('addCityWindow')

  onEditClick: (btn) ->
    record = this.getSelectedRecord(btn)
    if record
      view = Ext.widget('addCityWindow')
      view.down('form').loadRecord(record)

  onDeletelick: (btn) ->
    record = this.getSelectedRecord(btn)
    store = this.getStore('CityStore')
    if record
      Ext.MessageBox.confirm 'Удаление', 'Вы действительно хотите удалить город ' + record.get('name') + '?', (btn) ->
        if btn == 'yes'
          store.remove(record)
          store.sync()

  onRefreshClick: (btn) ->
    this.getStore('CityStore').load()
