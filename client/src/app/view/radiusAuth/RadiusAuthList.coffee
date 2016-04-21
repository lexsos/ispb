Ext.define 'ISPBClient.view.radiusAuth.RadiusAuthList',
  extend: 'Ext.Panel'
  alias: 'widget.radiusAuthList'
  title: 'RADIUS авторизация'
  layout: 'fit'

  items: [
    {xtype: 'RadiusAuthGrid'}
  ]