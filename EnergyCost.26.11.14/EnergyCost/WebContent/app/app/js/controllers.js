'use strict';


/* Controllers */


var app = angular.module('myApp.controllers', []);

app.controller('ExampleCtrl', ['$scope', '$window', 'DatacenterService', 'FloorService', 'RackService', 'HostService', 'Utils', 'toaster', 
	function(scopeService, window, dcService, floorService, rackService, hostService, utils, toaster) {


	var data = [{"timeStamp":1381067064, "power":12}, 
				{"timeStamp":1381067184, "power":52}, 
				{"timeStamp":1381068984, "power":178}, 
				{"timeStamp":1381085184, "power":91}
				];


	data.forEach(function(d) {            
                d.timeStamp = new Date(d.timeStamp * 1000);            
    });

	scopeService.d3LineData = data;

	scopeService.d3PieData = [{"name":"kalle", "value":6}, 
								{"name":"nisse", "value":12}, 
								{"name":"knut", "value":24}, 
								{"name":"ronja", "value":48}];

  	scopeService.d3BarData = [
      {name: "Greg", score: 98},
      {name: "Ari", score: 96},
      {name: 'Q', score: 75},
      {name: "Loser", score: 48}
    ];

    scopeService.setAppsPieChartData = function(item)
	{
    	scopeService.$apply(function() {
      		scopeService.lineItem = item;      		
      		/*
      		//set pichart data
      		var apps = item.apps.app;
      		//console.log(apps);
      		apps.forEach(function(d) {            
                d.value = d.cpu;            			
    		});
      		scopeService.ad3AppsData = apps;
			*/
    	});
	};

    scopeService.ShowbarItemData = function(item)
	{
    	scopeService.$apply(function() {
      		scopeService.barItem = item;
    	});
	};

	scopeService.closeAlert = function(index) {
    	scopeService.alerts = null;
  	};

	scopeService.pop = function(){
        toaster.pop('success', "title", '<ul><li>Render html</li></ul>', 5000, 'trustedHtml');
        toaster.pop('error', "title", '<ul><li>Render html</li></ul>', null, 'trustedHtml');
        toaster.pop('warning', "title", null, null, 'template');
        toaster.pop('note', "title", "text");
        };

  }]); 

app.controller('HeartbeatCtrl', ['$scope', 'DatacenterService', 'Utils',function(scopeService, dcService, utils) {
	

	var getAllDatacenters = function(){
		dcService.getAll(onGetAll, utils.onError);
	};

	var onGetAll = function(data){
		scopeService.datacenters = utils.ensureArray(data.datacenter);	
	};
	  	scopeService.$watch('selectedDatacenter', function(){
  		if(scopeService.selectedDatacenter){
  			var dcId = scopeService.selectedDatacenter.id;
  			dcService.getInActiveHosts(dcId, null, function(data){
  				scopeService.hosts = utils.ensureArray(data.host);
  			}, utils.onError);
  		}
  	});


	getAllDatacenters();

  }]);  

