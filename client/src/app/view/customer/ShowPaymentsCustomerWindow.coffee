Ext.define 'ISPBClient.view.customer.ShowPaymentsCustomerWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.showPaymentsCustomerWindow'

  title: 'Платежи'
  layout: 'fit'
  autoShow: true

  height: 600,
  width: 800,
  layout:'border'

  maximizable: true

  items: [
    {
      xtype: 'panel'
      region: 'north'

      layout:
        type: 'vbox'
        align: 'stretch'

      border: null

      items: [
        {
          xtype: 'label'
          text: 'Номер договора:'
          margin: '5 0 0 5'
          name: 'contractNumberLabel'
        }
        {
          xtype: 'label'
          text: 'ФИО абонента:'
          margin: '5 0 5 5'
          name: 'qualifiedNameLabel'
        }
      ]
    }
    {
      xtype: 'gridpanel'
      region: 'center'

      store:
        model: 'ISPBClient.model.Payment'
        autoLoad: false
        remoteFilter: true
        remoteSort: true

      columns: [
        {xtype: 'rownumberer'}
        {header: 'Дата проводки', dataIndex: 'applyAt', width: 150, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')}
        {header: 'Коментарий', dataIndex: 'paymentGroupComment', flex: 1}
        {
          header: 'Сумма'
          dataIndex: 'paymentSum'
          width: 100
          align: 'right'
          renderer: (v) ->
            formated = Ext.util.Format.number(v, '0,000.00') + 'р.'
            return formated
        }
        {
          header: 'Статус'
          dataIndex: 'processed'
          width: 50
          renderer: (v) ->
            if v
              return '<img src="static/img/saved.png">'
            return ''
        }
      ]

      dockedItems: [
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
    }
  ]