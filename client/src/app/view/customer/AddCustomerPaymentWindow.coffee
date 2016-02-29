Ext.define 'ISPBClient.view.customer.AddCustomerPaymentWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.addCustomerPaymentWindow'

  title: 'Платеж\\Списание'
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
          width: 350
        }
        {
          xtype: 'textfield'
          name : 'comment'
          fieldLabel: 'Коментарий'
          margin: '5 5 5 5'
          width: 350
        }
      ]
    }
  ]

  listeners:
    show: (element) ->
      customer = element.getCustomer()
      element.down('displayfield[name=contractNumberLabel]').setValue(customer.get('contractNumber'))
      element.down('displayfield[name=qualifiedNameLabel]').setValue(customer.get('qualifiedName'))
      commentField = element.down('textfield[name=comment]')
      commentField.setValue('Платеж от ' + Ext.Date.format(new Date(), 'Y-m-d') + ' по доровору ' + customer.get('contractNumber'))