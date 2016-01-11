Ext.define 'ISPBClient.utils.RpcProcedure',
  statics:
    call: (procedureName, argObj) ->
      response = Ext.Ajax.request
        url: '/api/rpc/' + procedureName
        method: 'POST'
        headers: {'Content-Type': 'application/json'}
        jsonData: argObj
        async: false

      if response.status == 200
        result = Ext.decode(response.responseText)
        if result.code == 200
          return result.value
      return null

    loginInfo: () ->
      this.call("login_info", {})

    login: (login, password) ->
      this.call "login",
        login: login
        password: password

    logout: () ->
      this.call("logout", {})

    cityNameExist: (name) ->
      this.call "city_name_exist",
        name: name