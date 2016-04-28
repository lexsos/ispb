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
    'customer.ShowCustomerStatusWindowCtrl',
    'customer.PlaneSuspenseCustomerWindowCtrl',

    'payment.PaymentGridCtrl',
    'payment.ShowPaymentsInGroupWindowCtrl',
    'payment.EditPaymentGroupWindowCtrl',

    'tariff.TariffGridCtrl',
    'tariff.EditTariffWindowCtrl',
    'tariff.EditTariffRadiusAttributeWindowCtrl',

    'user.UserGridCtrl',
    'user.EditUserWindowCtrl',

    'radiusAuth.RadiusAuthGridCtrl',
    'radiusAuth.EditRadiusAuthWindowCtrl',
    'radiusAuth.EditRadiusAuthAttributeWindowCtrl',

    'radiusClient.RadiusClientGridCtrl',
    'radiusClient.EditRadiusClientWindowCtrl',
  ]

  launch: ->
    Ext.create 'Ext.container.Viewport',
      layout: 'fit'
      items: [
        xtype: 'Main'
      ]
