Ext.define 'ISPBClient.view.tariff.EditTariffRadiusAttributeWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editTariffRadiusAttributeWindow'

  title: 'Атрибуты RADIUS'
  height: 400
  width: 500
  layout:'border'
  autoShow: false

  config:
    tariff: null


  items: [
    {
      xtype: 'panel'
      region: 'north'

      layout:
        type: 'vbox'
        align: 'stretch'

      border: null

      items: [
        {
          xtype: 'displayfield'
          fieldLabel: 'Тариф'
          name: 'tariffLabel'
          margin: '5 5 5 5'
          value: ''
          labelWidth: 50
        }
      ]
    }
    {
      xtype: 'gridpanel'
      region: 'center'

      columns: [
        {xtype: 'rownumberer'}
        {header: 'Атрибут', dataIndex: 'attributeName', flex: 1}
        {header: 'Значение', dataIndex: 'attributeValue', flex: 1}
        {header: 'Условие', dataIndex: 'condition', flex: 1}
      ]

      store:
        model: 'ISPBClient.model.TariffRadiusAttribute'
        autoLoad: false
        remoteFilter: true
        remoteSort: true


      dockedItems: [
        xtype: 'toolbar'
        dock: 'top'
        items:[
          {
            text: 'Добавить'
            icon:'static/img/add.png'
            action: 'add'
          }
          '-'
          {
            text: 'Удалить'
            icon: 'static/img/delete.gif'
            action: 'delete'
          }
        ]
      ]

    }
  ]

  listeners:
    show: (element) ->
      tariff = element.getTariff()
      element.down('displayfield[name=tariffLabel]').setValue(tariff.get('name'))
      store = element.down('gridpanel').getStore()
      store.filter("tariffId__eq", tariff.get('id'))


