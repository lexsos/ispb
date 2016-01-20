Ext.define 'ISPBClient.view.customer.CustomerList',
  extend: 'Ext.Panel'
  alias: 'widget.CustomerList'
  title: 'Абоненты'

  items: [
    {xtype: 'CustomerGrid'}
  ]

