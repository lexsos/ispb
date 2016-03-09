Ext.define 'ISPBClient.view.tariff.TariffGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.TariffGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Название', dataIndex: 'name', flex: 1}
    {
      header: 'Ежедневный платеж'
      dataIndex: 'dailyPayment'
      flex: 1
      renderer: (v) ->
        formated = Ext.util.Format.number(v, '0,000.00') + 'р.'
        return formated
    }
    {
      header: 'Порог отключения'
      dataIndex: 'offThreshold'
      flex: 1
      renderer: (v) ->
        formated = Ext.util.Format.number(v, '0,000.00') + 'р.'
        return formated
    }
  ]

  store:
    model: 'ISPBClient.model.Tariff'
    autoLoad: true

  dockedItems: [
    {
      xtype: 'toolbar'
      dock: 'top'
      items:[
        {
          text: 'Обновить'
          icon: 'static/img/table_refresh.png'
          action: 'refresh'
        }
        '-'
        {
          text: 'Добавить'
          icon:'static/img/add.png'
          action: 'add'
        }
        '-'
        {
          text: 'Редактировать'
          icon:'static/img/edit.png'
          action: 'edit'
        }
        '-'
        {
          text: 'Удалить'
          icon: 'static/img/delete.gif'
          action: 'delete'
        }
      ]
    }
  ]
