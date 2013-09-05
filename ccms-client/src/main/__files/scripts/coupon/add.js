
//$(function(){
	$('#startTime').datepicker({
		//dateFormat:'yy-mm-dd 00:00:00',
		showOtherMonths:true,
		selectOtherMonths:false,
		changeMonth:true,
		changeYear:true,
		minDate:new Date(),
		onClose:function(selectedDate){
			if(selectedDate){
				var obj = $("#endTime");
				$(this).val(this.value.split(' ')[0]+' 00:00:00');
				obj.datepicker("option","minDate",selectedDate);
				if(obj.val()){
					obj.val(obj.val()+' 23:59:59');
				}
			}
		}
	});
	$('#endTime').datepicker({
		//dateFormat:'yy-mm-dd 23:59:59',
		showOtherMonths:true,
		selectOtherMonths:false,
		changeMonth:true,
		changeYear:true,
		minDate:new Date(),
		onClose:function(selectedDate){
			if(selectedDate){
				var obj = $("#startTime");
				$(this).val(this.value.split(' ')[0]+' 23:59:59');
				obj.datepicker("option","maxDate",selectedDate);
				if(obj.val()){
					obj.val(obj.val()+' 00:00:00');
				}
			}
		}
	});
//});