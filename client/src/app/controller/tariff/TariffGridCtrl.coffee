Ext.define 'ISPBClient.controller.tariff.TariffGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['tariff.TariffGrid']
  models: ['Tariff']

  config:
    gridWidget: 'TariffGrid'
    editWindowWidget: 'editTariffWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить тариф {name}?'
    deleteFieldName: 'name'
