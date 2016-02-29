Ext.define 'ISPBClient.view.customer.AddCustomerVirtualPaymentWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.addCustomerVirtualPaymentWindow'

  title: 'Виртуальный платеж'
  layout: 'fit'
  autoShow: false
  resizable: false

  config:
    customer: null

  buttons: [
    {
      text: 'Добавить'
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
          xtype: 'numberfield'
          name : 'paymentSum'
          fieldLabel: 'Размер платежа'
          margin: '5 5 5 5'
          value: 0
          minValue: 0
        }
        {
          xtype: 'datefield'
          name : 'dateUntil'
          fieldLabel: 'Срок виртуального платежа'
          margin: '5 5 5 5'
          format: 'Y-m-d'
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
      dateUntilField = element.down('datefield[name=dateUntil]')
      dateUntilField.setMinValue(tomorrow)
      dateUntilField.setValue(tomorrow)