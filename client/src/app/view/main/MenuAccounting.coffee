Ext.define 'ISPBClient.view.main.MenuAccounting',
  extend: 'Ext.Panel'
  alias: 'widget.MenuAccounting'
  title: 'Учет'
  collapsible: true

  items: [
    {
      xtype: 'menu'
      floating: false
      items: [
        {text: 'Абоненты', widget: 'customer.CustomerList'}
        {text: 'Начисления', widget: ''}
      ]
    }
  ]
