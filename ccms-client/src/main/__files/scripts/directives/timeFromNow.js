'use strict';
angular.module('common.directives', []);
angular.module('common.directives').directive('timeFromNow', function($timeout) {
	var timePlugObj = {
		"B" : function(j) {
			return j < 10 ? "0" + j : j
		},
		"F" : function(s) {
			if (s > 0) {
				/* 1分=60秒 1天=1440分 1天=86400秒 */
				var _s = s % 60;
				var _m = (s - _s) / 60;
				var _h = parseInt(_m / 60);
				var _d = parseInt(_h / 24);
				_h = _h % 24;
				_m = _m % 60;
				return [ _d, timePlugObj.B(_h), timePlugObj.B(_m), timePlugObj.B(_s) ];
			}
			return [ 0, 0, 0, 0 ];
		},
		"dj_time" : function(starttime, serverTime) {
			var diff = null;
			if (serverTime == null || isNaN(serverTime)) {
				// 获取服务器时间可以通过Ajax
				serverTime = new Date().getTime();
			}

			// 适用于2010-08-23 6000是服务器时间差
			var start = new Date(starttime.replace(/-/g, "/")).getTime();
			if (isNaN(start)) {
				start = new Date(starttime.replace(/^(\d{4})(\d{2})(\d{2})$/, "$1/$2/$3")).getTime();
			} /* 适用于20100823 */
			diff = parseInt((serverTime - start) / 1000);

			if (isNaN(diff) || diff < 0) {
				diff = -1;
			}
			diff += 1;

			return timePlugObj.F(diff);
		}
	};

	return {
		restrict : 'E',
		template : '<span ng-model="startTime">已执行：<b>{{day}}</b>天<b>{{hour}}</b>小时<b>{{minute}}</b>分<b>{{second}}</b>秒</span>',
		replace : true,
		scope : {
			startTime : '=',
			serverTime : '='
		},
		link : function(scope, element, attrs) {

			function updateTime() {
				var times = timePlugObj.dj_time(scope.startTime, scope.serverTime);
				scope.day = times[0];
				scope.hour = times[1];
				scope.minute = times[2];
				scope.second = times[3];
			}

			updateTime();

			scope.$watch('startTime', function() {
				updateTime();
			});

			var timeoutId;

			function updateLater() {
				timeoutId = $timeout(function() {
					updateTime();
					updateLater();
				}, 1000);
			}

			element.bind('$destroy', function() {
				$timeout.cancel(timeoutId);
			});

			updateLater();

		}
	};

});