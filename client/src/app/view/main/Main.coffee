Ext.define 'ISPBClient.view.main.Main',
  extend: 'Ext.Panel'

  alias: 'widget.Main'
  header: false
  layout:'border'

  items: [
    {
      id: 'widget_container'
      xtype: 'panel'
      region: 'center'
      header: false
      margin: '5 5 5 0'
      layout: 'fit'
    }
    {
      xtype: 'toolbar'
      region: 'north'
      items: [
        {
          xtype: 'tbtext'
          text: 'ISP Builling web client'
          style: 'font-size: 14pt;'
        }
        '->'
        {
          text: 'Выход'
          scale: 'large'
          icon: 'static/img/logout.png'
          action: 'logout'
        }
      ]
    }
    {
      id: 'menu_panel'
      xtype: 'panel'
      region: 'west'
      header: false
      width: 200
      margin: '5 0 5 5'
      autoScroll: true
      split: true
      layout:
        type: 'vbox'
        align: 'stretch'
      items: [
        {xtype: 'MenuDictionaries'}
        {xtype: 'MenuAccounting'}
      ]
    }
  ]

