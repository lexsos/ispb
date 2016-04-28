Ext.define 'ISPBClient.controller.radiusClient.RadiusClientGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['radiusClient.RadiusClientGrid']
  models: ['RadiusClient']

  config:
    gridWidget: 'RadiusClientGrid'
    editWindowWidget: 'editRadiusClientWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить устройство {name}?'
    deleteFieldName: 'ip4Address'
