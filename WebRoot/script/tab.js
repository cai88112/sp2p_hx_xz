
var whiteTab = function(myTab) {
	var index = $(myTab).attr("dataIndex");
	if (index != "true") {
		$(myTab).css("background-image", "");
	}
}

var blackTab = function(myTab) {
	var index = $(myTab).attr("dataIndex");
	if (index != "true") {
		$(myTab).css("background-image", "url(images/background.png)");
	}
}

$(".tabtil ul li").bind("mouseover", function() {
	blackTab(this);
});
$(".tabtil ul li").bind("mouseout", function() {
	whiteTab(this);
});
