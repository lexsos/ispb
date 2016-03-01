Ext.define 'ISPBClient.view.payment.PaymentList',
  extend: 'Ext.Panel'
  alias: 'widget.PaymentList'
  title: 'Начисления'
  layout: 'fit'

  items: [
    {xtype: 'PaymentGrid'}
  ]