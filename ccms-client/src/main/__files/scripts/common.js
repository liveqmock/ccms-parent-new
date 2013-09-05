
//短信编辑器
function listenLen(objIframe){
	var nowLen=0;
	if(jQuery.browser.mozilla){//ff中每增加一个img，len增加1
		nowLen=objIframe.find("img.varImg").length;
	}else{
		var $iframeButton=objIframe.find("span.iframeButton");
		for(var i=0;i<$iframeButton.length;i++){
			nowLen=nowLen+$iframeButton.eq(i).text().length;
		}
	}
	return nowLen;
}

var kindEditorObj={
	"creatEditor":function(id){
		//编辑器插件
		var ke=KindEditor.create(id,{
			basePath:'3rd/kindeditor-4.1.6/',
			allowFileManager : true,
			items:[],
			width:'100%',
			resizeType:0,
			pasteType:1,
			fullscreenMode:false,//全屏
			newlineTag:"br",//设置换行符
			afterChange : function() {
				var objIframe=$(document.getElementById('mesIframe').contentWindow.document.body);
				var nowLen=listenLen(objIframe);
				
				var count = this.count('text');
				var sStr = this.html().match(/&([^n]*?);/g);
				if(sStr){
					$.each(sStr,function(i,n){
						count = count - n.match(/&([^n]*?;)/)[1].length;
					});
				}
				
				if($(".tdBtn").is(":checked")){
					KindEditor('.mseLen').html(count+7-nowLen);
				}else{
					KindEditor('.mseLen').html(count-nowLen);
				}
				if (KindEditor.IE&&this.isEmpty()){objIframe.html("")};//IE删除再添加bug
	//保存后bug，span添加contenteditable属性
			objIframe.find("span").attr("contenteditable",false);
			}
		});
		return ke;
	},
	"editorAddElement":function(ele,id,val,orderVal){
			var imgStr,cvalAttr = '';
			if(typeof orderVal != 'undefined'){
				cvalAttr = orderVal;
			}
			
			if(jQuery.browser.mozilla){//ff用img，其他button
				imgStr="<img id='"+id+"' class='varImg "+orderVal+"' src='' alt="+val+">";
			}else{
				imgStr="<span id='"+id+"' class='iframeButton "+orderVal+"'>"+val+"</span>"+"&nbsp;";
			}
			ele.insertHtml(imgStr);
	},
	"firstSetVal":function(value){//获取数据
		var firstValue;
		if(jQuery.browser.mozilla){
			//var regOne=/<span id=['\\"](\d+)['\\"] class=['\\"]iframeButton['\\"] contenteditable=['\\"]false['\\"]>([^<\/span>]+)<\/span>/gi;
			var regOne=/<span id=(['\\"])(\d+)\1 class=\1iframeButton\1>([^<\/span]+)<\/span>/gi;
			var flagOne=regOne.test(value);
			if(flagOne){
				firstValue=value.replace(regOne,"<img id='$2' class='varImg' src='' alt='$3' />");
			}else{
				firstValue=value;
			}
		}else{
			var regTwo=/<img id=(['\\"])(\d+)\1 class=\1varImg\1 src=\1\1 alt=\1([^\/>]+)\1 \/>/gi;
			var flagTwo=regTwo.test(value);
			if(flagTwo){
				firstValue=value.replace(regTwo,"<span id='$2' class='iframeButton' contenteditable='false'>$3</span>");
			}else{
				firstValue=value;
			}
		}
		var valReg=/>$/;
		if(valReg.test(firstValue)){//IE无法获取光标bug
			firstValue+="&nbsp;";
		}
		return firstValue;
	},
	"getKindEditorVal":function(s){
		s=jQuery.trim(s.replace(/\<br \/\>/gi,"").replace(/&amp;/g,'&').replace(/&lt;/g,'<').replace(/&gt;/g,'>'));
		if(jQuery.browser.mozilla){
			return s=(s.replace(/^(&nbsp;\s*)+/,"")).replace(/(&nbsp;\s*)+$/,"");
		}else{//去浏览器添加的空格
			s=(s.replace(/^(&nbsp;\s*)+/,"")).replace(/(&nbsp;\s*)+$/,"");
			var spanReg=/(<span id="(\d+)" class="iframeButton">[^<\/span>]+<\/span>)&nbsp;/gi;
			s=s.replace(spanReg,"$1");
			return s=s.replace(/<p>\s*/g,"").replace(/<\/p>\s*/g,"").replace(/\n*/g,"");
		}
	}
}
//message end
var ccms = {
	getSecond: function(time){//获取指定时间距 1970/1/1 午夜（GMT 时间）之间的毫秒数    ps: time格式为 2012-12-04 11:34:13
		return time ? Date.parse(time.replace(/\-/g,'/')) : '';
	},
	getObjInAry:function(obj,id,key){//获取某个id值在数组中的对象
		for(var i=0;i<obj.length;i++){
			if(id == obj[i][key]) return obj[i];
		}
	},
	fixedHeight:function(obj, bottomH){
		var bottomH = bottomH || 0;
		function setHeight(){
			if(obj.length){
				//obj.height($(window).height() - obj.offset().top - 37);
				obj.css({'overflow-y': 'auto', 'height': $(window).height() - obj.offset().top - bottomH})
			}else{
				$(window).unbind('resize',setHeight);
			}
		}
		setHeight();
		$(window).resize(setHeight);
	},
	getDifferTime:function(sTime, eTime){
		var s = (ccms.getSecond(eTime) - ccms.getSecond(sTime))/1000;
		if(s > 0){
			var _s = s % 60,
				_m = (s - _s) / 60,
				_h = parseInt(_m / 60),
				_d = parseInt(_h / 24);
			_h = _h % 24;
			_m = _m % 60;
			return{d:_d, h: _h, m: _m, s: _s}
		}else{
			return 'false';
		}
	},
	filterTags: function(str){
		return str.replace(/<[^>#]+>/g,'');//标签中有#的为变量，不需要过滤
	}
}




