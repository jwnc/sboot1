$(function(){
	if(isMobile()){
		adjustMobile();
	}
})

function adjustMobile(){
	$(".content").css('width','95%');
	$(".img-page").show();
}

function isMobile(){
	return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
}

function exChangeBgColor(obj){
	var hlbg = "row-bg-highlight";
	$(obj).siblings().each(function(i,o){
		if($(o).prop("class") == hlbg){
			$(o).prop("class", $(o).prop("oldclass"));
		}
	});
	var bg = $(obj).prop("class");
	if(bg == hlbg){
		$(obj).prop("class", $(obj).prop("oldclass"));
		$(obj).prop("oldclass", hlbg);
	}
	else{
		$(obj).prop("oldclass", $(obj).prop("class"));
		$(obj).prop("class", hlbg);
	}
}

function showline(obj, rowno) {
	$("#prow").text(rowno);
}

function focusInput(obj, tip){
	if($(obj).val() == tip){
		$(obj).val("").css("color","#000719");
	}
}

function blurInput(obj, tip){
	if($(obj).val() == ""){
		$(obj).val(tip).css("color","#AFADAE");
	}
}