Ext.define 'ISPBClient.view.customer.CustomerGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.CustomerGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Договор', dataIndex: 'name', flex: 1}
    {header: 'ФИО', dataIndex: 'name', flex: 1}
    {header: 'Адрес', dataIndex: 'name', flex: 1}
    {header: 'Телефон', dataIndex: 'name', flex: 1}
    {header: 'Тариф', dataIndex: 'name', flex: 1}
    {header: 'Баланс', dataIndex: 'name', flex: 1}
    {header: 'Статус', dataIndex: 'name', flex: 1}
  ]


  dockedItems: [
    {
      xtype: 'toolbar'
      dock: 'top'
      items: [

        {
          fieldLabel: 'Фильтровать по'
          xtype: 'combobox'
          queryMode: 'local'
          displayField: 'name'
          valueField: 'key'
          action: 'filterBy'
          store:
            fields: ['key', 'name']
            data:[
              {'key':'address', 'name':'адресу'}
              {'key':'contractNumber', 'name':'номеру договора'}
              {'key':'', 'name':'баланс меньше'}
              {'key':'', 'name':'баланс больше'}
              {'key':'status', 'name':'статусу'}
              {'key':'', 'name':'тарифу'}
            ]
        }

        # Фильтр по адресу
        {
          xtype: 'toolbar'
          hidden: true
          filterType: 'address'
          items: [
            {
              xtype: 'combobox'
              width: 100
            }
            '-'
            {
              xtype: 'combobox'
              width: 150
            }
            '-'
            {
              xtype: 'combobox'
              width: 60
            }
            '-'
            {
              text: 'Применить'
              icon: 'static/img/filtered.gif'
            }
          ]
        }

        # Фильтр по номеру договора
        {
          xtype: 'toolbar'
          hidden: true
          filterType: 'contractNumber'
          items: [
            {
              xtype: 'textfield'
              width: 130
            }
            '-'
            {
              text: 'Применить'
              icon: 'static/img/filtered.gif'
            }
          ]
        }


      ]
    }
  ]