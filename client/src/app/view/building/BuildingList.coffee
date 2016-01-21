Ext.define 'ISPBClient.view.building.BuildingList',
    extend: 'Ext.Panel'
    alias: 'widget.BuildingList'
    title: 'Здания'
    layout: 'fit'

    items: [
        {xtype: 'BuildingGrid'}
    ]