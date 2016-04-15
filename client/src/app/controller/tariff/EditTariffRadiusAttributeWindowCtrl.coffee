Ext.define 'ISPBClient.controller.tariff.EditTariffRadiusAttributeWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['tariff.EditTariffRadiusAttributeWindow']
  models: ['TariffRadiusAttribute']

  init: ->
    this.control
      'editTariffRadiusAttributeWindow button[action=add]':
        click: this.onAddClick

  onAddClick: (element) ->
    store = element.up('grid').getStore()
    tariffId = element.up('window').getTariff().getId()

    record = Ext.create('ISPBClient.model.TariffRadiusAttribute')
    record.set('tariffId', tariffId)
    record.set('attributeName', 'name')
    record.set('attributeValue', 'value')
    record.set('condition', 'ALWAYS')

    store.add(record)
    store.sync()