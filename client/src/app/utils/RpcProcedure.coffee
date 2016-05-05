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

    streetNameExist: (name, cityId) ->
      this.call "street_name_exist",
        name: name
        cityId: cityId

    buildingNameExist: (name, streetId) ->
      this.call "building_name_exist",
        name: name
        streetId: streetId

    contractNumberExist: (contractNumber) ->
      this.call "contract_number_exist",
        contractNumber: contractNumber

    addPayment: (customerId, sum, comment) ->
      this.call "add_payment",
        customerId: customerId
        sum: sum
        comment: comment

    addVirtualPayment: (customerId, sum, dateUntil, comment) ->
      this.call "add_virtual_payment",
        customerId: customerId
        sum: sum
        until: dateUntil
        comment: comment

    tariffNameExist:  (name) ->
      this.call "tariff_name_exist",
        name: name

    assignTariff: (customerId, tariffId, dateFrom) ->
      this.call "assign_tariff",
        customerId: customerId
        tariffId: tariffId
        from: dateFrom

    setCustomerStatus: (customerId, status) ->
      this.call "set_customer_status",
        customerId: customerId
        status: status

    loginExist: (login) ->
      this.call "login_exist",
        login: login

    planeSuspenseCustomer: (customerId, dateSuspend, dateResume) ->
      this.call "plane_suspense_customer",
        customerId: customerId
        dateSuspend: dateSuspend
        dateResume: dateResume

    ip4AddressValidate: (ip4Address) ->
      this.call "ip4_address_validate",
        ip4Address: ip4Address

    ip4RadiusExist: (ip4Address) ->
      this.call "ip4_radius_exist",
        ip4Address: ip4Address

    radiusLoginExist: (login) ->
      this.call "radius_login_exist",
        login: login

    radiusClientIp4Exist: (ip4Address) ->
      this.call "radius_client_ip4_exist",
        ip4Address: ip4Address

    radiusReqClear: () ->
      this.call("radius_req_clear", {})
