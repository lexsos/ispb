Ext.define 'ISPBClient.view.customer.ShowAssignTariffCustomerWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.showAssignTariffCustomerWindow'

  title: 'История назначения тарифов'
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
        {header: 'Дата назначения', dataIndex: 'applyAt', width: 150, renderer: Ext.util.Format.dateRenderer('d.m.Y H:i:s')}
        {header: 'Название тарифа', dataIndex: 'tariffName', flex: 1}
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

      store:
        model: 'ISPBClient.model.TariffAssignment'
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
          displayMsg: 'Платежи {0} - {1} из {2}'

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
