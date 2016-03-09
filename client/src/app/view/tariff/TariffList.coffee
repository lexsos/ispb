Ext.define 'ISPBClient.view.tariff.TariffList',
  extend: 'Ext.Panel'
  alias: 'widget.TariffList'
  title: 'Тарифы'
  layout: 'fit'

  items: [
    {xtype: 'TariffGrid'}
  ]