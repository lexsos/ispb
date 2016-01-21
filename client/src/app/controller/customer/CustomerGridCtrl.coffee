Ext.define 'ISPBClient.controller.customer.CustomerGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['customer.CustomerGrid']
  models: ['Customer']

  init: ->
    this.control 'CustomerGrid combobox[action=filterBy]',
      change: this.onFilterByChange

    this.control 'CustomerGrid toolbar[filterType=contractNumber] button[action=filterApply]',
      click: this.onFilerByContractNumber

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
      store.filter("contractNumber__like", value)
