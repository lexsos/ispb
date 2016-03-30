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

      'PaymentGrid button[action=edit]':
        click: this.onEditClick

      'PaymentGrid':
        itemdblclick: this.onItemDblClick


  onRefreshClick: (element)->
      element.up('grid').getStore().load()

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onShowPaymentsClick: (element)->
    paymentGroup = this.getSelectedRecord(element)
    if paymentGroup
      this.showPaymentsInGroup(paymentGroup)

  onItemDblClick: (grid, record) ->
    this.showPaymentsInGroup(record)

  showPaymentsInGroup: (paymentGroup) ->
    view = Ext.widget('showPaymentsInGroupWindow')
    view.setPaymentGroup(paymentGroup)
    store = view.down('grid').getStore()
    store.filter("groupId__eq", paymentGroup.get('id'))
    view.show()

  onEditClick: (element) ->
    paymentGroup = this.getSelectedRecord(element)
    if paymentGroup
      view = Ext.widget('editPaymentGroupWindow')
      store = element.up('grid').getStore()
      view.setStore(store)
      view.down('form').loadRecord(paymentGroup)
      view.show()


