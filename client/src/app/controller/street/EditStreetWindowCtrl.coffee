Ext.define 'ISPBClient.controller.street.EditStreetWindowCtrl',
  extend: 'Ext.app.Controller'
  views: ['street.EditStreetWindow']

  requires: ['ISPBClient.utils.RpcProcedure']

  init: ->
    this.control
      'editStreetWindow button[action=cancel]':
        click: this.onCancelClick

      'editStreetWindow button[action=save]':
        click: this.onSaveClick


  onCancelClick: (btn) ->
    btn.up('editStreetWindow').close()

  onSaveClick: (btn) ->
    return