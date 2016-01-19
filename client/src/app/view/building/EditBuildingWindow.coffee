Ext.define 'ISPBClient.view.building.EditBuildingWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editBuildingWindow'

  title: 'Здание'
  layout: 'fit'
  autoShow: true

  config:
    store: null

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
          xtype: 'combobox'
          name : 'streetId'
          fieldLabel: 'Улица'
          margin: '5 5 5 5'
          displayField: 'name',
          valueField: 'id',
          typeAhead: true
          store:
            model: 'ISPBClient.model.Street'
            autoLoad: false
            remoteFilter: true
        }
        {
          xtype: 'textfield'
          name : 'name'
          fieldLabel: 'Номер здания'
          margin: '5 5 5 5'
        }
      ]
    }
  ]