Ext.define 'ISPBClient.controller.payment.PaymentGridCtrl',
  extend: 'Ext.app.Controller'

  views: ['payment.PaymentGrid']
  models: ['PaymentGroup']

  init: ->
    this.control
      'PaymentGrid button[action=refresh]':
        click: this.onRefreshClick

      'PaymentGrid button[action=showPayments]':
        click: this.onShowPaymentsClick


  onRefreshClick: (element)->
    element.up('grid').getStore().load()

  onShowPaymentsClick: (element)->
    view = Ext.widget('showPaymentsInGroupWindow')
    view.show()