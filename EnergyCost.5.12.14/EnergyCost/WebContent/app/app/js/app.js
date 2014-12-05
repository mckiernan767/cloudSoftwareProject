'use strict';


// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['ngRoute', 'ui.bootstrap','myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers', 'd3', 'toaster']);

 app.config(['$routeProvider', function($routeProvider) {
    //dashboards
    $routeProvider.when('/hostdashboard', {templateUrl: 'partials/hostdashboard.html', controller: 'DashboardCtrl'});
    //admin
    $routeProvider.when('/heartbeat', {templateUrl: 'partials/admin/heartbeat.html', controller: 'HeartbeatCtrl'});
    $routeProvider.when('/datacenters', {templateUrl: 'partials/admin/datacenters.html', controller: 'DatacenterCtrl'});
    $routeProvider.when('/uploaddatacenter', {templateUrl: 'partials/admin/uploadDatacenter.html', controller: 'DatacenterCtrl'});
    $routeProvider.when('/floors', {templateUrl: 'partials/admin/floors.html', controller: 'FloorCtrl'});
    $routeProvider.when('/racks', {templateUrl: 'partials/admin/racks.html', controller: 'RackCtrl'});
    $routeProvider.when('/hosts', {templateUrl: 'partials/admin/hosts.html', controller: 'HostCtrl'});
    $routeProvider.when('/powermodelgroups', {templateUrl: 'partials/admin/powermodelgroups.html', controller: 'PowerModelGroupCtrl'});
    $routeProvider.when('/powermodels', {templateUrl: 'partials/admin/powermodels.html', controller: 'PowerModelCtrl'});
    $routeProvider.when('/reports', {templateUrl: 'partials/reports.html', controller: 'ReportCtrl'});
    //examples, Note its not shown in the menu. hack the url to see this
    $routeProvider.when('/examples', {templateUrl: 'partials/examples.html', controller: 'ExampleCtrl'});
    //default
    $routeProvider.otherwise({redirectTo: '/hostdashboard'});
  }]);

//added so cross domain call works
app.config(['$httpProvider', function($httpProvider) {
	$httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common["X-Requested-With"];
}]);