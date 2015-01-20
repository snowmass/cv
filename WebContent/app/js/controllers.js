'use strict';

var cvControllers = angular.module('cvControllers', []);

cvControllers.controller('WorkCtrl', [ '$scope', 'SelectItem', 'Experiments', 'AltExperiments', '$upload',
    function($scope, SelectItem, Experiments, AltExperiments, $upload) {
      $scope.loadExperimentalSet = function() {
        $scope.expCollection = Experiments.load({
          GUID : $scope.selection.id
        });
        
        // TODO: Controller has state - replace flags with state-transition value object.
        $scope.hasData = true;
        $scope.hasNewSort = false;
        $scope.isNewSortPersisted = false;
        $scope.hasSelectedSet = true;
      };
    
      $scope.onFileSelect = function($files) {
        for (var i = 0; i < $files.length; i++) {
          var file = $files[i];
          $scope.upload = $upload.upload({ // TODO: Use a service.
            url : '/cv/rest/document/upload',
            method : 'POST',
            file : file
          }).success(function(data, status, headers, config) {
            $scope.dropdown = SelectItem.dropdown();

            $scope.hasData = true;
            $scope.hasNewSort = false;
            $scope.isNewSortPersisted = false;
            $scope.hasSelectedSet = false;
          });
        };
      };

      $scope.documentDownloadUrl = '/cv/rest/document/download'; 
      // Cannot download via Ajax so use _self to disable Angular's routing.

      $scope.getters = { // If only smart-table would expose this.
          
          DesignIndex : function(value) {
          $scope.predicate = 'Design Index';
          $scope.hasNewSort = true;
          $scope.isNewSortPersisted = false;
          return value.DesignIndex;
        },
        RunIndex : function(value) {
          $scope.predicate = 'Run Index';
          $scope.hasNewSort = true;
          $scope.isNewSortPersisted = false;
          return value.RunIndex;
        },
        value : function(value) {
          $scope.predicate = 'Value';
          $scope.hasNewSort = true;
          $scope.isNewSortPersisted = false;
          return value.value;
        }
      };

      $scope.saveExperimentalSet = function() {
        AltExperiments.persist({
          GUID : $scope.selection.id,
          OrderBy : $scope.predicate
        }, $scope.updatedCollection);

        $scope.hasData = true;
        $scope.hasNewSort = false;
        $scope.isNewSortPersisted = true
        $scope.hasSelectedSet = true;
      };

      $scope.download = function() {

        $scope.hasData = true;
        $scope.hasNewSort = false;
        $scope.isNewSortPersisted = false;
        $scope.hasSelectedSet = true;
      };
    } 
]);
