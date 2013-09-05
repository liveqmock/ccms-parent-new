angular.module('ShopServices', []);

angular.module('ShopServices').factory('Shop', function($http) {

	var Shop = {
		list: function(callback) {
			var url = root + 'shop/taobao/list';
			$http.get(url).success(function(response){
				callback(response.data.shops);
			});
		}
	};

	return Shop;
});