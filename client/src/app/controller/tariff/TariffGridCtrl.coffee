Ext.define 'ISPBClient.controller.tariff.TariffGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['tariff.TariffGrid']
  models: ['Tariff']

  config:
    gridWidget: 'TariffGrid'
    editWindowWidget: 'editTariffWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить тариф {name}?'
    deleteFieldName: 'name'

  init: ->
    this.callParent();

    this.control
      'TariffGrid button[action=attributes]':
        click: this.onAttributesClick

  onAttributesClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('editTariffRadiusAttributeWindow')
      view.setTariff(record)
      view.show()