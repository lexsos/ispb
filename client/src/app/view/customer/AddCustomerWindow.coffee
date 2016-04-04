Ext.define 'ISPBClient.view.customer.AddCustomerWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.addCustomerWindow'

  title: 'Абонент'
  layout: 'fit'
  autoShow: true

  config:
    store: null

  buttons: [
    {
      text: 'Сохранить'
      action: 'save'
      icon:'static/img/save.gif'
    }
    {
      text: 'Отмена'
      action: 'cancel'
      icon:'static/img/cross.gif'

    }
  ]

  items: [
    {
      xtype: 'form'
      trackResetOnLoad: true
      border: false
      items: [
        {
          xtype: 'tabpanel'
          border: false
          items: [
            {
              title: 'Данные клинта'
              border: false
              items: [
                {
                  xtype: 'textfield'
                  name : 'contractNumber'
                  fieldLabel: 'Номер договора'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textfield'
                  name : 'name'
                  fieldLabel: 'Имя'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textfield'
                  name : 'surname'
                  fieldLabel: 'Фамилия'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textfield'
                  name : 'patronymic'
                  fieldLabel: 'Отчество'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textareafield'
                  name : 'passport'
                  fieldLabel: 'Паспортные данные'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textfield'
                  name : 'phone'
                  fieldLabel: 'Мобильный телефон'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'combobox'
                  name : 'cityId'
                  fieldLabel: 'Город'
                  margin: '5 5 5 5'
                  displayField: 'name',
                  valueField: 'id',
                  typeAhead: true
                  width: 400
                  store:
                    model: 'ISPBClient.model.City'
                    autoLoad: true
                }
                {
                  xtype: 'combobox'
                  name : 'streetId'
                  fieldLabel: 'Улица'
                  margin: '5 5 5 5'
                  displayField: 'name',
                  valueField: 'id',
                  typeAhead: true
                  width: 400
                  store:
                    model: 'ISPBClient.model.Street'
                    autoLoad: false
                    remoteFilter: true
                }
                {
                  xtype: 'combobox'
                  name : 'buildingId'
                  fieldLabel: 'Дом'
                  margin: '5 5 5 5'
                  displayField: 'name',
                  valueField: 'id',
                  typeAhead: true
                  width: 400
                  store:
                    model: 'ISPBClient.model.Building'
                    autoLoad: false
                    remoteFilter: true
                }
                {
                  xtype: 'textfield'
                  name : 'room'
                  fieldLabel: 'Квартира/Офис'
                  margin: '5 5 5 5'
                  width: 400
                }
                {
                  xtype: 'textareafield'
                  name : 'comment'
                  fieldLabel: 'Дополительные сведения'
                  margin: '5 5 5 5'
                  width: 400
                }
              ]
            }
            {
              title: 'Тариф'
              border: false
              items: [
                {
                  xtype: 'numberfield'
                  name : 'balance'
                  fieldLabel: 'Начальный остаток'
                  margin: '5 5 5 5'
                  value: 0
                  width: 400
                }
                {
                  xtype: 'combobox'
                  name : 'tariffId'
                  fieldLabel: 'Тариф'
                  margin: '5 5 5 5'
                  displayField: 'name',
                  valueField: 'id',
                  typeAhead: true
                  width: 400
                  store:
                    model: 'ISPBClient.model.Tariff'
                    autoLoad: true
                    remoteFilter: true
                }
                {
                  xtype: 'combobox'
                  name : 'status'
                  fieldLabel: 'Статус'
                  margin: '5 5 5 5'
                  displayField: 'name'
                  valueField: 'key'
                  typeAhead: true
                  value: 'ACTIVE'
                  width: 400

                  store:
                    fields: ['key', 'name']
                    data : [
                      {'key': 'ACTIVE', name: 'Активен'}
                      {'key': 'INACTIVE', name: 'Отключен'}
                    ]
                }
              ]
            }
          ]
        }
      ]
    }
  ]
