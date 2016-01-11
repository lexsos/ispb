Ext.define 'ISPBClient.utils.VTypes',

  requires: ['ISPBClient.utils.RpcProcedure']

  statics:

    regTypes: () ->
      Ext.apply(Ext.form.field.VTypes, this.cityNameVType);


    cityNameVType:
      cityNameText: 'Не возможно добвать город с указанным именем. Возможно он уже существует.'
      cityName: (val, field) ->
        not ISPBClient.utils.RpcProcedure.cityNameExist(val)


, () ->
   this.regTypes()
