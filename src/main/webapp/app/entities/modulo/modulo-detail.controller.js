(function() {
    'use strict';

    angular
        .module('smartTestUiApp')
        .controller('ModuloDetailController', ModuloDetailController);

    ModuloDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Modulo', 'Servicio', 'Prueba', 'Ambiente'];

    function ModuloDetailController($scope, $rootScope, $stateParams, previousState, entity, Modulo, Servicio, Prueba, Ambiente) {
        var vm = this;

        vm.modulo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('smartTestUiApp:moduloUpdate', function(event, result) {
            vm.modulo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
