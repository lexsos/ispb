Ext.define 'ISPBClient.model.TariffAssignment',
  extend: 'Ext.data.Model'

  fields: [
    { name: 'id', type: 'int' }
    { name: 'applyAt', type: 'date', dateFormat: 'Y-m-d H:i:s' }
    { name: 'processed', type: 'boolean' }
    { name: 'customerId', type: 'int' }
    { name: 'tariffId', type: 'int' }

    { name: 'customerQualifiedName', type: 'string' }
    { name: 'tariffName', type: 'string' }
    { name: 'customerContractNumber', type: 'string' }
  ]

  proxy:
    type: 'rest'
    url : '/api/rest/tariff_assignment'
    reader: {type: 'json', root: 'tariffAssignmentList'}