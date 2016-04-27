Ext.define 'ISPBClient.controller.radiusAuth.RadiusAuthGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['radiusAuth.RadiusAuthGrid']
  models: ['RadiusAuth']

  config:
    gridWidget: 'RadiusAuthGrid'
    editWindowWidget: 'editRadiusAuthWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить запись {name}?'
    deleteFieldName: 'contractNumber'

  init: ->
    this.callParent();

    this.control
      'RadiusAuthGrid button[action=attributes]':
        click: this.onAttributesClick

  onAttributesClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('editRadiusAuthAttributeWindow')
      view.setRadiusAuth(record)
      view.show()