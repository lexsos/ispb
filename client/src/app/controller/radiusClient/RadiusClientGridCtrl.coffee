Ext.define 'ISPBClient.controller.radiusClient.RadiusClientGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['radiusClient.RadiusClientGrid']
  models: ['RadiusClient']

  config:
    gridWidget: 'RadiusClientGrid'
    editWindowWidget: 'editRadiusClientWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить устройство {name}?'
    deleteFieldName: 'ip4Address'

  init: ->
    this.callParent();

    this.control
      'RadiusClientGrid button[action=parameters]':
        click: this.onParametersClick

  onParametersClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('editRadiusClientParametersWindow')
      view.setRadiusClient(record)
      view.show()