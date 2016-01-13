Ext.define 'ISPBClient.view.street.EditStreetWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editStreetWindow'

  title: 'Улица'
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
      trackResetOnLoad: true
      items:[
        {
          xtype: 'combobox'
          name : 'cityId'
          fieldLabel: 'Город'
          margin: '5 5 5 5'
          displayField: 'name',
          valueField: 'id',
          typeAhead: true
          store:
            model: 'ISPBClient.model.City'
            autoLoad: true
        }
        {
          xtype: 'textfield'
          name : 'name'
          fieldLabel: 'Название улицы'
          margin: '5 5 5 5'
        }
      ]
    }
  ]