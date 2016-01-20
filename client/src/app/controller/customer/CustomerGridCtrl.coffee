Ext.define 'ISPBClient.controller.customer.CustomerGridCtrl',
  extend: 'ISPBClient.utils.DictionaryGridCtrl'

  views: ['customer.CustomerGrid']

  init: ->
    this.control 'CustomerGrid combobox[action=filterBy]',
      change: this.onFilterByChange


  onFilterByChange: (element, newValue) ->
    parent = element.up('toolbar')
    for i, filter of Ext.ComponentQuery.query('toolbar[filterType]', parent)
      filter.hide()
    parent.down('toolbar[filterType=' + newValue + ']').show()