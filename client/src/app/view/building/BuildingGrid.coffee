Ext.define 'ISPBClient.view.building.BuildingGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.BuildingGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Город', dataIndex: 'cityName', flex: 1}
    {header: 'Улица', dataIndex: 'streetName', flex: 1}
    {header: 'Здание', dataIndex: 'name', flex: 1}
  ]

  features: [Ext.create('Ext.grid.feature.Grouping', {groupHeaderTpl: '{name}' })]

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
          fieldLabel: 'Группировать по'
          xtype: 'combobox'
          queryMode: 'local'
          displayField: 'name'
          valueField: 'key'
          action: 'groupBy'
          store:
            fields: ['key', 'name']
            data:[
              {'key':null, 'name':'Без группировки'}
              {'key':'cityName', 'name':'Город'}
              {'key':'qualifiedStreetName', 'name':'Улица'}
            ]
        }
      ]
    }
  ]