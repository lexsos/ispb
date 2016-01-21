Ext.define 'ISPBClient.view.customer.CustomerList',
  extend: 'Ext.Panel'
  alias: 'widget.CustomerList'
  title: 'Абоненты'
  layout: 'fit'

  items: [
    {xtype: 'CustomerGrid'}
  ]

