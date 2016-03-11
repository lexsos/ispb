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
    'customer.AddCustomerWindowCtrl',
    'customer.ShowPaymentsCustomerWindowCtrl',
    'customer.AddCustomerVirtualPaymentWindowCtrl',
    'customer.AddCustomerPaymentWindowCtrl',
    'customer.AssignTariffCustomerWindowCtrl',
    'customer.ShowAssignTariffCustomerWindowCtrl',

    'payment.PaymentGridCtrl',
    'payment.ShowPaymentsInGroupWindowCtrl',

    'tariff.TariffGridCtrl',
    'tariff.EditTariffWindowCtrl',
  ]

  launch: ->
    Ext.create 'Ext.container.Viewport',
      layout: 'fit'
      items: [
        xtype: 'Main'
      ]
