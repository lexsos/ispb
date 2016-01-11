Ext.define 'ISPBClient.view.city.CityList',
  extend: 'Ext.Panel'
  alias: 'widget.CityList'
  title: 'Города'

  listeners:
    show: () ->
      this.down('CityGrid').getStore().load()

  items: [
      {xtype: 'CityGrid'}
  ]