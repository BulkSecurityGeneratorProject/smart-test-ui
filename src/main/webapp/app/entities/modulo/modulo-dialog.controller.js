(function() {
    'use strict';

    angular
        .module('smartTestUiApp')
        .controller('ModuloDialogController', ModuloDialogController);

    ModuloDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Modulo', 'Servicio', 'Prueba', 'Ambiente'];

    function ModuloDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Modulo, Servicio, Prueba, Ambiente) {
        var vm = this;

        vm.modulo = entity;
        vm.clear = clear;
        vm.save = save;
        vm.servicios = Servicio.query();
        vm.pruebas = Prueba.query();
        vm.ambientes = Ambiente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.modulo.id !== null) {
                Modulo.update(vm.modulo, onSaveSuccess, onSaveError);
            } else {
                Modulo.save(vm.modulo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('smartTestUiApp:moduloUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
