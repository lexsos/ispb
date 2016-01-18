Ext.define 'ISPBClient.view.building.BuildingGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.BuildingGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Город', dataIndex: 'cityName', flex: 1}
    {header: 'Улица', dataIndex: 'streetName', flex: 1}
    {header: 'Здание', dataIndex: 'name', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.Building'
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