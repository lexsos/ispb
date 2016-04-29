Ext.define 'ISPBClient.controller.radiusClient.EditRadiusClientWindowCtrl',
  extend: 'ISPBClient.utils.DictionaryEditWindowCtrl'

  views: ['radiusClient.EditRadiusClientWindow']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    modelClass: 'ISPBClient.model.RadiusClient'
    windowWidget: 'editRadiusClientWindow'

  validateForm: (form) ->
    ip4AddressField = form.findField('ip4Address')
    secretField = form.findField('secret')

    if ip4AddressField.getValue() == ""
      ip4AddressField.markInvalid('Укажите IPv4 адрес NAS/BRAS устройтва')
      return false

    if not ISPBClient.utils.RpcProcedure.ip4AddressValidate(ip4AddressField.getValue())
      ip4AddressField.markInvalid('Не верный формат IPv4 адреса')
      return false

    if ip4AddressField.isDirty() and ISPBClient.utils.RpcProcedure.radiusClientIp4Exist(ip4AddressField.getValue())
      ip4AddressField.markInvalid('Указанный IPv4 адреса уже используется другим NAS/BRAS устройтвом')
      return false

    if secretField.getValue() == ""
      secretField.markInvalid('Укажите общий пароль с NAS/BRAS устройством')
      return false

    return true