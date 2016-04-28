Ext.define 'ISPBClient.view.radiusClient.RadiusClientList',
  extend: 'Ext.Panel'
  alias: 'widget.RadiusClientList'
  title: 'Клиенты RADIUS'
  layout: 'fit'

  items: [
    {xtype: 'RadiusClientGrid'}
  ]
