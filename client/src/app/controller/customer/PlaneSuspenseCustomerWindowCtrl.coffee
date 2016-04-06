Ext.define 'ISPBClient.controller.customer.PlaneSuspenseCustomerWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.PlaneSuspenseCustomerWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'planeSuspenseCustomerWindow button[action=cancel]':
        click: this.onCancelClick

      'planeSuspenseCustomerWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    customerId = window.getCustomer().get('id')
    dateSuspend = Ext.Date.format(window.down('datefield[name=dateSuspend]').getValue(), 'Y-m-d H:i:s')
    dateResume = Ext.Date.format(window.down('datefield[name=dateResume]').getValue(), 'Y-m-d H:i:s')

    if ISPBClient.utils.RpcProcedure.planeSuspenseCustomer(customerId, dateSuspend, dateResume)
      window.close()
    else
      Ext.MessageBox.alert 'Ошибка', 'Не удалось запланировать приостановку!'