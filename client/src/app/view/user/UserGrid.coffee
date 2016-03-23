Ext.define 'ISPBClient.view.user.UserGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.UserGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Логин', dataIndex: 'cityName', flex: 1}
    {header: 'Имя', dataIndex: 'streetName', flex: 1}
    {header: 'Фамилия', dataIndex: 'name', flex: 1}
    {header: 'Уровень доступа', dataIndex: 'name', flex: 1}
    {header: 'Включен', dataIndex: 'name', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.User'
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