Ext.define 'ISPBClient.controller.city.CityGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['city.CityGrid']
  models: ['City']

  config:
    gridWidget: 'CityGrid'
    editWindowWidget: 'addCityWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить город {name}?'
    deleteFieldName: 'name'
