;(function($){
	var ccmsAt = {
		getInputPositon: function (elem, atStr) {
			if (document.selection) {   //IE Support
				elem.focus();
				var Sel = document.selection.createRange(),
					left = Sel.boundingLeft,
					top = Sel.boundingTop,
					bottom = Sel.boundingTop + Sel.boundingHeight;
				
				/* IE下获取光标的位置 */
				Sel.moveStart('character', -elem.value.length);
				var text = Sel.text, index = 0;
				for (var i = 0; i < elem.value.length; i++) {
					if (elem.value.substring(0, i + 1) == text.substring(text.length - i - 1, text.length)) {
						index = i + 1;
					}
				}
				
				return {
					left: left,
					top: top,
					bottom: bottom,
					index :index
				};
			} else {
				var that = this;
				var cloneDiv = '{$clone_div}', cloneLeft = '{$cloneLeft}', cloneFocus = '{$cloneFocus}', cloneRight = '{$cloneRight}';
				var none = '<span style="white-space:pre-wrap;"> </span>';
				var div = elem[cloneDiv] || document.createElement('div'), focus = elem[cloneFocus] || document.createElement('span');
				var text = elem[cloneLeft] || document.createElement('span');
				var offset = that._offset(elem), index = this._getFocus(elem), focusOffset = { left: 0, top: 0 };

				if (!elem[cloneDiv]) {
					elem[cloneDiv] = div, elem[cloneFocus] = focus;
					elem[cloneLeft] = text;
					div.appendChild(text);
					div.appendChild(focus);
					document.body.appendChild(div);
					focus.innerHTML = '|';
					focus.style.cssText = 'display:inline-block;width:0px;overflow:hidden;z-index:-100;word-wrap:break-word;word-break:break-all;';
					div.className = this._cloneStyle(elem);
					div.style.cssText = 'visibility:hidden;display:inline-block;position:absolute;z-index:-100;word-wrap:break-word;word-break:break-all;overflow:hidden;';
				};
				div.style.left = this._offset(elem).left + "px";
				div.style.top = this._offset(elem).top + "px";
				var strTmp = elem.value.substring(0, index).replace(/</g, '<').replace(/>/g, '>').replace(/\n/g, '<br/>').replace(/\s/g, none);
				strTmp = strTmp.substring(0,strTmp.lastIndexOf(atStr));
				
				text.innerHTML = strTmp;

				focus.style.display = 'inline-block';
				try { focusOffset = this._offset(focus); } catch (e) { };
				focus.style.display = 'none';
				return {
					left: focusOffset.left,
					top: focusOffset.top,
					bottom: focusOffset.bottom,
					index: index
				};
			}
		},

		// 克隆元素样式并返回类
		_cloneStyle: function (elem, cache) {
			if (!cache && elem['${cloneName}']) return elem['${cloneName}'];
			var className, name, rstyle = /^(number|string)$/;
			var rname = /^(content|outline|outlineWidth)$/; //Opera: content; IE8:outline && outlineWidth
			var cssText = [], sStyle = elem.style;

			for (name in sStyle) {
				if (!rname.test(name)) {
					val = this._getStyle(elem, name);
					if (val !== '' && rstyle.test(typeof val)) { // Firefox 4
						name = name.replace(/([A-Z])/g, "-$1").toLowerCase();
						cssText.push(name);
						cssText.push(':');
						cssText.push(val);
						cssText.push(';');
					};
				};
			};
			cssText = cssText.join('');
			elem['${cloneName}'] = className = 'clone' + (new Date).getTime();
			this._addHeadStyle('.' + className + '{' + cssText + '}');
			return className;
		},

		// 向页头插入样式
		_addHeadStyle: function (content) {
			var style = this._style[document];
			if (!style) {
				style = this._style[document] = document.createElement('style');
				document.getElementsByTagName('head')[0].appendChild(style);
			};
			style.styleSheet && (style.styleSheet.cssText += content) || style.appendChild(document.createTextNode(content));
		},
		_style: {},

		// 获取最终样式
		_getStyle: 'getComputedStyle' in window ? function (elem, name) {
			return getComputedStyle(elem, null)[name];
		} : function (elem, name) {
			return elem.currentStyle[name];
		},

		// 获取光标在文本框的位置
		_getFocus: function (elem) {
			var index = 0;
			if (document.selection) {// IE Support
				elem.focus();
				var Sel = document.selection.createRange();
				if (elem.nodeName === 'TEXTAREA') {//textarea
					var Sel2 = Sel.duplicate();
					Sel2.moveToElementText(elem);
					var index = -1;
					while (Sel2.inRange(Sel)) {
						Sel2.moveStart('character');
						index++;
					};
				}
				else if (elem.nodeName === 'INPUT') {// input
					Sel.moveStart('character', -elem.value.length);
					index = Sel.text.length;
				}
			}
			else if (elem.selectionStart || elem.selectionStart == '0') { // Firefox support
				index = elem.selectionStart;
			}
			return (index);
		},

		// 获取元素在页面中位置
		_offset: function (elem) {
			var box = elem.getBoundingClientRect(), doc = elem.ownerDocument, body = doc.body, docElem = doc.documentElement;
			var clientTop = docElem.clientTop || body.clientTop || 0, clientLeft = docElem.clientLeft || body.clientLeft || 0;
			var top = box.top + (self.pageYOffset || docElem.scrollTop) - clientTop, left = box.left + (self.pageXOffset || docElem.scrollLeft) - clientLeft;
			return {
				left: left,
				top: top,
				right: left + box.width,
				bottom: top + box.height
			};
		},
		//hacked by Fai
		getData: function(kw, ary){
			var contHtml = '', length = 0;
			$.each(ary, function(i ,n){
				if(kw == '' || new RegExp(kw,'i').test(n.content)){
					var curClass = length == 0 ? ' class="cur"' : '';
					contHtml += '<li'+curClass+'>'+n.content+'</li>';
					length ++;
				}
			});
			return contHtml;
		},
		//把值插入文本框中
		setVal: function(elem, inseartVal, curPos, inseartPos){ // elem -- 文本框 ; inseartVal -- 插入的text ; curPos -- textarea之前的光标位置 ; inseartPos -- 需要插入的字符的位置,没有则在curPos处
			var editorval = elem.value,
				inseartPos = typeof inseartPos != 'undefined' ? inseartPos : curPos;
			elem.value = editorval.substring(0, inseartPos) + inseartVal + editorval.substring(curPos);
			//让光标移动到指定的位置
			if (document.selection){
				var txrRange = elem.createTextRange(),
					lastStr = editorval.substring(0,inseartPos);
				//IE下计算得到的光标位置会把\n计算进去，故要减去
				txrRange.moveStart('character',inseartPos + inseartVal.length - (lastStr.match(/\n/g) ? lastStr.match(/\n/g).length : 0)); 
				txrRange.collapse(true);  
				txrRange.select();
			}else{
				elem.select();  
				elem.selectionStart = inseartPos + inseartVal.length;  
				elem.selectionEnd = inseartPos + inseartVal.length;  
			}
		}
	};
	window.ccmsAt = ccmsAt;
	
	$.fn.ccmsAt = function(opts){
		return this.each(function(){
			opts = $.extend({
				at: '@',
				showAllListId: '',
				showAllListStyle: '',//格式为标签上的style的写法
				maxCount: '', //用html标签的属性 maxlength 来限制最大输入字数的话，当用户用 at 插入值的时候，就限制不了了，所以用js也判断下，因为maxlength的体验较好，故两种方法一起使用
				counter: 'atCounter',
				data: []
			}, opts);
			
			//如果有绑定过ccmsAt则跳出，data 不一样的话，更换数据
			var data = opts.data;
			if($(this).data('ccmsAt')){
				// if(JSON.stringify($(this).data('data')) != JSON.stringify(data)){
					// $(this).data('data', data);
				// }
				$(this).data('data', data);
				return false;
			}
			$(this).data({ccmsAt: true, data: data});
			
			
			var showFlg = false,
				_target = !$('#at_content').length ? $('<div id="at_content" class="at_content"><ul></ul></div>').appendTo('body') : $('#at_content'),
				classIndex = 0,
				editor = this,
				curPos = 0,
				maxCount = opts.maxCount,
				hideCont = function(){
					showFlg = false;
					_target.hide();
					if(_target.data('showAll')) _target.data('showAll',false);
				};
			$(this).bind('keyup focus click', function(e){
				if(showFlg && (e.keyCode == 38 || e.keyCode == 40)) return false;
				var p = ccmsAt.getInputPositon(this, opts.at),
					editorval = this.value,
					editorLen = editorval.length;
				
				
				//输入的字符个数 及字符限制
				$('#' + opts.counter).text(maxCount ? (editorLen > maxCount ? maxCount : editorLen) : editorLen);
				if(maxCount && editorLen > maxCount){
					this.value = editorval.substring(0, maxCount);
				}
				
				
				curPos = p.index;
				var atStr = editorval.substring(0, curPos),
					lastAtIndex = atStr.lastIndexOf(opts.at);
				atStr = atStr.substring(atStr.lastIndexOf(opts.at), curPos);

				//截取的内容中没有@/有换行/空白/,/;/则不提示 
				if(lastAtIndex < 0 || /[\s|\r]/.test(atStr)){
					hideCont();
					return false;
				}
				
				classIndex = 0;
				var resultData = ccmsAt.getData(atStr.substring(1), $(editor).data('data'));
				if(resultData){
					//_target.css({left: p.left, top: p.bottom - editor.scrollTop, display: 'block'}).find('ul').html(resultData);
					_target.attr('style', 'left:' + p.left + 'px;top:' + (p.bottom - editor.scrollTop) + 'px;display:block').find('ul').html(resultData);
					showList(_target, lastAtIndex);
					_target.data('showAll', false);
					showFlg = true;
				}else{
					hideCont();
				}
				
			}).keydown(function(e){
				if(showFlg){
					var eventFlg = true,
						currKey = e.keyCode || e.which, //38上  40 下
						_li = _target.find('li'),
						liHeight = _li.outerHeight(true),
						maxLength = _target.height() / liHeight - 1;

					if(currKey == 38 || currKey == 40){
						if(currKey == 40){
							classIndex ++;
							if(classIndex >= _li.length) classIndex = 0;
						}else{
							classIndex --;
							if(classIndex < 0) classIndex = _li.length - 1;
						}
						_li.eq(classIndex).addClass('cur').siblings().removeClass('cur');
						
						//设置超出列表的偏移
						if(classIndex > maxLength){
							_target.scrollTop(liHeight * (classIndex - maxLength));
						}else{
							_target.scrollTop(0);
						}
						
						eventFlg = false;
					}else if(currKey == 13){
						_li.filter('.cur').click();
						eventFlg = false;
					}
					

					return eventFlg;
				}
			});
			
			//查看全部list
			if(opts.showAllListId){
				$('#'+opts.showAllListId).click(function(){
					if(!_target.data('showAll')){
						var offset = $(editor).offset();
						//_target.css({left: offset.left,top: offset.top + $(editor).outerHeight(true) + 3, display: 'block'}).find('ul').html(ccmsAt.getData('', opts.data));
						_target.attr('style', 'left:'+offset.left+'px;top:'+(offset.top + $(editor).outerHeight(true) + 3)+'px;display:block;' + opts.showAllListStyle)
							.find('ul').html(ccmsAt.getData('', $(editor).data('data')));
					
						
						showList(_target, curPos);
						showFlg = true;
						_target.data('showAll', true);
					}else{
						_target.hide();
						_target.data('showAll', false);
					}
				});
			}

			//当有多个editor时，隐藏的时候，判断是否需要隐藏，有时需要点两次，待解决
			$('body').click(function(e){
				var t = e.target || e.srcElement;
				if(showFlg && t.id != editor.id && t.id != opts.showAllListId){ //当列表显示时，点击非editor区域则隐藏列表
					hideCont();
				}
			});
	
			function showList(obj ,lastAtIndex){
				obj.find('li').click(function(){
					ccmsAt.setVal(editor, $(this).text(), curPos ,lastAtIndex);
					/*
					editor.value = editorval.substring(0, lastAtIndex) + $(this).text() + editorval.substring(p.index);
					//让光标移动到指定的位置
					if (document.selection){
						var txrRange = editor.createTextRange(),
							lastStr = editorval.substring(0,lastAtIndex);
						//IE下计算得到的光标位置会把\n计算进去，故要减去
						txrRange.moveStart('character',lastAtIndex + $(this).text().length - (lastStr.match(/\n/g) ? lastStr.match(/\n/g).length : 0)); 
						txrRange.collapse(true);  
						txrRange.select();
					}else{
						editor.select();  
						editor.selectionStart = lastAtIndex + $(this).text().length;  
						editor.selectionEnd = lastAtIndex + $(this).text().length;  
					}
					*/
				}).hover(function(){
					classIndex = $(this).index();
					$(this).addClass('cur').siblings().removeClass('cur');
				});
			}
		});
	}
})(jQuery);