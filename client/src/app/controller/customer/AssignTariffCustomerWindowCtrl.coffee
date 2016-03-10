Ext.define 'ISPBClient.controller.customer.AssignTariffCustomerWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.AssignTariffCustomerWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'assignTariffCustomerWindow button[action=cancel]':
        click: this.onCancelClick

      'assignTariffCustomerWindow button[action=save]':
        click: this.onSaveClick

  onCancelClick: (element) ->
    element.up('window').close()

  onSaveClick: (element) ->
    window = element.up('window')
    customerId = window.getCustomer().get('id')
    tariffIdField = window.down('combobox[name=tariffId]')
    tariffId = tariffIdField.getValue()
    dateFrom = window.down('datefield[name=dateFrom]').getValue()
    dateFrom = Ext.Date.format(dateFrom, 'Y-m-d H:i:s')

    if !tariffId
      tariffIdField.markInvalid('Укажите тариф')
      return

    if ISPBClient.utils.RpcProcedure.assignTariff(customerId, tariffId, dateFrom)
      window.close()
    else
      Ext.MessageBox.alert 'Ошибка', 'Не удалось назначить тариф!'