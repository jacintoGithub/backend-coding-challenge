"use strict";

/******************************************************************************************

Expenses controller

******************************************************************************************/

var app = angular.module("expenses.controller", []);

app.controller("ctrlExpenses", ["$rootScope", "$scope", "config",
                                "restalchemy", "moneyService",
    function ExpensesCtrl($rootScope, $scope, $config, $restalchemy, moneyService) {

	// Update the headings
	$rootScope.mainTitle = "Expenses";
	$rootScope.mainHeading = "Expenses";

	// Update the tab sections
	$rootScope.selectTabSection("expenses", 0);

	var restExpenses = $restalchemy.init({ root: $config.apiroot}).at("expenses");

	$scope.dateOptions = {
		changeMonth: true,
		changeYear: true,
		dateFormat: "dd/mm/yy"
	};

	var loadExpenses = function() {
		// Retrieve a list of expenses via REST
		restExpenses.get().then(function(response) {
			$scope.expenses = response.content;
		}).error(function(response) {
            if(response && response.message)
                alert(response.message);
        });
	};

	$scope.saveExpense = function() {
		if ($scope.expensesform.$valid) {
		    $scope.newExpense.date = Date.parse($scope.newExpense.dateString);
		    $scope.newExpense.vat = moneyService.onlyVat($scope.newExpense.amount);
			// Post the expense via REST
			restExpenses.post($scope.newExpense).then(function() {
				// Reload new expenses list
				loadExpenses();
			}).error(function(response) {
                if(response && response.message)
                    alert(response.message);
            });
		}
	};

	$scope.onChangeAmount = function() {
	    if($scope.newExpense.amountWithOutVat){
            var amountSplit = $scope.newExpense.amountWithOutVat.split(" ");
            if(amountSplit && amountSplit.length == 2 && amountSplit[1] == "EUR"){
                moneyService.eurToGbp(amountSplit[0], function(result){
                    $scope.newExpense.amountWithOutVat = result;
                    $scope.newExpense.amount = moneyService.includeVat(result);
                });
            }
        }
        $scope.newExpense.amount = moneyService
            .includeVat($scope.newExpense.amountWithOutVat);
	}

	$scope.clearExpense = function() {
		$scope.newExpense = {};
	};

	// Initialise scope variables
	loadExpenses();
	$scope.clearExpense();
}]);
