angular.module('personalizedPackage', [ 'ShopServices', 'common.directives' ]).factory('Plan', [ '$http', function($http) {

	var Plan = {
		planGroup : function(shopId, callback) {
			var url = root + 'rulecenter/planGroup/' + shopId;
			$http.get(url).success(function(response) {
				callback(response.data);
			});
		}
	};

	return Plan;

} ]);

angular.module('personalizedPackage').factory('Statuses', function($http) {
	var Statuses = {

		currentStatus : function(planId, dateSlot, callback) {
			var url = root + 'rulecenter/statuses/plans/' + planId + '/slot/' + dateSlot;
			$http.get(url).success(function(response) {
				response.data._planId = planId; // XXX 给返回值加上一个planId
				response.data.visit = response.visit; // XXX 其实这个是serverTime
				callback(response.data);
			});
		}

	};

	return Statuses;
});

angular.module('personalizedPackage').controller('statusesController',
		[ '$scope', '$http', '$window', '$timeout', 'Plan', 'Shop', 'Statuses', function($scope, $http, $window, $timeout, Plan, Shop, Statuses) {
			// 店铺
			(function() {
				$(function() {
					$(".autoMark .yellownote").hover(function(){
						$(this).siblings("em").show();									  
					},function(){
						$(this).siblings("em").hide();
					});
				});
			})(jQuery)

			$scope.changShop = function(shop) {
				$scope.currentShop = shop;
				cookiesObj.setcookiesMeth(shop.shopName, shop.shopId, true);
			}
			// end

			var date = new Date();
			var today = date.getFullYear() + '' + (date.getMonth() > 10 ? (date.getMonth() + 1) : '0' + (date.getMonth() + 1)) + date.getDate();

			$scope.$watch('currentShop', function(shop) {
				if (shop) {
					Plan.planGroup(shop.shopId, function(planGroup) {
						$scope.plans = new Array(3);
						var plans = planGroup.plans;
						if (plans && plans.length > 0) {
							$scope.currentPlan = plans[0];
							for ( var i = 0; i < plans.length; i++) {
								$scope.plans[i] = plans[i];
								if (plans[i].active) {
									Statuses.currentStatus(plans[i].id, today, function(statuses) {
										angular.forEach($scope.plans, function(plan) {
											if (plan.id == statuses._planId) {
												plan.statuses = statuses;
											}
										});
									});
								}
							}
							;
						}
					});
				}
			}, true);

			// 加载所有店铺，callback真恶心
			Shop.list(function(shops) {
				$scope.shops = shops;
				var firstShop = shops[0];
				cookiesObj.setcookiesMeth(firstShop.shopName, firstShop.shopId, false);
				firstShop = cookiesObj.getcookiesMeth();
				$scope.currentShop = firstShop;
			});

			$scope.turn = 0;
			(function() {
				cancelRefresh = $timeout(function myFunction() {
					if ($scope.plans.length == 0) {
						return;
					}
					$scope.turn = ($scope.turn + 1) % $scope.plans.length;
					if ($scope.plans[$scope.turn].active) {
						Statuses.currentStatus($scope.plans[$scope.turn].id, today, function(statuses) {
							angular.forEach($scope.plans,function(val,key){
								if(val.id==statuses._planId){
									val.statuses=statuses;
								}								  
							});
							//$scope.plans[$scope.turn].statuses = statuses;
						});
					}

					cancelRefresh = $timeout(myFunction, 2000);
				}, 2000);

				$scope.$on('$destroy', function(e) {
					$timeout.cancel(cancelRefresh);
				});
			})();
			$scope.className = "";
			$scope.setActive = function(plan, i) {
				switch (i) {
				case 0:
					$scope.className = "";
					break;
				case 1:
					$scope.className = "leftValOne";
					break;
				case 2:
					$scope.className = "leftValTwo";
					break;
				}
				$scope.currentPlan = plan;
			};

			$scope.getRuleData = function(rule) {
				if (!$scope.currentPlan.statuses) {
					return {
						rate : '0%'
					};
				}
				var ruleData = $scope.currentPlan.statuses.ruleData;
				for ( var i = ruleData.length - 1; i >= 0; i--) {
					if (ruleData[i].id == rule.id) {
						return ruleData[i];
					}
				}
				return {
					rate : '0%'
				};
			};

		} ]);
