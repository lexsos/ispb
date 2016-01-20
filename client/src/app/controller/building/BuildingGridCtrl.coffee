Ext.define 'ISPBClient.controller.building.BuildingGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['building.BuildingGrid']
  models: ['Building']

  config:
    gridWidget: 'BuildingGrid'
    editWindowWidget: 'editBuildingWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить здание {name}?'
    deleteFieldName: 'name'