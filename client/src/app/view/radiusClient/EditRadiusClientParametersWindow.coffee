Ext.define 'ISPBClient.view.radiusClient.EditRadiusClientParametersWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editRadiusClientParametersWindow'

  title: 'Настройки RADIUS клиента'
  height: 400
  width: 600
  layout:'border'
  autoShow: false

  config:
    radiusClient: null

  buttons: [
    {
      text: 'Сохранить'
      action: 'save'
      icon:'static/img/save.gif'
    }
    {
      text: 'Отмена'
      action: 'cancel'
      icon:'static/img/cross.gif'
    }
  ]

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
          fieldLabel: 'RADIUS клиент:'
          name: 'radiusClientLabel'
          margin: '5 5 5 5'
          value: ''
          labelWidth: 100
        }
      ]
    }
    {
      xtype: 'gridpanel'
      region: 'center'

      columns: [
        {xtype: 'rownumberer'}
        {header: 'Парамерт', dataIndex: 'parameterName', flex: 1}
        {header: 'Значение', dataIndex: 'parameterValue', flex: 1}
      ]

      store:
        model: 'ISPBClient.model.RadiusClientParameter'
        autoLoad: false
        remoteFilter: true
        remoteSort: false

      dockedItems: [
        {
          xtype: 'toolbar'
          dock: 'top'
          items: [
            {
              text: 'Добавить'
              icon: 'static/img/add.png'
              action: 'add'
            }
            '-'
            {
              text: 'Удалить'
              icon: 'static/img/delete.gif'
              action: 'delete'
            }
            '-'
            {
              text: 'Изменить'
              icon: 'static/img/edit.png'
              action: 'change'
            }
          ]
        }
        {
          xtype: 'toolbar'
          dock: 'top'
          items: [
            {
              xtype: 'combobox'
              name : 'parameterName'
              allowBlank: false
              displayField: 'displayName'
              valueField: 'parameterName'
              typeAhead: true
              width: 250
              store:
                model: 'ISPBClient.model.RadiusClientParameterName'
                autoLoad: false
                remoteFilter: true
                remoteSort: false
            }
            '-'
            {
              xtype: 'combobox'
              name : 'parameterValue'
              allowBlank: false
              displayField: 'displayValue'
              valueField: 'parameterValue'
              typeAhead: true
              width: 250
              store:
                model: 'ISPBClient.model.RadiusClientParameterValue'
                autoLoad: false
                remoteFilter: true
                remoteSort: false
            }
          ]
        }
      ]

    }
  ]


  listeners:
    show: (element) ->
      radiusClient = element.getRadiusClient()
      element.down('displayfield[name=radiusClientLabel]').setValue(radiusClient.get('ip4Address'))

      store = element.down('gridpanel').getStore()
      store.filter("radiusClientId__eq", radiusClient.get('id'))

      parameterName = element.down('gridpanel combobox[name=parameterName]')
      parameterName.getStore().filter("clientIp__eq", radiusClient.get("ip4Address"))