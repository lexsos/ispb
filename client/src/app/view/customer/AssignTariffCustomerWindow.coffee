Ext.define 'ISPBClient.view.customer.AssignTariffCustomerWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.assignTariffCustomerWindow'

  title: 'Смена тарифа'
  layout: 'fit'
  autoShow: false
  resizable: false

  config:
    customer: null

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
      border: false
      items: [
        {
          xtype: 'displayfield'
          fieldLabel: 'Номер договора'
          name: 'contractNumberLabel'
          margin: '5 5 5 5'
          value: '1'
        }
        {
          xtype: 'displayfield',
          fieldLabel: 'ФИО',
          name: 'qualifiedNameLabel',
          margin: '5 5 5 5'
          value: '1'
        }
        {
          xtype: 'combobox'
          name : 'tariffId'
          fieldLabel: 'Тариф'
          margin: '5 5 5 5'
          displayField: 'name',
          valueField: 'id',
          typeAhead: true
          store:
            model: 'ISPBClient.model.Tariff'
            autoLoad: true
        }
        {
          xtype: 'datefield'
          name : 'dateFrom'
          fieldLabel: 'Дата назначения тарифа'
          margin: '5 5 5 5'
          format: 'd.m.Y'
        }
      ]
    }
  ]

  listeners:
    show: (element) ->
      customer = element.getCustomer()
      element.down('displayfield[name=contractNumberLabel]').setValue(customer.get('contractNumber'))
      element.down('displayfield[name=qualifiedNameLabel]').setValue(customer.get('qualifiedName'))
      tomorrow = Ext.Date.add(new Date(), Ext.Date.DAY, 1)
      dateUntilField = element.down('datefield[name=dateFrom]')
      dateUntilField.setMinValue(tomorrow)
      dateUntilField.setValue(tomorrow)