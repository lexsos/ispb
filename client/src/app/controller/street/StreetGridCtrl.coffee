Ext.define 'ISPBClient.controller.street.StreetGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['street.StreetGrid']
  models: ['Street']

  config:
    gridWidget: 'StreetGrid'
    editWindowWidget: 'editStreetWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить улицу {name}?'
    deleteFieldName: 'name'
