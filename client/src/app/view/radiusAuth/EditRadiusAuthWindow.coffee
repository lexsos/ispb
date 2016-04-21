Ext.define 'ISPBClient.view.radiusAuth.EditRadiusAuthWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editRadiusAuthWindow'

  title: 'RADIUS авторизация'
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
        items: [
          {
            xtype: 'textfield'
            name : 'userName'
            fieldLabel: 'Логин'
            margin: '5 5 5 5'
            width: 400
          }
          {
            xtype: 'textfield'
            name : 'password'
            fieldLabel: 'Пароль'
            margin: '5 5 5 5'
            width: 400
          }
          {
            xtype: 'textfield'
            name : 'ip4Address'
            fieldLabel: 'IPv4 адрес'
            margin: '5 5 5 5'
            width: 400
          }
          {
            xtype: 'combobox'
            name : 'cityId'
            fieldLabel: 'Город'
            margin: '5 5 5 5'
            displayField: 'name',
            valueField: 'id',
            typeAhead: true
            width: 400
            store:
              model: 'ISPBClient.model.City'
              autoLoad: true
          }
          {
            xtype: 'combobox'
            name : 'streetId'
            fieldLabel: 'Улица'
            margin: '5 5 5 5'
            displayField: 'name',
            valueField: 'id',
            typeAhead: true
            width: 400
            store:
              model: 'ISPBClient.model.Street'
              autoLoad: false
              remoteFilter: true
          }
          {
            xtype: 'combobox'
            name : 'buildingId'
            fieldLabel: 'Дом'
            margin: '5 5 5 5'
            displayField: 'name',
            valueField: 'id',
            typeAhead: true
            width: 400
            store:
              model: 'ISPBClient.model.Building'
              autoLoad: false
              remoteFilter: true
          }
          {
            xtype: 'combobox'
            name : 'customerId'
            fieldLabel: 'Абонент'
            margin: '5 5 5 5'
            displayField: 'contractNumber',
            valueField: 'id',
            typeAhead: true
            width: 400
            store:
              model: 'ISPBClient.model.Customer'
              autoLoad: false
              remoteFilter: true
            listConfig: {
              getInnerTpl: () ->
                return '<b>{contractNumber}</b> {qualifiedName}'
            }
          }
        ]
     }
  ]