Ext.define 'ISPBClient.view.radiusAuth.RadiusAuthGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.RadiusAuthGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Логин', dataIndex: 'comment', flex: 1}
    {header: 'Номер договора', dataIndex: 'comment', flex: 1}
    {header: 'ФИО', dataIndex: 'comment', flex: 1}
    {header: 'Адрес подключения', dataIndex: 'comment', flex: 1}
    {header: 'IPv4', dataIndex: 'comment', flex: 1}
    {header: 'Дата создания', dataIndex: 'comment', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.RadiusAuth'
    autoLoad: true
    remoteFilter: true
    remoteSort: true

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
          text: 'RADIUS атрибуты'
          icon:'static/img/application_view_list.png'
          action: 'attributes'
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
      displayMsg: 'Платежи {0} - {1} из {2}'

      listeners:
        added: (element) ->
          store = element.up('grid').getStore()
          element.bindStore(store)
    }
  ]