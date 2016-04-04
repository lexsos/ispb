Ext.define 'ISPBClient.view.customer.CustomerGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.CustomerGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Договор', dataIndex: 'contractNumber', width: 100}
    {header: 'ФИО', dataIndex: 'qualifiedName', flex: 1}
    {header: 'Адрес', dataIndex: 'qualifiedAddress', flex: 1}
    {header: 'Дополнительные сведения', dataIndex: 'comment', flex: 1}
    {header: 'Телефон', dataIndex: 'phone', width: 120}
    {
      header: 'Баланс'
      dataIndex: 'balance'
      align: 'right'
      width: 70
      renderer: (v) ->
        formated = Ext.util.Format.number(v, '0,000.00') + 'р.'
        return formated
    }
    {header: 'Тариф', dataIndex: 'tariffName', width: 100}
    {
      header: 'Статус'
      dataIndex: 'status'
      width: 60
      renderer: (v) ->
        if v == 'ACTIVE'
          return '<img src="static/img/green-led.gif">'
        else if v == 'INACTIVE'
          return '<img src="static/img/red-led.gif">'
        return ''
    }
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
          text: 'Удалить'
          icon: 'static/img/delete.gif'
          action: 'delete'
        }
        '-'
        {
          text: 'Платежи'
          icon:'static/img/money.png'

          menu: [
            {text: 'Показать платежи', action: 'showPayments', icon:'static/img/grid.png'}
            {text: 'Виртуальный платеж', action: 'addVirtualPayment', icon:'static/img/money_add.png'}
            {text: 'Новый платеж\\списание', action: 'addPayment', icon:'static/img/money_add.png'}
          ]
        }
        '-'
        {
          text: 'Тариф'
          icon: 'static/img/details.gif'

          menu: [
            {text: 'Показать историю', action: 'showTariffHistory', icon: 'static/img/grid.png'}
            {text: 'Изменить тариф', action: 'changeTariff', icon: 'static/img/article.gif'}
          ]
        }
        '-'
        {
          text: 'Активность'
          icon:'static/img/user.png'

          menu: [
            {text: 'Показать историю', action: 'showStatusHistory', icon:'static/img/grid.png'}
            {text: 'Включить', action: 'activateCustomer', icon:'static/img/green-led.gif'}
            {text: 'Отключить', action: 'deactivateCustomer', icon:'static/img/red-led.gif'}
          ]
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