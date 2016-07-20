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

  onParameterNameChange: (combobox, newValue) ->
    store = element.up('window').down("combobox[name=parameterValue]").getStore()
    servletType = combobox.up('window').getRadiusClient().get("clientType")
    store.filter(
      {property: "servletType", value: servletType}
      {property: "servletParameter", value: newValue}
    )
    store.reload()



