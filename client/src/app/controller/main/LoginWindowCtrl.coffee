Ext.define 'ISPBClient.controller.main.LoginWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['main.LoginWindow']

  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'loginWindow button[action=login]':
        click: this.onLoginClick

      'loginWindow textfield':
        keypress: this.onKeyPress

  onKeyPress: (element, e)->
    if e.getKey() == 13
      this.onLoginClick(element)

  onLoginClick: (element)->
    window = element.up('loginWindow')
    form   = window.down('form')
    login = form.getValues().login
    password = form.getValues().password
    result = ISPBClient.utils.RpcProcedure.login(login, password)
    if result
      window.close()
    else
      Ext.MessageBox.alert 'Предупреждение', 'Имя пользователя или пароль не верны!'
