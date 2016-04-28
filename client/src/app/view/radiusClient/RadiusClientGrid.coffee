Ext.define 'ISPBClient.view.radiusClient.RadiusClientGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.RadiusClientGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'IP4 адрес', dataIndex: 'ip4Address', flex: 1}
    {header: 'Тип NAS/BRAS устройства', dataIndex: 'clientType', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.RadiusClient'
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

    {
      xtype: 'pagingtoolbar',
      dock: 'bottom',
      displayInfo: true,
      beforePageText: 'Страница',
      afterPageText: 'из {0}',
      displayMsg: 'Клиенты {0} - {1} из {2}'

      listeners:
        added: (element) ->
          store = element.up('grid').getStore()
          element.bindStore(store)
    }
  ]
