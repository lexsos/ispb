Ext.define 'ISPBClient.view.customer.PlaneSuspenseCustomerWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.planeSuspenseCustomerWindow'

  title: 'Запланировать приостановку'
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
          xtype: 'datefield'
          name : 'dateSuspend'
          fieldLabel: 'Дата отключения'
          margin: '5 5 5 5'
          format: 'd.m.Y H:i:s'
          listeners:
            change: (element, newValue) ->
              dateResumeField = element.up('form').down('datefield[name=dateResume]')
              dateResumeField.setMinValue(newValue)
        }
        {
          xtype: 'datefield'
          name : 'dateResume'
          fieldLabel: 'Дата включения'
          margin: '5 5 5 5'
          format: 'd.m.Y H:i:s'
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
      tomorrow = Ext.Date.clearTime(tomorrow)
      dateSuspendField = element.down('datefield[name=dateSuspend]')
      dateSuspendField.setMinValue(tomorrow)
      dateSuspendField.setValue(tomorrow)
      dateResumeField = element.down('datefield[name=dateResume]')
      dateResumeField.setMinValue(tomorrow)