Ext.define 'ISPBClient.view.city.AddWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.addCityWindow'

  title: 'Город'
  layout: 'fit'
  autoShow: true

  buttons: [
    {
      text: 'Сохранить'
      action: 'save'
      icon:'static/img/save.gif'
    }
    {
      text: 'Отмена'
      action: 'cancel'
      icon:'static/img/cross.gif'
    }
  ]

  items: [
    {
      xtype: 'form'
      items:[
        {xtype: 'textfield', name : 'name', fieldLabel: 'Название города', margin: '5 5 5 5'}
      ]
    }
  ]