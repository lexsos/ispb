Ext.define 'ISPBClient.view.payment.PaymentGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.PaymentGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Дата создания', dataIndex: 'createAt', flex: 1}
    {header: 'Комментарий', dataIndex: 'comment', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.PaymentGroup'
    autoLoad: true

  dockedItems: [
    {
      xtype: 'toolbar'
      dock: 'top'
      items: [
        {
          text: 'Обновить'
          icon: 'static/img/table_refresh.png'
          action: 'refresh'
        }
        '-'
        {
          text: 'Показать платежи'
          icon: 'static/img/grid.png'
          action: 'showPayments'
        }
      ]
    }

    {
      xtype: 'pagingtoolbar',
      dock: 'bottom',
      displayInfo: true,
      beforePageText: 'Страница',
      afterPageText: 'из {0}',
      displayMsg: 'Платежи {0} - {1} из {2}'

      listeners:
        added: (element) ->
          store = element.up('grid').getStore()
          element.bindStore(store)
    }

  ]