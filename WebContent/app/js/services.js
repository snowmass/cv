'use strict';

var cvServices = angular.module('cvServices', ['ngResource']);

cvServices.factory('SelectItem', ['$resource',
  function($resource){
   return $resource('/cv/rest/document/selectItem', {}, {
       dropdown: {method:'GET', isArray:true}
     });
   }]);

cvServices.factory('Experiments', ['$resource',
  function($resource){
    return $resource('/cv/rest/document/experimentalSet/experiments/:GUID', {}, {
        load: {method:'GET', isArray:true}
      });
    }]);

cvServices.factory('AltExperiments', ['$resource',
   function($resource){
        return $resource('/cv/rest/document/experimentalSet/experiments/:GUID/:OrderBy', null, {
        persist: {
          method:'POST',
            headers: { 'Content-Type': 'application/json'
            }},
      });
   }]);
 
