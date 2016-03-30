Ext.define 'ISPBClient.view.payment.EditPaymentGroupWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editPaymentGroupWindow'

  title: 'Начисления'
  layout: 'fit'
  autoShow: false
  resizable: false

  config:
    store: null

  buttons: [
    {
      text: 'Сохранить'
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
      items: [
        {
          xtype: 'displayfield',
          fieldLabel: 'Дата создания',
          name: 'createAt',
          margin: '5 5 5 5'
          renderer: (value, field) ->
            return Ext.util.Format.date(value, 'Y-m-d H:i:s')
        }
        {
          xtype: 'textfield'
          name : 'comment'
          fieldLabel: 'Комментарий'
          margin: '5 5 5 5'
          width: 450
        }
      ]
    }
  ]