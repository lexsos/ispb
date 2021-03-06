Ext.define 'ISPBClient.controller.customer.CustomerGridCtrl',
  extend: 'Ext.app.Controller'

  views: ['customer.CustomerGrid']
  models: ['Customer']
  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->

    this.control
      'CustomerGrid button[action=refresh]':
        click: this.onRefreshClick

      'CustomerGrid button[action=edit]':
        click: this.onEditClick

      'CustomerGrid button[action=add]':
        click: this.onAddClick

      'CustomerGrid button[action=delete]':
        click: this.onDeleteClick

      'CustomerGrid menuitem[action=showPayments]':
        click: this.onShowPaymentsClick

      'CustomerGrid menuitem[action=addVirtualPayment]':
        click: this.onAddVirtualPaymentClick

      'CustomerGrid menuitem[action=addPayment]':
        click: this.onAddPaymentClick

      'CustomerGrid menuitem[action=changeTariff]':
        click: this.onChangeTariffClick

      'CustomerGrid menuitem[action=showTariffHistory]':
        click: this.onShowTariffHistoryClick

      'CustomerGrid menuitem[action=showStatusHistory]':
        click: this.onShowStatusHistoryClick

      'CustomerGrid menuitem[action=activateCustomer]':
        click: this.onActivateCustomerClick

      'CustomerGrid menuitem[action=deactivateCustomer]':
        click: this.onDeactivateCustomerClick

      'CustomerGrid menuitem[action=suspenseCustomer]':
        click: this.onSuspenseCustomerClick

      'CustomerGrid combobox[action=filterBy]':
        change: this.onFilterByChange

      'CustomerGrid toolbar[filterType=contractNumber] button[action=filterApply]':
        click: this.onFilerByContractNumber

      'CustomerGrid toolbar[filterType=address] combobox[filterField=cityIdValue]':
        change: this.onCitySelect

      'CustomerGrid toolbar[filterType=address] combobox[filterField=streetIdValue]':
        change: this.onStreetSelect

      'CustomerGrid toolbar[filterType=address] button[action=filterApply]':
        click: this.onFilerByAddress

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onRefreshClick: (element)->
    element.up('grid').getStore().load()

  onEditClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('editCustomerWindow')
      view.down('form').loadRecord(record)
      view.setStore(element.up('grid').getStore())

  onAddClick: (element) ->
    view = Ext.widget('addCustomerWindow')
    view.setStore(element.up('grid').getStore())

  onDeleteClick: (element) ->
    record = this.getSelectedRecord(element)
    store = element.up('grid').getStore()
    if record
      Ext.MessageBox.confirm 'Удаление', 'Удалить абонента с номером договора ' + record.get('contractNumber'), (btn) ->
        if btn == 'yes'
          store.remove(record)
          store.sync()

  onShowPaymentsClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('showPaymentsCustomerWindow')

      contractNumber = record.get('contractNumber')
      view.down('label[name=contractNumberLabel]').setText('Номер договора: ' + contractNumber)

      qualifiedName = record.get('qualifiedName')
      view.down('label[name=qualifiedNameLabel]').setText('ФИО абонента: ' + qualifiedName)

      store = view.down('grid').getStore()
      store.filter("customerId__eq", record.getId())

  onAddVirtualPaymentClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('addCustomerVirtualPaymentWindow')
      view.setCustomer(record)
      view.show()

  onAddPaymentClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('addCustomerPaymentWindow')
      view.setCustomer(record)
      view.show()

  onFilterByChange: (element, newValue) ->
    parent = element.up('toolbar')
    for i, filter of Ext.ComponentQuery.query('toolbar[filterType]', parent)
      filter.hide()

    if newValue
      parent.down('toolbar[filterType=' + newValue + ']').show()
    else
      element.up('grid').getStore().clearFilter()

  onFilerByContractNumber: (element) ->
    store = element.up('grid').getStore()
    value = element.up('grid').down('toolbar[filterType=contractNumber] textfield[filterField=contractNumberValue]').getValue()
    if value
      store.clearFilter(true)
      store.filter("contractNumber__like", value + '%')

  onCitySelect: (element, newValue) ->
    streetCombo = element.up('toolbar').down('combobox[filterField=streetIdValue]')
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')

    streetCombo.clearValue()
    buildingCombo.clearValue()

    if newValue
      streetStore = streetCombo.getStore()
      streetStore.clearFilter(true)
      streetStore.filter("cityId__eq", newValue)

  onStreetSelect: (element, newValue) ->
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')
    buildingCombo.clearValue()

    if newValue
      buildingStore = buildingCombo.getStore()
      buildingStore.clearFilter(true)
      buildingStore.filter("streetId__eq", newValue)

  onFilerByAddress: (element) ->
    cityCombo = element.up('toolbar').down('combobox[filterField=cityIdValue]')
    streetCombo = element.up('toolbar').down('combobox[filterField=streetIdValue]')
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')

    store = element.up('grid').getStore()

    if buildingCombo.getValue()
      store.clearFilter(true)
      store.filter("buildingId__eq", buildingCombo.getValue())
    else if streetCombo.getValue()
      store.clearFilter(true)
      store.filter("streetId__eq", streetCombo.getValue())
    else if cityCombo.getValue()
      store.clearFilter(true)
      store.filter("cityId__eq", cityCombo.getValue())

  onChangeTariffClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('assignTariffCustomerWindow')
      view.setCustomer(record)
      view.show()

  onShowTariffHistoryClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('showAssignTariffCustomerWindow')
      view.setCustomer(record)
      view.show()

      store = view.down('grid').getStore()
      store.filter("customerId__eq", record.getId())

  onShowStatusHistoryClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('showCustomerStatusWindow')
      view.setCustomer(record)
      view.show()

      store = view.down('grid').getStore()
      store.filter("customerId__eq", record.getId())

  setCustomerStatus: (customerId, status) ->
    if !ISPBClient.utils.RpcProcedure.setCustomerStatus(customerId, status)
      Ext.MessageBox.alert 'Ошибка', 'Не удалось изменить статус!'

  onActivateCustomerClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      contractNumber = record.get('contractNumber')
      customerId = record.get('id')
      Ext.MessageBox.confirm 'Активация', 'Вы действительно желатие активировать абонента с номером договора ' + contractNumber, (btn) =>
        if btn == 'yes'
          this.setCustomerStatus(customerId, 'ACTIVE')

  onDeactivateCustomerClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      contractNumber = record.get('contractNumber')
      customerId = record.get('id')
      Ext.MessageBox.confirm 'Отключение', 'Вы действительно желатие отключить абонента с номером договора ' + contractNumber, (btn) =>
        if btn == 'yes'
          this.setCustomerStatus(customerId, 'INACTIVE')

  onSuspenseCustomerClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('planeSuspenseCustomerWindow')
      view.setCustomer(record)
      view.show()