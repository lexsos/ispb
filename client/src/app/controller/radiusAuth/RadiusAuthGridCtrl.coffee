Ext.define 'ISPBClient.controller.radiusAuth.RadiusAuthGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['radiusAuth.RadiusAuthGrid']
  models: ['RadiusAuth']

  config:
    gridWidget: 'RadiusAuthGrid'
    editWindowWidget: 'editRadiusAuthWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить запись {name}?'
    deleteFieldName: 'contractNumber'
