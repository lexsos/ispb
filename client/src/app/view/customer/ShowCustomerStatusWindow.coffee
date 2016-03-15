Ext.define 'ISPBClient.view.customer.ShowCustomerStatusWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.showCustomerStatusWindow'

  title: 'История отключений/включений'
  autoShow: false

  height: 600,
  width: 800,
  layout:'border'

  maximizable: true

  config:
    customer: null

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
          xtype: 'displayfield'
          fieldLabel: 'Номер договора:'
          margin: '5 0 0 5'
          name: 'contractNumberLabel'
        }
        {
          xtype: 'displayfield'
          fieldLabel: 'ФИО абонента:'
          margin: '5 0 5 5'
          name: 'qualifiedNameLabel'
        }
      ]
    }
    {
      xtype: 'gridpanel'
      region: 'center'

      columns: [
        {xtype: 'rownumberer'}
        {header: 'Дата обработки', dataIndex: 'applyAt', width: 150, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i:s')}
        {
          header: 'Описание'
          dataIndex: 'cause'
          flex: 1
          renderer: (v, metaData, record) ->
            status = record.get('status')
            cause = record.get('cause')
            if status == 'ACTIVE' && cause == 'MANAGER'
              return 'Активирован менеджером'
            else if status == 'INACTIVE' && cause == 'MANAGER'
              return 'Отключен менеджером'
            else if status == 'ACTIVE' && cause == 'SYSTEM_FINANCE'
              return 'Отключен системой по достижении порога отключения'
            else if status == 'INACTIVE' && cause == 'SYSTEM_FINANCE'
              return 'Активирован системой по зачислению средст на счет'
            return ''
        }
        {
          header: 'Статус'
          dataIndex: 'status'
          width: 50
          renderer: (v) ->
            if v == 'ACTIVE'
              return '<img src="static/img/green-led.gif">'
            else if v == 'INACTIVE'
              return '<img src="static/img/red-led.gif">'
            return ''
        }
        {
          header: 'Обработан'
          dataIndex: 'processed'
          width: 100
          renderer: (v) ->
            if v
              return '<img src="static/img/saved.png">'
            return ''
        }
      ]

      store:
        model: 'ISPBClient.model.CustomerStatus'
        autoLoad: false
        remoteFilter: true
        remoteSort: true

      dockedItems: [
        {
          xtype: 'pagingtoolbar',
          dock: 'bottom',
          displayInfo: true,
          beforePageText: 'Страница',
          afterPageText: 'из {0}',
          displayMsg: 'События {0} - {1} из {2}'

          listeners:
            added: (element) ->
              store = element.up('grid').getStore()
              element.bindStore(store)
        }
      ]
    }
  ]

  listeners:
    show: (element) ->
      customer = element.getCustomer()
      element.down('displayfield[name=contractNumberLabel]').setValue(customer.get('contractNumber'))
      element.down('displayfield[name=qualifiedNameLabel]').setValue(customer.get('qualifiedName'))