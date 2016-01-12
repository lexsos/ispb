Ext.define 'ISPBClient.view.building.BuildingList',
    extend: 'Ext.Panel'
    alias: 'widget.BuildingList'
    title: 'Здания'

    items: [
        {xtype: 'BuildingGrid'}
    ]