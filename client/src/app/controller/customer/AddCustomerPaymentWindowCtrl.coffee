Ext.define 'ISPBClient.controller.customer.AddCustomerPaymentWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.AddCustomerPaymentWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'addCustomerPaymentWindow button[action=cancel]':
        click: this.onCancelClick

      'addCustomerPaymentWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    customerId = window.getCustomer().get('id')
    sum = window.down('numberfield[name=paymentSum]').getValue()
    comment = window.down('textfield[name=comment]').getValue()
    if ISPBClient.utils.RpcProcedure.addPayment(customerId, sum, comment)
      window.close()
    else
      Ext.MessageBox.alert 'Ошибка', 'Не удалось создать платеж!'

