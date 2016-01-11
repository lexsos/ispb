Ext.define 'ISPBClient.view.city.CityGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.CityGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Название', dataIndex: 'name', flex: 1}
  ]

  store: 'CityStore'

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
