Ext.define 'ISPBClient.view.radiusAuth.EditRadiusAuthAttributeWindow',
  extend: 'Ext.window.Window'
  alias: 'widget.editRadiusAuthAttributeWindow'

  title: 'Атрибуты RADIUS'
  height: 400
  width: 500
  layout:'border'
  autoShow: false

  config:
    radiusAuth: null

  buttons: [
    {
      text: 'Сохранить'
      action: 'save'
      icon:'static/img/save.gif'
    }
    {
      text: 'Отмена'
      action: 'cancel'
      icon:'static/img/cross.gif'
    }
  ]

  items: [
    {
      xtype: 'panel'
      region: 'north'

      layout:
        type: 'vbox'
        align: 'stretch'

      border: null

      items: [
        {
          xtype: 'displayfield'
          fieldLabel: 'RADIUS логин'
          name: 'radiusAuthLabel'
          margin: '5 5 0 5'
          value: ''
          labelWidth: 100
        }
        {
          xtype: 'displayfield'
          fieldLabel: 'Номер договора'
          name: 'contractNumberLabel'
          margin: '5 5 0 5'
          value: ''
          labelWidth: 100
        }
        {
          xtype: 'displayfield'
          fieldLabel: 'ФИО'
          name: 'customerQualifiedNameLabel'
          margin: '5 5 5 5'
          value: ''
          labelWidth: 100
        }
      ]
    }
    {
      xtype: 'gridpanel'
      region: 'center'

      columns: [
        {xtype: 'rownumberer'}
        {
          header: 'Атрибут'
          dataIndex: 'attributeName'
          flex: 1
          editor: {xtype: 'textfield', allowBlank: false}
        }
        {
          header: 'Значение'
          dataIndex: 'attributeValue'
          flex: 1
          editor: {xtype: 'textfield', allowBlank: false}
        }
        {
          header: 'Условие'
          dataIndex: 'condition'
          flex: 1
          editor: {
            xtype: 'combobox'
            allowBlank: false
            displayField: 'name'
            valueField: 'key'
            store:
              fields: ['key', 'name']
              data : [
                {'key': 'ON_ACTIVE', name: 'Абонент активен'}
                {'key': 'ON_INACTIVE', name: 'Абонент отключен'}
                {'key': 'ALWAYS', name: 'Всегда'}
              ]
          }
        }
      ]

      plugins: [
        {ptype:'rowediting', clicksToEdit: 1}
      ]

      store:
        model: 'ISPBClient.model.RadiusAuthAttribute'
        autoLoad: false
        remoteFilter: true
        remoteSort: false

      dockedItems: [
        xtype: 'toolbar'
        dock: 'top'
        items:[
          {
            text: 'Добавить'
            icon:'static/img/add.png'
            action: 'add'
          }
          '-'
          {
            text: 'Удалить'
            icon: 'static/img/delete.gif'
            action: 'delete'
          }
        ]
      ]

    }
  ]

  listeners:
    show: (element) ->
      radiusAuth = element.getRadiusAuth()
      element.down('displayfield[name=radiusAuthLabel]').setValue(radiusAuth.get('userName'))
      element.down('displayfield[name=contractNumberLabel]').setValue(radiusAuth.get('contractNumber'))
      element.down('displayfield[name=customerQualifiedNameLabel]').setValue(radiusAuth.get('customerQualifiedName'))
      store = element.down('gridpanel').getStore()
      store.filter("userId__eq", radiusAuth.get('id'))


