Ext.define 'ISPBClient.view.customer.CustomerGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.CustomerGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Договор', dataIndex: 'contractNumber', flex: 1}
    {header: 'ФИО', dataIndex: 'qualifiedName', flex: 1}
    {header: 'Адрес', dataIndex: 'qualifiedAddress', flex: 1}
    {header: 'Телефон', dataIndex: 'phone', flex: 1}
    {header: 'Баланс', dataIndex: 'balance', flex: 1}
  ]

  store:
    model: 'ISPBClient.model.Customer'
    autoLoad: true
    remoteFilter: true
    remoteSort: true

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
          text: 'Платежи'
          icon:'static/img/money.png'
          action: 'showPayments'
        }
      ]
    }

    {
      xtype: 'pagingtoolbar',
      dock: 'bottom',
      displayInfo: true,
      beforePageText: 'Страница',
      afterPageText: 'из {0}',
      displayMsg: 'Абоненты {0} - {1} из {2}'

      listeners:
        added: (element) ->
          store = element.up('grid').getStore()
          element.bindStore(store)
    }

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
              {'key': null, 'name':'Без фильтрации'}
              {'key':'address', 'name':'адресу'}
              {'key':'contractNumber', 'name':'номеру договора'}
            ]
        }

        # Фильтр по адресу
        {
          xtype: 'toolbar'
          hidden: true
          border: false
          filterType: 'address'
          items: [
            {
              xtype: 'combobox'
              width: 100
              displayField: 'name'
              valueField: 'id'
              filterField: 'cityIdValue'
              store:
                model: 'ISPBClient.model.City'
                autoLoad: false
                remoteFilter: true
            }
            '-'
            {
              xtype: 'combobox'
              width: 150
              displayField: 'name'
              valueField: 'id'
              filterField: 'streetIdValue'
              store:
                model: 'ISPBClient.model.Street'
                autoLoad: false
                remoteFilter: true
            }
            '-'
            {
              xtype: 'combobox'
              width: 60
              displayField: 'name'
              valueField: 'id'
              filterField: 'buildingIdValue'
              store:
                model: 'ISPBClient.model.Building'
                autoLoad: false
                remoteFilter: true
            }
            '-'
            {
              text: 'Применить'
              icon: 'static/img/filtered.gif'
              action: 'filterApply'
            }
          ]
        }

        # Фильтр по номеру договора
        {
          xtype: 'toolbar'
          hidden: true
          border: false
          filterType: 'contractNumber'
          items: [
            {
              xtype: 'textfield'
              width: 130
              filterField: 'contractNumberValue'
            }
            '-'
            {
              text: 'Применить'
              icon: 'static/img/filtered.gif'
              action: 'filterApply'
            }
          ]
        }
      ]
    }
  ]