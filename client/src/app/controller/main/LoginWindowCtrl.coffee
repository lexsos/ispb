Ext.define 'ISPBClient.controller.main.LoginWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['main.LoginWindow']

  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'loginWindow button[action=login]':
        click: this.onLoginClick

  onLoginClick: (btn)->
    window = btn.up('loginWindow')
    form   = window.down('form')
    login = form.getValues().login
    password = form.getValues().password
    result = ISPBClient.utils.RpcProcedure.login(login, password)
    if result
      window.close()
    else
      Ext.MessageBox.alert 'Предупреждение', 'Имя пользователя или пароль не верны!'
