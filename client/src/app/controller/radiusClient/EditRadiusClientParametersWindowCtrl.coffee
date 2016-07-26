Ext.define 'ISPBClient.controller.radiusClient.EditRadiusClientParametersWindowCtrl',
  extend: 'Ext.app.Controller'

  views: ['radiusClient.EditRadiusClientParametersWindow']
  models: ['RadiusClientParameter', 'RadiusClientParameterName', 'RadiusClientParameterValue']

  init: ->
    this.control
      'editRadiusClientParametersWindow button[action=add]':
        click: this.onAddClick

      'editRadiusClientParametersWindow button[action=delete]':
        click: this.onDeleteClick

      'editRadiusClientParametersWindow button[action=save]':
        click: this.onSaveClick

      'editRadiusClientParametersWindow button[action=cancel]':
        click: this.onCancelClick

      'editRadiusClientParametersWindow button[action=change]':
        click: this.onChangeClick

      'editRadiusClientParametersWindow combobox[name=parameterName]':
        change: this.onParameterNameChange

      'editRadiusClientParametersWindow grid':
        cellclick: this.onGridClick

  getSelectedRecord: (control) ->
    grid = control.up('grid')
    s = grid.getSelectionModel().getSelection()
    if s.length > 0
      return s[0]
    return null

  onAddClick: (element) ->
    store = element.up('grid').getStore()
    radiusClientId = element.up('window').getRadiusClient().getId()
    parameterName = element.up('window').down("combobox[name=parameterName]").getValue()
    parameterValue = element.up('window').down("combobox[name=parameterValue]").getValue()

    record = Ext.create('ISPBClient.model.RadiusClientParameter')
    record.set('radiusClientId', radiusClientId)
    record.set('parameterName', parameterName)
    record.set('parameterValue', parameterValue)

    store.add(record)

  onDeleteClick: (element) ->
    record = this.getSelectedRecord(element)
    store = element.up('grid').getStore()
    if (record)
      store.remove(record)

  onSaveClick: (element) ->
    store = element.up('window').down('grid').getStore()
    store.sync failure: (batch) ->
      Ext.MessageBox.alert 'Ошибка', 'Не удалось сохранить изменения!'
    element.up('window').close()

  onCancelClick: (element) ->
    element.up('window').close()

  onParameterNameChange: (element, newValue) ->
    clientIp = element.up('window').getRadiusClient().get('ip4Address')
    store = element.up('window').down("combobox[name=parameterValue]").getStore()

    store.clearFilter(true);
    store.filter([
      {property: "clientIp__eq", value: clientIp}
      {property: "parameterName__eq", value: newValue}
    ])

  onChangeClick: (element) ->
    record = this.getSelectedRecord(element)
    parameterName = element.up('window').down("combobox[name=parameterName]").getValue()
    parameterValue = element.up('window').down("combobox[name=parameterValue]").getValue()
    if (record)
      record.set('parameterName', parameterName)
      record.set('parameterValue', parameterValue)

  onGridClick: (element) ->
    record = this.getSelectedRecord(element)
    element.up('window').down("combobox[name=parameterName]").setValue(record.get('parameterName'))
    element.up('window').down("combobox[name=parameterValue]").setValue(record.get('parameterValue'))





