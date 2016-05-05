Ext.define 'ISPBClient.view.radiusAuth.RadiusAuthGrid',
  extend: 'Ext.grid.Panel'
  alias: 'widget.RadiusAuthGrid'

  columns: [
    {xtype: 'rownumberer'}
    {header: 'Логин', dataIndex: 'userName', flex: 1}
    {header: 'Номер договора', dataIndex: 'contractNumber', flex: 1}
    {header: 'ФИО', dataIndex: 'customerQualifiedName', flex: 1}
    {header: 'Адрес подключения', dataIndex: 'customerQualifiedAddress', flex: 1}
    {header: 'IPv4', dataIndex: 'ip4Address', flex: 1}
    {
      header: 'Дата создания'
      dataIndex: 'createAt'
      flex: 1
      renderer: (v) ->
        return Ext.util.Format.date(v, 'd.m.Y H:i:s')
    }
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
        '-'
        {
          text: 'Удалить запросы'
          icon: 'static/img/cross.gif'
          action: 'clearReq'
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
              {'key':'authType', 'name':'по типу'}
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

        # Фильтр по типу
        {
          xtype: 'toolbar'
          hidden: true
          border: false
          filterType: 'authType'
          items: [
            {
              xtype: 'combobox'
              queryMode: 'local'
              displayField: 'name'
              valueField: 'key'
              filterField: 'authTypeValue'
              store:
                fields: ['key', 'name']
                data:[
                  {'key':'user', 'name':'Пользователь'}
                  {'key':'req', 'name':'Запрос авторизации'}
                ]
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