Ext.define 'ISPBClient.controller.customer.AddCustomerVirtualPaymentWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.AddCustomerVirtualPaymentWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'addCustomerVirtualPaymentWindow button[action=cancel]':
        click: this.onCancelClick

      'addCustomerVirtualPaymentWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    customerId = window.getCustomer().get('id')
    sum = window.down('numberfield[name=paymentSum]').getValue()
    dateUntil = window.down('datefield[name=dateUntil]').getValue()
    dateUntil = Ext.Date.format(dateUntil, 'Y-m-d H:i:s')
    comment = 'Виртуальный платеж по договору ' + window.getCustomer().get('contractNumber') + ' сроком до ' + dateUntil

    if ISPBClient.utils.RpcProcedure.addVirtualPayment(customerId, sum, dateUntil, comment)
      window.close()
    else
      Ext.MessageBox.alert 'Ошибка', 'Не удалось создать виртуальный платеж!'

