Ext.define 'ISPBClient.view.street.StreetList',
    extend: 'Ext.Panel'
    alias: 'widget.StreetList'
    title: 'Улицы'
    layout: 'fit'

    items: [
        {xtype: 'StreetGrid'}
    ]
