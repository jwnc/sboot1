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

function getYearMonth(){
	var myDate = new Date();
	var m = myDate.getMonth();
	if(m > 8) {
		return myDate.getFullYear() + "-" + (myDate.getMonth()+1);
	}
	else {
		return myDate.getFullYear() + "-0" + (myDate.getMonth()+1);
	}
}

function getTheWeek() {
	var date = new Date();
	var year = date.getFullYear();
    var week = ((date-(new Date(year+"-01-01")))/(24*60*60*7*1000)|0) + 1;
    return date.getFullYear()+"-W"+(week < 10 ? "0" + week : "" + week);
}

var cnNamesEmojis = [
	['[微笑]','ic_weixiao@2x.png'],
	['[再见]','ic_zaijian@2x.png'],
	['[笑哭]','ic_xiaoku@2x.png'],
	['[爱慕]','ic_xianmu@2x.png'],
	['[捂脸]','ic_wulian@2x.png'],
	['[睡觉]','ic_shuijiao@2x.png'],
	['[衰]','ic_shuai@2x.png'],
	['[呕吐]','ic_outu@2x.png'],
	['[震惊]','ic_zhenjing@2x.png'],
	['[眩晕]','ic_xuanyun@2x.png'],
	['[流汗]','ic_liuhan@2x.png'],
	['[厉害]','ic_lihai@2x.png'],
	['[酷]','ic_ku@2x.png'],
	['[可怜]','ic_kelian@2x.png'],
	['[滑稽]','ic_huaji@2x.png'],
	['[哈哈]','ic_haha@2x.png'],
	['[愤怒]','ic_fennu@2x.png'],
	['[大哭]','ic_daku@2x.png'],
	['[加油]','ic_jiayou@2x.png'],
	['[祈祷]','ic_qidao@2x.png'],
	['[黑锅]','ic_heiguo@2x.png'],
	['[xjbd]','ic_xjbd@2x.png'],
	['[xjbt]','ic_xjbt@2x.png'],
	['[下课]','ic_xiake@2x.png'],
	['[mvp]','ic_mvp@2x.png'],
	['[黄牌]','ic_huangpai@2x.png'],
	['[红牌]','ic_hongpai@2x.png'],
	['[助威]','ic_zhuwei@2x.png'],
	['[背锅]','ic_beiguo@2x.png'],
	['[奖杯]','ic_jiangbei@2x.png'],
	['[饮水机]','ic_yinshuiji@2x.png'],
	['[哨子]','ic_shaozi@2x.png'],
	['[足球]','ic_zuqiu@2x.png'],
	['[篮球]','ic_lanqiu@2x.png'],
	['[投降]','ic_touxiang@2x.png'],
	['[战术板]','ic_zhanshuban@2x.png'],
	['[啤酒]','ic_pijiu@2x.png'],
	['[金牌]','ic_jinpai@2x.png'],
	['[撸串]','ic_luchuan@2x.png'],
	['[击掌]','ic_jizhang@2x.png']
];
