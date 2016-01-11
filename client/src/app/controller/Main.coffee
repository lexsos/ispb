Ext.define 'ISPBClient.controller.Main',
    extend: 'Ext.app.Controller'

    requires: ['ISPBClient.utils.RpcProcedure']

    views: [
        'main.Main',
        'main.Dictionaries',
        'main.LoginWindow',
        'city.CityList',
        'street.StreetList',
        'building.BuildingList',
    ]

    init: ->
        this.viewInstance = new Array()
        this.control
            '#menu_panel menuitem':
                click: this.onMenuItemClick

            'Main button[action=logout]':
                click: this.onLogoutClick


        this.checkLogin()

    getOrCreateView: (className) ->
        if this.viewInstance[className]
            return this.viewInstance[className]
        view = this.getView(className).create()
        view.hide()
        Ext.ComponentQuery.query("#widget_container")[0].add(view)
        this.viewInstance[className] = view
        return view

    hideAll: ->
        for name, view of this.viewInstance
            view.hide()

    showView: (className) ->
        this.hideAll()
        view = this.getOrCreateView(className)
        view.show()

    onMenuItemClick: (item) ->
        this.showView(item.widget)

    onLogoutClick: () ->
        ISPBClient.utils.RpcProcedure.logout()
        view = Ext.widget('loginWindow')

    checkLogin: ->
        if not ISPBClient.utils.RpcProcedure.loginInfo().authed
            view = Ext.widget('loginWindow')