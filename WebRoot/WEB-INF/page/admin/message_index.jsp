<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>信息管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
		<script type="text/javascript" src="../css/admin/popom.js"></script>
		<script type="text/javascript">
			$(function(){				
				initListInfo(null);				
			});
			
			function initListInfo(praData){						    
		 		$.shovePost("queryMessageListPage.do",praData,initCallBack);
		 	}
		 	
		 	function initCallBack(data){
		 	  
				$("#dataInfo").html(data);
			}
			
		
		function moveUp(_a){
			    var _row = _a.parentNode.parentNode;
			    //如果不是第一行，则与上一行交换顺序
			    var _node = _row.previousSibling;
			    if(_node == null)
			      alert("已经是第一行了");
			    while(_node && _node.nodeType != 1){
			        _node = _node.previousSibling;
			    }
			    if(_node){
			        swapNode(_row,_node);
			    }
			}
			
			function moveDown(_a){
			    var _row = _a.parentNode.parentNode;
			    //如果不是最后一行，则与下一行交换顺序
			    var _node = _row.nextSibling;
			    while(_node && _node.nodeType != 1){
			        _node = _node.nextSibling;
			    }
			    if(_node){
			        swapNode(_row,_node);
			    }
			}
			
			function swapNode(node1,node2){
			    //获取父结点
			    var _parent = node1.parentNode;
			    //获取两个结点的相对位置
			    var _t1 = node1.nextSibling;
			    var _t2 = node2.nextSibling;
			    //将node2插入到原来node1的位置
			    if(_t1)_parent.insertBefore(node2,_t1);
			    else _parent.appendChild(node2);
			    //将node1插入到原来node2的位置
			    if(_t2)_parent.insertBefore(node1,_t2);
			    else _parent.appendChild(node1);
			}
			
			//信息管理预览  houli
		 	function preview(id){
		 	  var url = "previewMessageInit.do?id="+id;
              ShowIframe("信息详情显示",url,600,450);
		 	  
		 	}
   			
   			//弹出窗口关闭 houli
	   		function close(){
	   			 ClosePop();  			  
	   		}
		
		
		//向上
       /* function upMove(indexs){
	         if(indexs == 1){
		           alert("已经第一了");return ;
	             }
	           var i = indexs - 1;	        
	           var trHtml = updatAttribute(indexs,i);
	
        }

     //向下
     function downMove(indexs){
	   if(indexs == 6){
		 alert("已经最后了");return ;
	   }
	  var i = indexs + 1;		
	  var trHtml = updateAttribute(indexs,i);
	
     }


//上移修改
function updatAttribute(indexs,i){

	
	
	//替换栏目的值
	var messagColunName=$("#messageColumName_"+i).html();
    $("#messageColumName_"+i).html($("#messageColumName_"+indexs).html());  //替换栏目的值
    $("#messageColumName_"+indexs).html(messagColunName); 
  
    
    
    //替换栏目的Id
    var messageColumId=$("#messageColumName_"+i).attr("id");
    
   $("#messageColumName_"+i).attr("id","messageColumName_"+indexs);  //替换栏目Id
    
    $("#messageColumName_"+indexs).attr("id",messageColumId);  //替换栏目Id
    
    
    //替换编辑的href
   
    var updateHref=$("#update_"+i).attr("href");  //替换编辑的值
   
    $("#update_"+i).attr("href",$("#update_"+indexs).attr("href"));  
    $("#update_"+indexs).attr("href",updateHref);  
    
    //替换编辑的Id
    var updateId=$("#update_"+i).attr("id");  //替换栏目Id
    $("#update_"+i).attr("id","update_"+indexs);  //替换栏目Id
   $("#update_"+indexs).attr("id",updateId);  //替换栏目Id
    
    //替换操作中的上移
    var upMove=$("#upMove_"+i).attr("onclick");
	var upMoveId=$("#upMove_"+i).attr("id");

	$("#upMove_"+i).attr({"onclick":"upMove("+indexs+")","id":"upMove_"+indexs});
	$("#upMove_"+indexs).attr({"onclick":upMove,"id":upMoveId});
    
	
	//替换操作中的下移
	var downMove=$("#downMove_"+i).attr("onclick");
	var downMoveId=$("#downMove_"+i).attr("id");

	$("#downMove_"+i).attr({"onclick":"downMove("+indexs+")","id":"downMove_"+indexs});
	$("#downMove_"+indexs).attr({"onclick":downMove,"id":downMoveId});

	
}

     //下移修改属性
function updateAttribute(indexs,i){
	//当前元素

	
	
	//替换栏目的值
	var messagColunName=$("#messageColumName_"+indexs).html();
    $("#messageColumName_"+indexs).html($("#messageColumName_"+i).html());  //替换栏目的值
    $("#messageColumName_"+i).html(messagColunName); 
  
    
    
    //替换栏目的Id
    var messageColumId=$("#messageColumName_"+indexs).attr("id");
    
   $("#messageColumName_"+indexs).attr("id","messageColumName_"+i);  //替换栏目Id
    
    $("#messageColumName_"+i).attr("id",messageColumId);  //替换栏目Id
    
    
    //替换编辑的href
   
    var updateHref=$("#update_"+indexs).attr("href");  //替换编辑的值
   
    $("#update_"+indexs).attr("href",$("#update_"+i).attr("href"));  
    $("#update_"+i).attr("href",updateHref);  
    
    //替换编辑的Id
    var updateId=$("#update_"+indexs).attr("id");  //替换栏目Id
    $("#update_"+indexs).attr("id","update_"+i);  //替换栏目Id
   $("#update_"+i).attr("id",updateId);  //替换栏目Id
    
    //替换操作中的上移
    var upMove=$("#upMove_"+indexs).attr("onclick");
	var upMoveId=$("#upMove_"+indexs).attr("id");

	$("#upMove_"+indexs).attr({"onclick":"upMove("+i+")","id":"upMove_"+i});
	$("#upMove_"+i).attr({"onclick":upMove,"id":upMoveId});
    
	
	//替换操作中的下移
	var downMove=$("#downMove_"+indexs).attr("onclick");
	var downMoveId=$("#downMove_"+indexs).attr("id");
	

	$("#downMove_"+indexs).attr({"onclick":"downMove("+i+")","id":"downMove_"+i});
	$("#downMove_"+i).attr({"onclick":downMove,"id":downMoveId});
		

	
}

//交换参数
function exchangeParam(curIndex, newIndex){
	var firsttemp = jsonParam[curIndex];
	var twotemp = jsonParam[newIndex];
	jsonParam[curIndex] = twotemp;
	jsonParam[newIndex] = firsttemp;
}*/
     
			
			
		
			
	
		 	
	
		 	
		</script>
	</head>
	<body>
		<div id="right">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table  border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="100" height="28"  class="main_alll_h2">
								<a href="queryMessageListInit.do">信息管理</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							
							<td width="2">
								&nbsp;
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
					<div
						style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
						<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
							<tbody>
							
							</tbody>
						</table>
						<span id="dataInfo"> </span>
					</div>
				</div>
			</div>
			</div>
	</body>
</html>
