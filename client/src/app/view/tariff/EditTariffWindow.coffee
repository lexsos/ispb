Ext.define 'ISPBClient.view.tariff.EditTariffWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editTariffWindow'

  title: 'Тариф'
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
          name : 'name'
          fieldLabel: 'Название тарифа'
          margin: '5 5 5 5'
          labelWidth: 150
        }
        {
          xtype: 'numberfield'
          name : 'dailyPayment'
          fieldLabel: 'Ежедневный платеж'
          margin: '5 5 5 5'
          value: 0
          minValue: 0
          labelWidth: 150
        }
        {
          xtype: 'checkboxfield'
          name : 'autoDailyPayment'
          fieldLabel: 'Автоматически начислять абонентскую плату'
          margin: '5 5 5 5'
          checked: true
          value: true
          inputValue: 'true',
          uncheckedValue: 'false'
          labelWidth: 150
        }
        {
          xtype: 'numberfield'
          name : 'offThreshold'
          fieldLabel: 'Порог отключения'
          margin: '5 5 5 5'
          value: 0
          labelWidth: 150
        }
      ]
    }
  ]