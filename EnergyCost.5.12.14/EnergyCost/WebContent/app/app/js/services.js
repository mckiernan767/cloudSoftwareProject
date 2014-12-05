'use strict';

/* Services */


var services = angular.module('myApp.services', []);
  
services.value('version', '0.0.7');

services.factory('DatacenterService', ['$http', function(http) {
   return {
    //offset is in minutes. Maybe should be changed in the api to unix time?
     getInActiveHosts: function(datacenterId, offset,successCallback, errorCallback) {
      var heartbeatUrl = '/heartbeat';
      if(offset) //Note: default offset is 10 min
      {
        heartbeatUrl = heartbeatUrl + '?offset=' + offset;
      }
      http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + heartbeatUrl).success(successCallback).error(errorCallback);
     },
     getAll: function(successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters').success(successCallback).error(errorCallback);
     },
     create: function(dc, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'datacenters/', dc).success(successCallback).error(errorCallback);
     },
     edit: function(dc, successCallback, errorCallback) {
       http.put(config.serverBaseUrl + 'datacenters/' + dc.id, dc).success(successCallback).error(errorCallback);
     },
     upload: function(file, successCallback, errorCallback) {
      var fd = new FormData();
      fd.append("file", file);
       http.post(config.serverBaseUrl + 'datacenters/upload', fd, {
            withCredentials: true,
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).success(successCallback).error(errorCallback);
     },
     delete: function(datacenterId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'datacenters/' + datacenterId).success(successCallback).error(errorCallback);
     }
   }
}]);

services.factory('FloorService', ['$http', function(http) {
   return {
     getByDatacenter: function(datacenterId, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors').success(successCallback).error(errorCallback);
     },
     create: function(datacenterId, floor, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/', floor).success(successCallback).error(errorCallback);
     },
     edit: function(datacenterId, floor, successCallback, errorCallback) {
       http.put(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floor.id, floor).success(successCallback).error(errorCallback);
     },
     delete: function(datacenterId, floorId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId).success(successCallback).error(errorCallback);
     }
   }
}]);

services.factory('RackService', ['$http', function(http) {
   return {
     get: function(datacenterId, floorId, rackId, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId)
       .success(successCallback).error(errorCallback);
     },
     getByDatacenter: function(datacenterId,successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/allracks')
       .success(successCallback).error(errorCallback);
     },
     getByFloor: function(datacenterId, floorId,successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks')
       .success(successCallback).error(errorCallback);
     },
     create: function(datacenterId, floorId, rack, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks', rack).success(successCallback).error(errorCallback);
     },
     edit: function(datacenterId, floorId, rack, successCallback, errorCallback) {
       http.put(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rack.id, rack).success(successCallback).error(errorCallback);
     },
     delete: function(datacenterId, floorId, rackId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId).success(successCallback).error(errorCallback);
     }
   }
}]);

services.factory('HostService', ['$http', function(http) {
   return {
    getAll: function(datacenterId, floorId, rackId, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId +'/hosts/').success(successCallback).error(errorCallback);
     },
    get: function(datacenterId, floorId, rackId, hostId, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId +'/hosts/' + hostId)
       .success(successCallback).error(errorCallback);
     },
    getActivity: function(datacenterId, floorId, rackId, hostId, startTime, endTime, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + 
        floorId +'/racks/' + rackId +'/hosts/' + hostId + '/activity?starttime=' + startTime + '&endtime=' + endTime)
       .success(successCallback).error(errorCallback);
     },
    getByDatacenter: function(datacenterId,successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'datacenters/' + datacenterId + '/allhosts')
       .success(successCallback).error(errorCallback);
     },
    create: function(datacenterId, floorId, rackId, host, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId + '/hosts/', host).success(successCallback).error(errorCallback);
     },
    edit: function(datacenterId, floorId, rackId, host, successCallback, errorCallback) {
       http.put(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId + '/hosts/' + host.id, host).success(successCallback).error(errorCallback);
     },
    delete: function(datacenterId, floorId, rackId, hostId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'datacenters/' + datacenterId + '/floors/' + floorId +'/racks/' + rackId + '/hosts/' + hostId).success(successCallback).error(errorCallback);
     }
   }
}]);

