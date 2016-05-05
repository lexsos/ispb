Ext.define 'ISPBClient.controller.radiusAuth.RadiusAuthGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['radiusAuth.RadiusAuthGrid']
  models: ['RadiusAuth']
  requires: ['ISPBClient.utils.RpcProcedure']

  config:
    gridWidget: 'RadiusAuthGrid'
    editWindowWidget: 'editRadiusAuthWindow'
    deleteMessageTmpl: 'Вы действительно хотите удалить запись {name}?'
    deleteFieldName: 'contractNumber'

  init: ->
    this.callParent();

    this.control
      'RadiusAuthGrid button[action=attributes]':
        click: this.onAttributesClick

      'RadiusAuthGrid button[action=clearReq]':
        click: this.onClearReqClick

      'RadiusAuthGrid combobox[action=filterBy]':
        change: this.onFilterByChange

      'RadiusAuthGrid toolbar[filterType=contractNumber] button[action=filterApply]':
        click: this.onFilerByContractNumber

      'RadiusAuthGrid toolbar[filterType=authType] button[action=filterApply]':
        click: this.onFilerByAuthType

      'RadiusAuthGrid toolbar[filterType=address] button[action=filterApply]':
        click: this.onFilerByAddress

      'RadiusAuthGrid toolbar[filterType=address] combobox[filterField=cityIdValue]':
        change: this.onCitySelect

      'RadiusAuthGrid toolbar[filterType=address] combobox[filterField=streetIdValue]':
        change: this.onStreetSelect

  onAttributesClick: (element) ->
    record = this.getSelectedRecord(element)
    if record
      view = Ext.widget('editRadiusAuthAttributeWindow')
      view.setRadiusAuth(record)
      view.show()

  onClearReqClick: (element) ->
    Ext.MessageBox.confirm 'Удаление', 'Вы действительно желаете удалить все запросы на авторизацию?', (btn) ->
      if btn == 'yes'
        if ISPBClient.utils.RpcProcedure.radiusReqClear()
          element.up('grid').getStore().load()
        else
          Ext.MessageBox.alert 'Ошибка', 'Не удалось удалить запрсы авторизации!'

  onFilterByChange: (element, newValue) ->
    parent = element.up('toolbar')
    for i, filter of Ext.ComponentQuery.query('toolbar[filterType]', parent)
      filter.hide()

    if newValue
      parent.down('toolbar[filterType=' + newValue + ']').show()
    else
      element.up('grid').getStore().clearFilter()

  onFilerByContractNumber: (element) ->
    store = element.up('grid').getStore()
    value = element.up('grid').down('toolbar[filterType=contractNumber] textfield[filterField=contractNumberValue]').getValue()
    if value
      store.clearFilter(true)
      store.filter("contractNumber__like", value + '%')

  onFilerByAuthType: (element) ->
    store = element.up('grid').getStore()
    value = element.up('grid').down('toolbar[filterType=authType] combobox[filterField=authTypeValue]').getValue()
    if value == 'req'
      store.clearFilter(true)
      store.filter("customer__is_null", true)
    if value == 'user'
      store.clearFilter(true)
      store.filter("customer__is_not_null", true)

  onCitySelect: (element, newValue) ->
    streetCombo = element.up('toolbar').down('combobox[filterField=streetIdValue]')
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')

    streetCombo.clearValue()
    buildingCombo.clearValue()

    if newValue
      streetStore = streetCombo.getStore()
      streetStore.clearFilter(true)
      streetStore.filter("cityId__eq", newValue)

  onStreetSelect: (element, newValue) ->
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')
    buildingCombo.clearValue()

    if newValue
      buildingStore = buildingCombo.getStore()
      buildingStore.clearFilter(true)
      buildingStore.filter("streetId__eq", newValue)


  onFilerByAddress: (element) ->
    cityCombo = element.up('toolbar').down('combobox[filterField=cityIdValue]')
    streetCombo = element.up('toolbar').down('combobox[filterField=streetIdValue]')
    buildingCombo = element.up('toolbar').down('combobox[filterField=buildingIdValue]')

    store = element.up('grid').getStore()

    if buildingCombo.getValue()
      store.clearFilter(true)
      store.filter("buildingId__eq", buildingCombo.getValue())
    else if streetCombo.getValue()
      store.clearFilter(true)
      store.filter("streetId__eq", streetCombo.getValue())
    else if cityCombo.getValue()
      store.clearFilter(true)
      store.filter("cityId__eq", cityCombo.getValue())