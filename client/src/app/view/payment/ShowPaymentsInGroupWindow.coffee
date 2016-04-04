Ext.define 'ISPBClient.view.payment.ShowPaymentsInGroupWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.showPaymentsInGroupWindow'

  title: 'Платежи'
  autoShow: false

  height: 600,
  width: 800,
  layout:'border'

  maximizable: true

  config:
    paymentGroup: null

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
          fieldLabel: 'Дата создания'
          name: 'createAtLabel'
          margin: '5 5 5 5'
          value: '1'
        }
        {
          xtype: 'displayfield'
          fieldLabel: 'Коментарий'
          name: 'commentLabel'
          margin: '5 5 5 5'
          value: '1'
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
        {header: 'Дата проводки', dataIndex: 'applyAt', width: 150, renderer: Ext.util.Format.dateRenderer('d.m.Y H:i:s')}
        {header: 'Номер договора', dataIndex: 'customerContractNumber', flex: 1}
        {header: 'ФИО абенента', dataIndex: 'customerQualifiedName', flex: 1}
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

  listeners:
    show: (element) ->
      paymentGroup = element.getPaymentGroup()
      createDate = Ext.util.Format.date(paymentGroup.get('createAt'), 'd.m.Y H:i:s')
      element.down('displayfield[name=createAtLabel]').setValue(createDate)
      element.down('displayfield[name=commentLabel]').setValue(paymentGroup.get('comment'))