self.onError = null;  
currentX = currentY = 0;   
whichIt = null;   
lastScrollX = 0; lastScrollY = 0;  
NS = (document.layers) ? 1 : 0;  
IE = (document.all) ? 1: 0;  
function heartBeat(objectid) {  
if(IE) { diffY = document.body.scrollTop; diffX = document.body.scrollLeft; }  
if(NS) { diffY = self.pageYOffset; diffX = self.pageXOffset; }  
if(diffY != lastScrollY) {  
percent = .1 * (diffY - lastScrollY);  
if(percent > 0) percent = Math.ceil(percent);  
else percent = Math.floor(percent);  
if(IE) {  
objectid = objectid.split(";");   
for (i = 0; i < objectid.length; i++) eval("document.all."+objectid[i]).style.pixelTop += percent;  
}   
if(NS) {  
objectid = objectid.split(";");   
for (i = 0; i < objectid.length; i++) eval("document."+objectid[i]).top += percent;   
}   
lastScrollY = lastScrollY + percent;  
}  
if(diffX != lastScrollX) {  
percent = .1 * (diffX - lastScrollX);  
if(percent > 0) percent = Math.ceil(percent);  
else percent = Math.floor(percent);  
if(IE) {  
objectid = objectid.split(";");   
for (i = 0; i < objectid.length; i++) eval("document.all."+objectid[i]).style.pixelLeft += percent;  
}   
if(NS) {  
objectid = objectid.split(";");   
for (i = 0; i < objectid.length; i++) eval("document."+objectid[i]).left += percent;   
}   
lastScrollX = lastScrollX + percent;  
}   
}  
if(NS || IE) action = window.setInterval("heartBeat(’floater;floater1’)", 2);