Ext.define 'ISPBClient.view.user.UserList',
  extend: 'Ext.Panel'
  alias: 'widget.UserList'
  title: 'Пользователи'
  layout: 'fit'

  items: [
    {xtype: 'UserGrid'}
  ]