Ext.define 'ISPBClient.view.main.LoginWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.loginWindow'

  title: 'Вход'
  layout: 'fit'
  autoShow: true

  buttons: [
    {
      text: 'Вход'
      action: 'login'
      icon:'static/img/logout.png'
    }
  ]

  items: [
    {
      xtype: 'form'
      items:[
        {xtype: 'textfield', name : 'login', fieldLabel: 'Имя пользователя', margin: '5 5 5 5'}
        {xtype: 'textfield', name : 'password', fieldLabel: 'Пароль', margin: '5 5 5 5', inputType: 'password'}
      ]
    }
  ]