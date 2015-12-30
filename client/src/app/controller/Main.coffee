Ext.define 'ISPBClient.controller.Main',
    extend: 'Ext.app.Controller'

    views: [
        'main.Main',
        'main.Dictionaries',
        'city.CityList',
        'street.StreetList',
        'building.BuildingList',
    ]

    init: ->
        this.viewInstance = new Array()
        this.control
            '#menu_panel menuitem':
                click: this.onMenuItemClick

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
