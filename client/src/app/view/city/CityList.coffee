Ext.define 'ISPBClient.view.city.CityList',
  extend: 'Ext.Panel'
  alias: 'widget.CityList'
  title: 'Города'

  items: [
      {xtype: 'CityGrid'}
  ]