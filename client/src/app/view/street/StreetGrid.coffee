Ext.define 'ISPBClient.view.street.StreetGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.StreetGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Город', dataIndex: 'cityName', flex: 1}
    {header: 'Улица', dataIndex: 'name', flex: 1}
  ]

  features: [Ext.create('Ext.grid.feature.Grouping', {groupHeaderTpl: 'Город  {name} ({rows.length})' })]

  store:
    model: 'ISPBClient.model.Street'
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
            ]
        }
      ]
    }
  ]
