var jobStateIcons={}
jobStateIcons.stateimage1=root+"images/graph/news/warning.gif";
jobStateIcons.executingImage =root+"images/graph/news/lc_wait_s.png";
jobStateIcons.executedStopImage = root+"images/graph/news/executed_stop.png";
jobStateIcons.executedSuccessImage = root+"images/graph/news/state_executed.png";
jobStateIcons.executedErrorImage = root+"images/graph/news/break_pro_execute.png";
jobStateIcons.executedSkipImage =root+"images/graph/news/state_skip.png";
jobStateIcons.timeOutImage = root+"images/graph/news/timeout.png";
jobStateIcons.successIamge =root+"images/graph/news/state_executed.png";
jobStateIcons.errorIamge = root+"images/graph/news/break_pro_execute.png";
jobStateIcons.skipImage = root+"images/graph/news/state_skip.png";
jobStateIcons.skip10Image = root+"images/graph/news/state_skip.png";
jobStateIcons.wait18Image = root+"images/graph/news/warning.gif";

var status_executing=11;
var status_executed_success=21;
var status_executed_error=22;
var status_stop=23;
var status_skip=24;
var executed_success=51;
var executed_error=52;
var executed_stop=53;
var executed_skip=54;
var executed_skip10=10;
var wait_opration=18; 


function getStateImage(a) {
    var b = null;
    switch (a) {
        case 11:
        b = jobStateIcons.executingImage;
        break;
        case 21:
        b = jobStateIcons.executedSuccessImage;
        break;
        case 22:
        b = jobStateIcons.executedErrorImage;
        break;
        case 23:
        b = jobStateIcons.executedStopImage;
        break;
        case 24:
        b = jobStateIcons.executedSkipImage;
        break;
        case 51:
        b = jobStateIcons.successIamge;
        break;
        case 52:
        b = jobStateIcons.errorIamge;
        break;
        case 53:
        b = jobStateIcons.executedStopImage;
        break;
        case 54:
        b = jobStateIcons.skipImage;
        break;
        case 10:
        b = jobStateIcons.skip10Image;
        break;
        case 18:
        b = jobStateIcons.timeOutImage;
        break;
        default:
        b = jobStateIcons.stateimage1;
        break

    }
    return b

}
function disabledToForm(flag){
	 if(flag){
		if($(".disableToForm").length==0){
		   var $div=$('<div class="disableToForm"></div>');	
		   $(".marketingCampNodeMain").append($div);
		   $(".disableToForm").show();
		}	
		$(".submitNodeData").css("visibility","hidden");
	 }else{
		$(".disableToForm").hide();
		$(".submitNodeData").css("visibility","visible");
	 }
}