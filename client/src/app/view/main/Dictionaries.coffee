Ext.define 'ISPBClient.view.main.Dictionaries',
    extend: 'Ext.Panel'
    alias: 'widget.MenuDictionaries'
    title: 'Справочники'
    collapsible: true

    items: [
        {
            xtype: 'menu'
            floating: false
            items: [
                {text: 'Города', widget: 'city.CityList'}
                {text: 'Улицы',  widget: 'street.StreetList'}
                {text: 'Здания', widget: 'building.BuildingList'}
                {text: 'Тарифы', widget: 'tariff.TariffList'}
                {text: 'Пользователи', widget: 'user.UserList'}
                {text: 'Клиенты RADIUS', widget: 'radiusClient.RadiusClientList'}
            ]
        }
    ]
