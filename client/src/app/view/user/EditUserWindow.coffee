Ext.define 'ISPBClient.view.user.EditUserWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editUserWindow'

  title: 'Пользователь'
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
      items:[
        {
          xtype: 'textfield'
          name : 'name'
          fieldLabel: 'Имя'
          margin: '5 5 5 5'
        }
        {
          xtype: 'textfield'
          name : 'surname'
          fieldLabel: 'Фамилия'
          margin: '5 5 5 5'
        }
        {
          xtype: 'textfield'
          name : 'login'
          fieldLabel: 'Логин'
          margin: '5 5 5 5'
        }
        {
          xtype: 'combobox'
          name : 'accessLevel'
          fieldLabel: 'Уровень доступа'
          margin: '5 5 5 5'
          displayField: 'name'
          valueField: 'key'
          typeAhead: true
          value: 100

          store:
            fields: ['key', 'name']
            data : [
              {'key': 100, name: 'Менеджер'}
              {'key': 1000, name: 'Администратор'}
            ]
        }
        {
          xtype: 'checkboxfield'
          name : 'active'
          fieldLabel: 'Включен'
          margin: '5 5 5 5'
          checked: true
          value: true
          inputValue: 'true',
          uncheckedValue: 'false'
        }
        {
          xtype: 'textfield'
          name : 'password'
          fieldLabel: 'Пароль'
          margin: '5 5 5 5'
          inputType: 'password'
        }
        {
          xtype: 'textfield'
          name : 'password1'
          fieldLabel: 'Подтверждение пароля'
          margin: '5 5 5 5'
          inputType: 'password'
        }
      ]
    }
  ]