app.controller('DatacenterCtrl', ['$scope', 'DatacenterService', 'Utils', function(scopeService, dcService, utils) {	
	scopeService.openCreateModal = function(){
		scopeService.dc = null;
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = false;
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	scopeService.openEditModal = function(dc){
		scopeService.dc = angular.copy(dc);
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = true;
	};

	scopeService.save = function(dc, isEdit){
		if(isEdit){
			dcService.edit(dc, onSave, utils.onError);
		}
		else{
			dcService.create(dc, onSave, utils.onError);
		}
	};

	var getAll = function(){
		dcService.getAll(onGetAll, utils.onError);
	};

	var onGetAll = function(data){
		scopeService.datacenters = utils.ensureArray(data.datacenter);	
	};

	var onSave = function(data){
		scopeService.closeModal();
		utils.toasterSuccess("Datacentre "  + data.name + " is saved.");
		getAll();
	};

	scopeService.reload = function(){
		utils.toasterSuccess("Full datacentre file has been imported");
	};

	scopeService.opts = {
    	backdropFade: true,
    	dialogFade:true
  	};

  	scopeService.openConfirmDeleteMsgBox = function(dc){
		utils.confirmDeleteMsgBox("datacentre", function(result){
			if(result && result==="ok"){
	        	dcService.delete(dc.id, function(data){
	        		utils.toasterSuccess("Datacentre "  + dc.name + " is deleted.");
	        		getAll();
	        	}, utils.onError);
	        }
		}, utils.onError);
	};

	getAll();

  }]);  

app.controller('FloorCtrl', ['$scope','DatacenterService', 'FloorService', 'Utils', 
	function(scopeService, dcService, floorService, utils) {
	
	var onGetByDatacenter = function(data){
		scopeService.floors = utils.ensureArray(data.floor);
	};

	var onSave = function(data){
		scopeService.closeModal();
		utils.toasterSuccess("Floor "  + data.name + " is saved.");
		scopeService.selectedDatacenter = scopeService.selectedFloorDC;
		getByDatacenter(scopeService.selectedFloorDC.id);	
	};

	scopeService.openCreateModal = function(){
		scopeService.floor = null;
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = false;
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	scopeService.openEditModal = function(floor){
		scopeService.floor = angular.copy(floor);
		scopeService.selectedFloorDC = scopeService.selectedDatacenter;
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = true;
	};

	scopeService.openConfirmDeleteMsgBox = function(floor){
		utils.confirmDeleteMsgBox("floor", function(result){
			if(result && result==="ok"){
	        	floorService.delete(floor.datacenterId, floor.id, function(data){
	        		utils.toasterSuccess("Floor "  + floor.name + " is deleted.");
	        		getByDatacenter(scopeService.selectedDatacenter.id);
	        	}, utils.onError);
	        }
		}, utils.onError);
	};

	scopeService.save = function(floor, isEdit){
		floor.datacenterId = scopeService.selectedFloorDC.id;
		if(isEdit){
			floorService.edit(scopeService.selectedDatacenter.id, floor, onSave, utils.onError);
		}
		else{
			floorService.create(scopeService.selectedFloorDC.id, floor, onSave, utils.onError);
		}
	};

	var getByDatacenter = function(dcId){
		floorService.getByDatacenter(dcId, onGetByDatacenter, utils.onError);
	};

	var init = function(){
		dcService.getAll(
			function(data){
				scopeService.datacenters = utils.ensureArray(data.datacenter);
				scopeService.selectedDatacenter	= 
				utils.getItemIfArrayOnlyContainsOneElement(scopeService.datacenters);
				scopeService.selectedFloorDC = null;
			}, 
			utils.onError);
	};

	scopeService.opts = {
    	backdropFade: true,
    	dialogFade:true
  	};

  	scopeService.$watch('selectedDatacenter', function(){
  		if(scopeService.selectedDatacenter){
  			getByDatacenter(scopeService.selectedDatacenter.id);
  		}
  	});

  	init();

  }]);  

app.controller('RackCtrl', ['$scope', 'DatacenterService', 'FloorService', 'RackService', 'Utils',
	function(scopeService, dcService, floorService, rackService, utils) {
	
	var onSave = function(data){
		scopeService.closeModal();
		utils.toasterSuccess("Rack "  + data.name + " is saved.");
		if(scopeService.selectedDatacenter !== scopeService.selectedRackDc){
			scopeService.selectedDatacenter = scopeService.selectedRackDc;
		}else{
			onSelectedDatacenterChanged();
		}
	};

	var onSelectedDatacenterChanged = function(){
	  	if(scopeService.selectedDatacenter){
				rackService.getByDatacenter(scopeService.selectedDatacenter.id,
					function(data){
						if(data){
							scopeService.racks = utils.ensureArray(data.rackExtended);
						}
						else{
							scopeService.racks = null;			
						}
					}, utils.onError);
		}
		else{
			scopeService.racks = null;
		}
	};

	scopeService.openConfirmDeleteMsgBox = function(rackExtended){
		utils.confirmDeleteMsgBox("rack", function(result){
			if(result && result==="ok"){				
	        	rackService.delete(rackExtended.datacenterId, rackExtended.floorId, rackExtended.rackId, function(data){
	        		utils.toasterSuccess("Rack "  + rackExtended.rackName + " is deleted.");
	        		onSelectedDatacenterChanged();
	        	}, utils.onError);
	        }
		}, utils.onError);
	};

	scopeService.openCreateModal = function(){
		scopeService.rack = null;
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = false;
		scopeService.selectedRackDc = null;
		scopeService.selectedFloorDc = null;
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	scopeService.openEditModal = function(rackExtended){
		rackService.get(rackExtended.datacenterId, rackExtended.floorId, rackExtended.rackId, function(data){
		scopeService.rack = data;
		scopeService.selectedRackDc = utils.getItem(rackExtended.datacenterId, scopeService.datacenters, "id");
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = true;
		}, utils.onError);
	};

	scopeService.save = function(rack, isEdit){
		rack.datacenterId = scopeService.selectedRackDc.id;
		var oldFloorId = rack.floorId;
		rack.floorId = scopeService.selectedRackFloor.id;
		if(isEdit){
			rackService.edit(scopeService.selectedDatacenter.id, oldFloorId, rack, onSave, utils.onError);
		}
		else{
			rackService.create(scopeService.selectedRackDc.id, scopeService.selectedRackFloor.id, rack, onSave, utils.onError);
		}
	};

	var init = function(){
		//get all datacenters
		dcService.getAll(
			function(data){
				scopeService.datacenters = utils.ensureArray(data.datacenter);
				scopeService.selectedDatacenter	= 
				utils.getItemIfArrayOnlyContainsOneElement(scopeService.datacenters);				
				scopeService.floors = null;
				scopeService.racks = null;
			}, 
			utils.onError);
	};

	scopeService.opts = {
    	backdropFade: true,
    	dialogFade:true
  	};

  	//for modal window
  	scopeService.$watch('selectedRackDc', function(){
  		if(scopeService.selectedRackDc){
  			floorService.getByDatacenter(scopeService.selectedRackDc.id,
  				function(data){
  					scopeService.floors = utils.ensureArray(data.floor);
  					if(scopeService.rack){
  						scopeService.selectedRackFloor = utils.getItem(scopeService.rack.floorId, scopeService.floors, "id");
  					}	
  				}, utils.onError);
  		}
  		else{
  			scopeService.floors = null;
  		}
  	});

  	scopeService.$watch('selectedDatacenter', function(){
  		onSelectedDatacenterChanged();
  	});

  	init();

  }]);  

app.controller('HostCtrl', ['$scope','DatacenterService', 'FloorService', 'RackService', 
	'HostService', 'PowerModelGroupService','Utils',
	function(scopeService, dcService, floorService, rackService, hostService, pmgService, utils) {
	
	var onSave = function(data){
		scopeService.closeModal();
		utils.toasterSuccess("Host "  + data.name + " is saved.");
		if(scopeService.selectedDatacenter !== scopeService.selectedHostDc){
			scopeService.selectedDatacenter = scopeService.selectedHostDc;
		}else{
			onSelectedDatacenterChanged();
		}
	};

	var clearChosenHost = function(){
		scopeService.host = null;
		scopeService.selectedHostDc = null;
		scopeService.selectedHostFloor = null;
		scopeService.selectedHostRack = null;
		scopeService.selectedHostModelGroup = null;
	};
	
	scopeService.openConfirmDeleteMsgBox = function(hostExtended){
		utils.confirmDeleteMsgBox("host", function(result){
			if(result && result==="ok"){
	        	hostService.delete(hostExtended.datacenterId, hostExtended.floorId, hostExtended.rackId, hostExtended.hostId, function(data){
	        		utils.toasterSuccess("Host "  + hostExtended.hostName + " is deleted.");
	        		onSelectedDatacenterChanged();
	        	}, utils.onError);
	        }
		}, utils.onError);
	};

	scopeService.openCreateModal = function(){
		clearChosenHost();
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = false;
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	scopeService.openEditModal = function(hostExtended){
		clearChosenHost();
		hostService.get(hostExtended.datacenterId, hostExtended.floorId, hostExtended.rackId, hostExtended.hostId, function(data){
			scopeService.host = data;
			scopeService.oldFloorId = hostExtended.floorId;
			scopeService.oldRackId = hostExtended.rackId;
			scopeService.selectedHostDc = utils.getItem(hostExtended.datacenterId, scopeService.datacenters, "id");
			scopeService.selectedHostModelGroup = utils.getItem(hostExtended.modelGroupId, scopeService.modelGroups, "id");
			scopeService.selectedHostType = utils.getItem(hostExtended.hostType, scopeService.hostTypes, "id");
			scopeService.shouldBeOpen = true;
			scopeService.isEdit = true;
		}, utils.onError);
	};

	scopeService.save = function(host, isEdit){
		host.datacenterId = scopeService.selectedHostDc.id;
		host.floorId = scopeService.selectedHostFloor.id;
		host.rackId = scopeService.selectedHostRack.id;
		host.modelGroupId = scopeService.selectedHostModelGroup.id;
		host.hostType = scopeService.selectedHostType.id;
		if(isEdit){
			hostService.edit(scopeService.selectedHostDc.id, scopeService.oldFloorId, scopeService.oldRackId, host, onSave, utils.onError);
		}
		else{
			hostService.create(scopeService.selectedHostDc.id, scopeService.selectedHostFloor.id, scopeService.selectedHostRack.id, host, onSave, utils.onError);
		}
	};

	var init = function(){
		dcService.getAll(
			function(data){
				scopeService.datacenters = utils.ensureArray(data.datacenter);
				scopeService.selectedDatacenter	= 
				utils.getItemIfArrayOnlyContainsOneElement(scopeService.datacenters);
				scopeService.hosts = null;
			}, 
			utils.onError);
		pmgService.getAll(
			function(data){
				scopeService.modelGroups = utils.ensureArray(data.powerModelGroup);
			}, 
			utils.onError);
	};

	scopeService.opts = {
    	backdropFade: true,
    	dialogFade:true
  	};

  	var onSelectedDatacenterChanged = function(){
  		if(scopeService.selectedDatacenter){
  			hostService.getByDatacenter(scopeService.selectedDatacenter.id,
  				function(data){
  					if(data){
  						scopeService.hosts = utils.ensureArray(data.hostExtended);
  					}
  					else{
  						scopeService.host = null;			
  					}
  				}, utils.onError);
  		}
  		else{
			scopeService.hosts = null;
  		}
	};

	scopeService.isVM = function(){
		if(scopeService.selectedHostType && scopeService.selectedHostType.id == 'VM_SERVER'){
			return true;
		}
		return false;
	};


	//TODO: need to get this data from the server from a rest call (hosts/hostTypes or something)
	scopeService.hostTypes = [ {'id':'SERVER'},  {'id':'VM_SERVER'}];

	scopeService.$watch('selectedHostType', function(){
		scopeService.isVM();
	});

  	scopeService.$watch('selectedDatacenter', function(){
	onSelectedDatacenterChanged();
  	});

  	  	//for modal window
  	scopeService.$watch('selectedHostDc', function(){
  		if(scopeService.selectedHostDc){
  			floorService.getByDatacenter(scopeService.selectedHostDc.id,
  				function(data){
  					scopeService.floors = utils.ensureArray(data.floor);  				
  					if(scopeService.host.id){
  						var hostExtended = utils.getItem(scopeService.host.id, scopeService.hosts, "hostId");
  						scopeService.selectedHostFloor = utils.getItem(hostExtended.floorId, scopeService.floors, "id");
  					}  					
  				}, utils.onError);
  		}
  		else{
  			scopeService.floors = null;
  		}
  	});

  	scopeService.$watch('selectedHostFloor', function(){
  		if(scopeService.selectedHostFloor){
  			rackService.getByFloor(scopeService.selectedHostDc.id, scopeService.selectedHostFloor.id,
  				function(data){
  					scopeService.racks = utils.ensureArray(data.rack);  					
  					if(scopeService.host.id){
  						scopeService.selectedHostRack = utils.getItem(scopeService.host.rackId, scopeService.racks, "id");
  					}  					
  				}, utils.onError);
  		}
  		else{
  			scopeService.racks = null;
  		}
  	});

  	init();

  }]);  

app.controller('PowerModelGroupCtrl', ['$scope', 'PowerModelGroupService', 'Utils', 
	function(scopeService, pmgService, utils) {

	scopeService.openCreateModal = function(){
		scopeService.pmg = null;
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = false;
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	scopeService.openEditModal = function(pmg){
		scopeService.pmg = angular.copy(pmg);
		scopeService.shouldBeOpen = true;
		scopeService.isEdit = true;
	};

	scopeService.save = function(pmg, isEdit){
		if(isEdit){
			pmgService.edit(pmg, onSave, utils.onError);
		}
		else{
			pmgService.create(pmg, onSave, utils.onError);
		}
	};

	scopeService.openConfirmDeleteMsgBox = function(modelGroup){
		utils.confirmDeleteMsgBox("powermodel group", function(result){
			if(result && result==="ok"){
	        	pmgService.delete(modelGroup.id, function(data){
	        		utils.toasterSuccess("Powermodel group "  + modelGroup.name + " is deleted.");
	        		init();
	        	}, utils.onError);
	        }
		}, utils.onError);
	};
	var onSave = function(data){
		scopeService.closeModal();
		utils.toasterSuccess("Powermodel group "  + data.name + " is saved.");
		init();
	};

	var init = function(){
		pmgService.getAll(
			function(data){
				scopeService.powerModelGroups = utils.ensureArray(data.powerModelGroup);
			}, 
		utils.onError);
	};

	init();
  }]);  

app.controller('PowerModelCtrl', ['$scope', 'PowerModelService', 'PowerModelGroupService','Utils', 
	function(scopeService, pmService, pmgService, utils) {
    scopeService.image = null;
    scopeService.imageFileName = '';

	scopeService.reload = function(){
		utils.toasterSuccess("Powermodel has been uploaded.");
		onSelectedPmgChange();
	};    

	var init = function(){
		pmgService.getAll(
			function(data){
				scopeService.powerModelGroups = utils.ensureArray(data.powerModelGroup);
			}, 
			utils.onError);
	};

	var onSelectedPmgChange = function(){
  		if(scopeService.selectedPmg){
  			pmService.getAll(scopeService.selectedPmg.id,
  				function(data){
  					scopeService.powerModels = utils.ensureArray(data.powerModel);  					
  					console.log(scopeService.powerModels);
  				}, utils.onError);
  		}
  		else{
  			scopeService.powerModels = null;
  		}
	};

	scopeService.$watch('selectedPmg', function(){
		onSelectedPmgChange();
  	});

	scopeService.openConfirmDeleteMsgBox = function(powerModel){
		utils.confirmDeleteMsgBox("powermodel", function(result){
			if(result && result==="ok"){
	        	pmService.delete(powerModel.modelGroupId,powerModel.id, function(data){
	        		utils.toasterSuccess("Powermodel "  + powerModel.name + " is deleted.");
	        		onSelectedPmgChange();
	        	}, utils.onError);
	        }
		}, utils.onError);
	};

	init();
  }]);  

app.controller('DashboardCtrl', ['$scope', '$window', 'DatacenterService', 'FloorService', 'RackService', 'HostService', 'Utils', 
	function(scopeService, window, dcService, floorService, rackService, hostService, utils) {


	var getAllDatacenters = function(){
		dcService.getAll(
			function(data){
				scopeService.datacenters = utils.ensureArray(data.datacenter);
				scopeService.selectedDc	= 
				utils.getItemIfArrayOnlyContainsOneElement(scopeService.datacenters);

				scopeService.selectedFloor = null;
			}, 
			utils.onError);
	};

	scopeService.$watch('selectedDc', function(){
  		if(scopeService.selectedDc){
  			var dcId = scopeService.selectedDc.id;
			floorService.getByDatacenter(dcId, function(data){
				scopeService.floors = utils.ensureArray(data.floor);
			}, utils.onError);
  		}
  		else
  		{
  			scopeService.selectedFloor = null;
  			scopeService.selectedRack = null;
  			scopeService.hosts = null;
  		}
  	});

	scopeService.$watch('selectedFloor', function(){
  		if(scopeService.selectedFloor){
  			var dcId = scopeService.selectedDc.id;
  			var floorId = scopeService.selectedFloor.id;
  			rackService.getByFloor(dcId, floorId, function(data){
				scopeService.racks = utils.ensureArray(data.rack);  				
			}, utils.onError);
		}
  		else
  		{
  			scopeService.selectedRack = null;
  			scopeService.hosts = null;
  		}
  	});

	scopeService.$watch('selectedRack', function(){
  		if(scopeService.selectedRack){
  			var dcId = scopeService.selectedDc.id;
  			var floorId = scopeService.selectedFloor.id;
  			var rackId = scopeService.selectedRack.id;
  			hostService.getAll(dcId, floorId, rackId, function(data)
  			{
  				scopeService.hosts = utils.ensureArray(data.host);  
  			}, utils.onError);
  		}
  		else
  		{
  			scopeService.hosts = null;
  		}
  	});

	scopeService.$watch('selectedTimeSpan', function(){
  		if(scopeService.selectedTimeSpan){
  			scopeService.d3AppsData = null;
  			updateChart();
  		}
  	});



	scopeService.getActivity = function(host){
		if(host){
			clearData();
			scopeService.selectedHost = host;
			updateChart();
		}
	};

	var clearData = function(){
		scopeService.selectedHost = null;
		scopeService.selectedTimeSpan = 24;
		scopeService.d3AppsData = null;
		scopeService.d3LineData = null;

	}
	var updateChart = function()
	{
		if(scopeService.selectedHost)
		{
			var endTime = Math.round(Date.now()/1000);
			var startTime = getStartTime(endTime); 
			//console.log("starttime:"  + startTime + "endtime:" + endTime);
			//alert(angular.toJson(host));			
			//Note: hardcoded for the moment to test the graphs
			hostService.getActivity(scopeService.selectedDc.id, scopeService.selectedFloor.id, 
				scopeService.selectedRack.id, scopeService.selectedHost.id, startTime, endTime, function(data){
			//hostService.getActivity(266, 290, 293, 283, 1337630591, 1337691593, function(data){
				var activities = data.activity;
				if(activities){
					activities.forEach(function(d) {            
		                d.timeStamp = new Date(d.timeStamp * 1000);
		                //to get power instead of energy
		                d.power = 60 * d.power;             			
		    		});
					scopeService.d3LineData = activities;
				}
				else{
					utils.toasterInfo("No data was found the last 24 hours for host");
					clearData();
				}
			}, utils.onError);
		}
	};

	var getStartTime = function(endTime){
		if(scopeService.selectedTimeSpan == 1){
			return endTime - 1*60*60;
		}
		if(scopeService.selectedTimeSpan == 12){
			return endTime - 12*60*60;
		}
		return endTime - 24*60*60; //default to 24
	};

	scopeService.setAppsPieChartData = function(item)
	{
    	scopeService.$apply(function() {
      		//scopeService.lineItem = item;      		
      		//set piechart data
      		var data = [];
      		var allApps = item.allApps;
      		var apps = item.apps.app;
      		//console.log(apps);
      		apps.forEach(function(d) {
      			data.push(createAppItem(d, allApps));            
    		});    		
  			data.push(createOtherAppsItem(data, allApps));        
      		scopeService.d3AppsData = data;
    	});
	};

	var createAppItem = function(d, allApps){
		var value = Number((d.cpu/allApps) * 100).toFixed(2);
		var name = setAppName(d.name, value);
		return {"name": name, "value": value};
	};

	var createOtherAppsItem = function(data, allApps){
    		var dataAppsPercentage = window._.reduce(data, function(memo, d){ return memo + Number(d.value);}, 0);
    		var value = Number(100 - dataAppsPercentage).toFixed(2);
    		var name = setAppName("other apps", value);
  			return {"name": name, "value": value};
	};

	var setAppName = function(name, value){
		return name + ' (' + value + '%)';
	};


	getAllDatacenters(); 
}]); 

app.controller('ReportCtrl', ['$scope', 'DatacenterService', 'ReportService','Utils', 'toaster', 
	function(scopeService, dcService, reportService, utils, toaster) {

	scopeService.openCreateModal = function(){
		scopeService.reportParam = null;
		scopeService.shouldBeOpen = true;
		dcService.getAll(
			function(data){
				scopeService.datacenters = utils.ensureArray(data.datacenter);
			}, 
			utils.onError);
	};

	scopeService.closeModal = function(){
		scopeService.shouldBeOpen = false;
	};

	var validateReportParams = function(reportParam){
		if(!validateReportParamInt(reportParam.pue, "PUE")||
			!validateReportParamInt(reportParam.energyCost, "Energy cost")||
			!validateReportParamInt(reportParam.carbonCost, "Carbon cost")){
			return false;
		}
		//TODO: validate Date objects
		var startTime = new Date(reportParam.startTime).getTime();
		var endTime = new Date(reportParam.endTime).getTime();
		if(startTime>0 && endTime>0 && startTime<endTime){
			reportParam.startTime = startTime/1000;
			reportParam.endTime = endTime/1000;
			return true;
		}
		else{
			utils.toasterError("Audit period is invalid");
			return false;	
		}

		return true;
	};

	var validateReportParamInt = function(val, paramName){
		if(isNaN(val) || val<=0)
		{
			utils.toasterError(paramName + " is invalid");
			return false;
		}
		return true;		
	};

	var map = function(reportParam){
		var serverReportParam = new Object();
		serverReportParam.datacenterId = reportParam.datacenter.id;
		serverReportParam.datacenterName = reportParam.datacenter.name;
		serverReportParam.pue = reportParam.pue;
		serverReportParam.energyCost = reportParam.energyCost;
		serverReportParam.carbonCost = reportParam.carbonCost;
		serverReportParam.startTime = reportParam.startTime;
		serverReportParam.endTime = reportParam.endTime;
		serverReportParam.extrapolateYears = config.extrapolateYears; 
		return serverReportParam;
	};

	scopeService.create = function(reportParam){
		//map to object which will be sent 
		var serverReportParam = map(reportParam);
		if(validateReportParams(serverReportParam))
		{
			utils.toasterInfo("The report is being created. It might take some time so please check back later.");
			scopeService.closeModal();
			
			reportService.create(serverReportParam, function(data){	
				init();
				//TODO: lock the report button until this report has been made?
				utils.toasterSuccess("Report has been created");
			}, 
			utils.onError);
		}
	};


	scopeService.opts = {
    	backdropFade: true,
    	dialogFade:true
  	};

  	scopeService.showReport = function(report){
  		scopeService.report = null;
  		scopeService.topHostReports = null;
  		scopeService.leastHostReports = null;
  		scopeService.consolidationHostReports = null;
  		scopeService.rackReports = null;
  		scopeService.hostDataAvailable = false;
  		scopeService.rackDataAvailable = false;
		reportService.get(report.id, function(data){
				scopeService.report = data;
				if(data.topHostReports){
					scopeService.topHostReports = data.topHostReports.hostReport;
					scopeService.hostDataAvailable = true;
				};
				if(data.leastHostReports){
					scopeService.leastHostReports = data.leastHostReports.hostReport;
					scopeService.hostDataAvailable = true;
				};
				if(data.consolidationHostReports){
					scopeService.consolidationHostReports = data.consolidationHostReports.hostReport;
					scopeService.hostDataAvailable = true;
				};
				if(data.rackReports){
					scopeService.rackReports = data.rackReports.rackReport;
					scopeService.rackDataAvailable = true;
				};
			}, utils.onError);
  	};

  	scopeService.setRowIndicator = function(rackReport){
  		if(Number(rackReport.pdu) < Number(rackReport.maxPower)){
  			return "error";
  		}
  		return "success";
  	};

	var init = function(){
		reportService.getAll(function(data){
				scopeService.reports = utils.ensureArray(data.reportInfo);
				scopeService.extrapolateYears = config.extrapolateYears; 
			}, utils.onError);

	};

	init();

}]);  
