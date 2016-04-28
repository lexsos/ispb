Ext.define 'ISPBClient.view.radiusClient.EditRadiusClientWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editRadiusClientWindow'

  title: 'клиент RADIUS'
  layout: 'fit'
  autoShow: true

  config:
    store: null

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
      xtype: 'form'
      trackResetOnLoad: true
      items:[
        {
          xtype: 'textfield'
          name : 'ip4Address'
          fieldLabel: 'IPv4 адрес устройства NAS/BRAS'
          margin: '5 5 5 5'
          labelWidth: 200
        }
        {
          xtype: 'textfield'
          name : 'secret'
          fieldLabel: 'Общий пароль'
          margin: '5 5 5 5'
          labelWidth: 200
        }
        {
          xtype: 'checkboxfield'
          name : 'addAuthRequest'
          fieldLabel: 'Создавать запросы авторизации'
          margin: '5 5 5 5'
          checked: true
          value: true
          inputValue: 'true',
          uncheckedValue: 'false'
          labelWidth: 200
        }
        {
          xtype: 'checkboxfield'
          name : 'rejectInactive'
          fieldLabel: 'Отвергать заблокированные'
          margin: '5 5 5 5'
          checked: true
          value: true
          inputValue: 'true',
          uncheckedValue: 'false'
          labelWidth: 200
        }
        {
          xtype: 'combobox'
          name : 'clientType'
          fieldLabel: 'Тип NAS/BRAS устройства'
          margin: '5 5 5 5'
          displayField: 'name'
          valueField: 'key'
          typeAhead: true
          value: 'DEFAULT'
          labelWidth: 200

          store:
            fields: ['key', 'name']
            data : [
              {'key': 'DEFAULT', name: 'Default'}
            ]
        }
      ]
    }
  ]