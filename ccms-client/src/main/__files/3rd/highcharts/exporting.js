/*
 Highcharts JS v2.3.5 (2012-12-19)
 Exporting module

 (c) 2010-2011 Torstein Hønsi

 License: www.highcharts.com/license
*/
(function(e){function y(a){for(var b=a.length;b--;)typeof a[b]==="number"&&(a[b]=Math.round(a[b])-0.5);return a}var z=e.Chart,u=e.addEvent,B=e.removeEvent,j=e.createElement,v=e.discardElement,t=e.css,l=e.merge,k=e.each,o=e.extend,C=Math.max,i=document,D=window,E=e.isTouchDevice,A=e.Renderer.prototype.symbols,w=e.getOptions();o(w.lang,{downloadPNG:"下载PNG图像",downloadJPEG:"下载JPEG图像",downloadPDF:"下载PDF文档",downloadSVG:"下载SVG矢量图像",exportButtonTitle:"导出图像或文档",
printButtonTitle:"打印图表"});w.navigation={menuStyle:{border:"1px solid #A0A0A0",background:"#FFFFFF"},menuItemStyle:{padding:"0 5px",background:"none",color:"#303030",fontSize:E?"14px":"11px"},menuItemHoverStyle:{background:"#4572A5",color:"#FFFFFF"},buttonOptions:{align:"right",backgroundColor:{linearGradient:[0,0,0,20],stops:[[0.4,"#F7F7F7"],[0.6,"#E3E3E3"]]},borderColor:"#B0B0B0",borderRadius:3,borderWidth:1,height:20,hoverBorderColor:"#909090",hoverSymbolFill:"#81A7CF",hoverSymbolStroke:"#4572A5",
symbolFill:"#E0E0E0",symbolStroke:"#A0A0A0",symbolX:11.5,symbolY:10.5,verticalAlign:"top",width:24,y:10}};w.exporting={type:"image/png",url:"http://export.highcharts.com/",width:800,buttons:{exportButton:{symbol:"exportIcon",x:-10,symbolFill:"#A8BF77",hoverSymbolFill:"#768F3E",_id:"exportButton",_titleKey:"exportButtonTitle",menuItems:[{textKey:"downloadPNG",onclick:function(){this.exportChart()}},{textKey:"downloadJPEG",onclick:function(){this.exportChart({type:"image/jpeg"})}},{textKey:"downloadPDF",
onclick:function(){this.exportChart({type:"application/pdf"})}},{textKey:"downloadSVG",onclick:function(){this.exportChart({type:"image/svg+xml"})}}]},printButton:{symbol:"printIcon",x:-36,symbolFill:"#B5C9DF",hoverSymbolFill:"#779ABF",_id:"printButton",_titleKey:"printButtonTitle",onclick:function(){this.print()}}}};e.post=function(a,b){var c,d;d=j("form",{method:"post",action:a,enctype:"multipart/form-data"},{display:"none"},i.body);for(c in b)j("input",{type:"hidden",name:c,value:b[c]},null,d);
d.submit();v(d)};o(z.prototype,{getSVG:function(a){var b=this,c,d,f,g=l(b.options,a);if(!i.createElementNS)i.createElementNS=function(a,b){return i.createElement(b)};a=j("div",null,{position:"absolute",top:"-9999em",width:b.chartWidth+"px",height:b.chartHeight+"px"},i.body);o(g.chart,{renderTo:a,forExport:!0});g.exporting.enabled=!1;g.chart.plotBackgroundImage=null;g.series=[];k(b.series,function(a){f=l(a.options,{animation:!1,showCheckbox:!1,visible:a.visible});f.isInternal||g.series.push(f)});c=
new e.Chart(g);k(["xAxis","yAxis"],function(a){k(b[a],function(b,d){var f=c[a][d],g=b.getExtremes(),e=g.userMin,g=g.userMax;(e!==void 0||g!==void 0)&&f.setExtremes(e,g,!0,!1)})});d=c.container.innerHTML;g=null;c.destroy();v(a);d=d.replace(/zIndex="[^"]+"/g,"").replace(/isShadow="[^"]+"/g,"").replace(/symbolName="[^"]+"/g,"").replace(/jQuery[0-9]+="[^"]+"/g,"").replace(/isTracker="[^"]+"/g,"").replace(/url\([^#]+#/g,"url(#").replace(/<svg /,'<svg xmlns:xlink="http://www.w3.org/1999/xlink" ').replace(/ href=/g,
" xlink:href=").replace(/\n/," ").replace(/<\/svg>.*?$/,"</svg>").replace(/&nbsp;/g," ").replace(/&shy;/g,"­").replace(/<IMG /g,"<image ").replace(/height=([^" ]+)/g,'height="$1"').replace(/width=([^" ]+)/g,'width="$1"').replace(/hc-svg-href="([^"]+)">/g,'xlink:href="$1"/>').replace(/id=([^" >]+)/g,'id="$1"').replace(/class=([^" ]+)/g,'class="$1"').replace(/ transform /g," ").replace(/:(path|rect)/g,"$1").replace(/style="([^"]+)"/g,function(a){return a.toLowerCase()});d=d.replace(/(url\(#highcharts-[0-9]+)&quot;/g,
"$1").replace(/&quot;/g,"'");d.match(/ xmlns="/g).length===2&&(d=d.replace(/xmlns="[^"]+"/,""));return d},exportChart:function(a,b){var c=this.options.exporting,d=this.getSVG(l(c.chartOptions,b)),a=l(c,a);e.post(a.url,{filename:a.filename||"chart",type:a.type,width:a.width,scale:a.scale||2,svg:d})},print:function(){var a=this,b=a.container,c=[],d=b.parentNode,f=i.body,g=f.childNodes;if(!a.isPrinting)a.isPrinting=!0,k(g,function(a,b){if(a.nodeType===1)c[b]=a.style.display,a.style.display="none"}),
f.appendChild(b),D.print(),setTimeout(function(){d.appendChild(b);k(g,function(a,b){if(a.nodeType===1)a.style.display=c[b]});a.isPrinting=!1},1E3)},contextMenu:function(a,b,c,d,f,g){var e=this,p=e.options.navigation,i=p.menuItemStyle,q=e.chartWidth,r=e.chartHeight,s="cache-"+a,h=e[s],m=C(f,g),x,n,l;if(!h)e[s]=h=j("div",{className:"highcharts-"+a},{position:"absolute",zIndex:1E3,padding:m+"px"},e.container),x=j("div",null,o({MozBoxShadow:"3px 3px 10px #888",WebkitBoxShadow:"3px 3px 10px #888",boxShadow:"3px 3px 10px #888"},
p.menuStyle),h),n=function(){t(h,{display:"none"})},u(h,"mouseleave",function(){l=setTimeout(n,500)}),u(h,"mouseenter",function(){clearTimeout(l)}),k(b,function(a){if(a){var b=j("div",{onmouseover:function(){t(this,p.menuItemHoverStyle)},onmouseout:function(){t(this,i)},innerHTML:a.text||e.options.lang[a.textKey]},o({cursor:"pointer"},i),x);b.onclick=function(){n();a.onclick.apply(e,arguments)};e.exportDivElements.push(b)}}),e.exportDivElements.push(x,h),e.exportMenuWidth=h.offsetWidth,e.exportMenuHeight=
h.offsetHeight;a={display:"block"};c+e.exportMenuWidth>q?a.right=q-c-f-m+"px":a.left=c-m+"px";d+g+e.exportMenuHeight>r?a.bottom=r-d-m+"px":a.top=d+g-m+"px";t(h,a)},addButton:function(a){function b(){r.attr(k);q.attr(m)}var c=this,d=c.renderer,f=l(c.options.navigation.buttonOptions,a),e=f.onclick,i=f.menuItems,p=f.width,j=f.height,q,r,s,h,a=f.borderWidth,m={stroke:f.borderColor},k={stroke:f.symbolStroke,fill:f.symbolFill},n=f.symbolSize||12;if(!c.btnCount)c.btnCount=0;h=c.btnCount++;if(!c.exportDivElements)c.exportDivElements=
[],c.exportSVGElements=[];f.enabled!==!1&&(q=d.rect(0,0,p,j,f.borderRadius,a).align(f,!0).attr(o({fill:f.backgroundColor,"stroke-width":a,zIndex:19},m)).add(),s=d.rect(0,0,p,j,0).align(f).attr({id:f._id,fill:"rgba(255, 255, 255, 0.001)",title:c.options.lang[f._titleKey],zIndex:21}).css({cursor:"pointer"}).on("mouseover",function(){r.attr({stroke:f.hoverSymbolStroke,fill:f.hoverSymbolFill});q.attr({stroke:f.hoverBorderColor})}).on("mouseout",b).on("click",b).add(),i&&(e=function(){b();var a=s.getBBox();
c.contextMenu("menu"+h,i,a.x,a.y,p,j)}),s.on("click",function(){e.apply(c,arguments)}),r=d.symbol(f.symbol,f.symbolX-n/2,f.symbolY-n/2,n,n).align(f,!0).attr(o(k,{"stroke-width":f.symbolStrokeWidth||1,zIndex:20})).add(),c.exportSVGElements.push(q,s,r))},destroyExport:function(){var a,b;for(a=0;a<this.exportSVGElements.length;a++)b=this.exportSVGElements[a],b.onclick=b.ontouchstart=null,this.exportSVGElements[a]=b.destroy();for(a=0;a<this.exportDivElements.length;a++)b=this.exportDivElements[a],B(b,
"mouseleave"),this.exportDivElements[a]=b.onmouseout=b.onmouseover=b.ontouchstart=b.onclick=null,v(b)}});A.exportIcon=function(a,b,c,d){return y(["M",a,b+c,"L",a+c,b+d,a+c,b+d*0.8,a,b+d*0.8,"Z","M",a+c*0.5,b+d*0.8,"L",a+c*0.8,b+d*0.4,a+c*0.4,b+d*0.4,a+c*0.4,b,a+c*0.6,b,a+c*0.6,b+d*0.4,a+c*0.2,b+d*0.4,"Z"])};A.printIcon=function(a,b,c,d){return y(["M",a,b+d*0.7,"L",a+c,b+d*0.7,a+c,b+d*0.4,a,b+d*0.4,"Z","M",a+c*0.2,b+d*0.4,"L",a+c*0.2,b,a+c*0.8,b,a+c*0.8,b+d*0.4,"Z","M",a+c*0.2,b+d*0.7,"L",a,b+d,a+
c,b+d,a+c*0.8,b+d*0.7,"Z"])};z.prototype.callbacks.push(function(a){var b,c=a.options.exporting,d=c.buttons;if(c.enabled!==!1){for(b in d)a.addButton(d[b]);u(a,"destroy",a.destroyExport)}})})(Highcharts);
