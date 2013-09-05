// JavaScript Document  当前店铺cookies值
var cookiesObj={
	"getcookiesMeth":function(){
		if(window.sessionStorage){
			return {"shopName":sessionStorage.getItem("shopName"),"shopId":sessionStorage.getItem("shopId")};
		}
	},
	"setcookiesMeth":function(str,id,t){//t 初始化&&切换赋值t
		var flag=cookiesObj.getcookiesMeth().shopName,
			flagId=cookiesObj.getcookiesMeth().shopId;
		if(t){//切换赋值
			sessionStorage.setItem("shopName",str);	
			sessionStorage.setItem("shopId",id);	
		}else{//初始化
			if(window.sessionStorage && !flag){
				sessionStorage.setItem("shopName",str);
				sessionStorage.setItem("shopId",id);
			}else{
				sessionStorage.setItem("shopName",flag);
				sessionStorage.setItem("shopId",flagId);
			}
		}
	}	
}


