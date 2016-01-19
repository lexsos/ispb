Ext.define 'ISPBClient.controller.building.BuildingGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['building.BuildingGrid']
  models: ['Building']

  config:
    gridWidget: 'BuildingGrid'
    editWindowWidget: 'editBuildingWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить здание {name}?'
    deleteFieldName: 'name'

  init: ->
    this.superclass.init.call(this)

    this.control
      'BuildingGrid combobox[action=groupBy]':
        change: this.onGroupByChange

  onGroupByChange: (element, newValue) ->
    store = element.up('grid').getStore()
    if newValue
      store.group(newValue)
    else
      store.clearGrouping()