Ext.define 'ISPBClient.controller.user.EditUserWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['user.EditUserWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.User'
    windowWidget: 'editUserWindow'

  validateForm: (form, record) ->
    nameField = form.findField('name')
    surnameField = form.findField('surname')
    loginField = form.findField('login')
    accessLevelField = form.findField('accessLevel')
    passwordField = form.findField('password')
    password1Field = form.findField('password1')

    if nameField.getValue() == ""
      nameField.markInvalid('Укажите имя пользователя')
      return false

    if surnameField.getValue() == ""
      surnameField.markInvalid('Укажите фамилию пользователя')
      return false

    if loginField.getValue() == ""
      loginField.markInvalid('Укажите логин пользователя')
      return false

    if loginField.isDirty() && ISPBClient.utils.RpcProcedure.loginExist(loginField.getValue())
      loginField.markInvalid('Указанный логин уже существует')
      return false

    if accessLevelField.getValue() == null
      accessLevelField.markInvalid('Укажите уровень привелегий пользователя')
      return false

    if passwordField.getValue() == '' and !record
      passwordField.markInvalid('Укажите пароль пользователя')
      return false

    if passwordField.getValue() != password1Field.getValue()
      password1Field.markInvalid('Пароли не совпадают')
      return false

    return true