services.factory('PowerModelService', ['$http', function(http) {
   return {
     getAll: function(pmgId, successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'powermodelgroups/'+ pmgId + '/powermodels/')
       .success(successCallback).error(errorCallback);
     },
     upload: function(pmgId, file, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'powermodelgroups/' + pmgId + '/powermodels/upload', file).success(successCallback).error(errorCallback);
     },
     create: function(pmgId, pm,successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'powermodelgroups/' + pmgId + '/powermodels/', pm).success(successCallback).error(errorCallback);
     },
     delete: function(pmgId, pmId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'powermodelgroups/' + pmgId + '/powermodels/' + pmId).success(successCallback).error(errorCallback);
     }               
   }
}]);

services.factory('PowerModelGroupService', ['$http', function(http) {
   return {
     getAll: function(successCallback, errorCallback) {
       http.get(config.serverBaseUrl + 'powermodelgroups/')
       .success(successCallback).error(errorCallback);
     },
     create: function(pmg, successCallback, errorCallback) {
       http.post(config.serverBaseUrl + 'powermodelgroups/', pmg).success(successCallback).error(errorCallback);
     },
     edit: function(pmg, successCallback, errorCallback) {
       http.put(config.serverBaseUrl + 'powermodelgroups/' + pmg.id, pmg).success(successCallback).error(errorCallback);
     },
     delete: function(pmgId, successCallback, errorCallback) {
       http.delete(config.serverBaseUrl + 'powermodelgroups/' + pmgId).success(successCallback).error(errorCallback);
     }          
   }
}]);

services.factory('ReportService', ['$http', function(http) {
        return {
         getAll: function(successCallback, errorCallback) {
           http.get(config.serverBaseUrl + 'reports').success(successCallback).error(errorCallback);
         },
         get: function(reportId, successCallback, errorCallback) {
           http.get(config.serverBaseUrl + 'reports/' + reportId).success(successCallback).error(errorCallback);
         },
         create: function(reportParam, successCallback, errorCallback) {
           http.post(config.serverBaseUrl + 'reports', reportParam).success(successCallback).error(errorCallback);
         }
        }; 

  }]);

services.factory('Utils', ['$dialog', 'toaster', function(dialogService, toaster) {
   return {
    ensureArray: function(data) {
     	if( data && !angular.isArray(data))
  		{
  			data = [data];
  		}
		  return data;
     },
    getItem: function(itemId, itemArray, propertyName){
     	if(itemArray && angular.isArray(itemArray)){     		
     		for(var i=0; i<itemArray.length; i++) {
     			var id = itemArray[i][propertyName];
        		if (id === itemId){
        			return itemArray[i];	
        		} 
    		}
     	}
     	return null;
     },
    getItemIfArrayOnlyContainsOneElement: function(dataArray){
      if(angular.isArray(dataArray) && dataArray.length == 1){
        return dataArray[0];
      }
      return null;
     },
    confirmDeleteMsgBox: function(entityName, onErrorOkConfirm, errorCallback){
      var title = 'Delete ' +entityName;
      var msg = 'Are you sure that you want to delete the '+entityName+'? Remember that all connected entities will be deleted as well.';
      var btns = [{result:'cancel', label: 'Cancel'}, {result:'ok', label: 'OK', cssClass: 'btn-primary'}];

      dialogService.messageBox(title, msg, btns)
        .open()
        .then(onErrorOkConfirm, errorCallback);                
     },
    onError: function(data, status){
      if(data.message)
      {
        toaster.pop('error', "Error", data.message, 4000);  
      }
      else
      {
        var text = "Something went wrong: " + angular.toJson(data);
        toaster.pop('error', "Error", text, 4000);
      }
    },
    toasterSuccess: function(text){
      toaster.pop('success', "Success", text, 3000);
    },
    toasterError: function(text){
      toaster.pop('error', "Error", text, 4000);
    },
    toasterWarning: function(text){
      toaster.pop('warning', "Warning", text, 3000);
    },
    toasterInfo: function(text){
      toaster.pop('info', "Info", text, 3000);
    }
  }
}]);