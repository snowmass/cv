'use strict';

/* App Module */

var cvApp = angular.module('cvApp', [ 'ngRoute', 'cvControllers', 'cvServices',
		'angularFileUpload', 'smart-table' ]);

cvApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/work', {
		templateUrl : 'partials/work.html',
		controller : 'WorkCtrl'
	}).when('/about', {
		templateUrl : 'partials/about.html',
	}).otherwise({
		redirectTo : '/about'
	});
} ]);
