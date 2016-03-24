Ext.define 'ISPBClient.view.user.UserGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.UserGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Логин', dataIndex: 'login', flex: 1}
    {header: 'Имя', dataIndex: 'name', flex: 1}
    {header: 'Фамилия', dataIndex: 'surname', flex: 1}
    {
      header: 'Уровень доступа'
      dataIndex: 'accessLevel'
      flex: 1
      renderer: (v) ->
        if v == 100
          return "менеджер"
        else if v == 1000
          return "администратор"
        return v
    }
    {
      header: 'Включен'
      dataIndex: 'active'
      flex: 1
      renderer: (v) ->
        if v
          return '<img src="static/img/green-led.gif">'
        return '<img src="static/img/red-led.gif">'
    }
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