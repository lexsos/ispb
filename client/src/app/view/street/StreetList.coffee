Ext.define 'ISPBClient.view.street.StreetList',
    extend: 'Ext.Panel'
    alias: 'widget.StreetList'
    title: 'Улицы'

    listeners:
        show: () ->
            this.down('gridpanel').getStore().load()

    items: [
        {xtype: 'StreetGrid'}
    ]
