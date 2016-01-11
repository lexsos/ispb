Ext.application
  requires: ['Ext.container.Viewport']
  name: 'ISPBClient'
  appFolder: 'app'

  controllers: [
    'Main',
    'LoginWindowCtrl',
    'city.CityGridCtrl',
    'city.AddWindowCtrl',
  ]

  launch: ->
    Ext.create 'Ext.container.Viewport',
      layout: 'fit'
      items: [
        xtype: 'Main'
      ]
