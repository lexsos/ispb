Ext.application
  requires: ['Ext.container.Viewport']
  name: 'ISPBClient'
  appFolder: 'app'

  controllers: [
    'main.Main',
    'main.LoginWindowCtrl',

    'city.CityGridCtrl',
    'city.AddWindowCtrl',

    'street.StreetGridCtrl',
    'street.EditStreetWindowCtrl',

    'building.BuildingGridCtrl',
    'building.EditBuildingWindowCtrl',

    'customer.CustomerGridCtrl',
    'customer.EditCustomerWindowCtrl',
  ]

  launch: ->
    Ext.create 'Ext.container.Viewport',
      layout: 'fit'
      items: [
        xtype: 'Main'
      ]
