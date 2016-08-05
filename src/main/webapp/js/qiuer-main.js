/*!
 * 
 *
 * 
 */
var g_index = 0;
var g_menudata;

 function addsubmenu()
{
	var menuHtml = "<i class=\"fa fa-angle-left pull-right\"></i></a>";
	menuHtml += "<ul class=\"treeview-menu\">";
	var maxindex = g_menudata[g_index-1].soncnt;
	for(var i=0;  i < maxindex;  i++)
	{
		if( g_menudata[g_index].soncnt > 0)
		{
			menuHtml +=  "<li><a href=\"" + g_menudata[g_index].url +"\"><i class=\""+  g_menudata[g_index].icon +"\"></i><span id=\""+g_menudata[g_index].menuid+"\">"+ g_menudata[g_index].menuname + "</span>";
			g_index += 1;
			menuHtml += addsubmenu();
		} 
		else
		{
			menuHtml +=  "<li><a href=\"" + g_menudata[g_index].url +"\" class=\"qiuer-menuitem\"><i class=\""+  g_menudata[g_index].icon +"\"></i><span id=\""+g_menudata[g_index].menuid+"\">"+ g_menudata[g_index].menuname + "</span>";
			g_index += 1;
			menuHtml +=  "</a>";
		}
		menuHtml +=  "</li>";
	}
	menuHtml +=  "</ul>";
   return menuHtml;
}
 
 function loadmenu(menuid,data){
		var x=window.document.getElementById(menuid);	 //查找元素
		var sidemenuHtml = "";  
		g_menudata = data;
	     for(; g_index < g_menudata.length;)
	     {
	     	if(g_menudata[g_index].parentid == 0)
	     	{
	     		sidemenuHtml +=  "<li class=\"treeview\">";
				if(g_menudata[g_index].soncnt > 0)
				{
					sidemenuHtml +=  "<a href=\"" + g_menudata[g_index].url +"\"><i class=\""+  g_menudata[g_index].icon +"\"></i><span id=\""+g_menudata[g_index].menuid+"\">"+ g_menudata[g_index].menuname + "</span>";
					g_index += 1;
					sidemenuHtml += addsubmenu(); 
				}
				else
				{
					sidemenuHtml +=  "<a href=\"" + g_menudata[g_index].url +"\" class=\"qiuer-menuitem\"><i class=\""+  g_menudata[g_index].icon +"\"></i><span id=\""+g_menudata[g_index].menuid+"\">"+ g_menudata[g_index].menuname + "</span>";
					g_index += 1;
					sidemenuHtml +=  "</a>";
				}
	     	}
	     } 
	     x.innerHTML=sidemenuHtml;    //改变内容 
	}

 
 function getActions(userid,menuid){
		$.ajax({
			data : {
	   				"userid":userid,
	   				"menuid":menuid
			},
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			async : true,//同步
			url : getContextPath()+"/permission/actions",
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readyState);
				alert(textStatus);
			},
			success : function(data) {
				   if(data.length>0){
					for(var i=0;i<data.length;i++){   
				 　　　　for(var key in data[i]){  
				 		if(key == "actionname"){
				          blockAction(data[i][key]); 
				         }
				        }   
					}
				   }
			}
		});
	}	   
	function blockAction(actionname)
	{
		//document.getElementById(actionname).style.display="block";
		$("#"+actionname).css("display","block");
	}
	function ReadCookie(cookieName) {
	    var theCookie = "" + document.cookie;
	    var ind = theCookie.indexOf(cookieName);
	    if(ind==-1 || cookieName=="") return "";
	    var ind1 = theCookie.indexOf(';',ind);
	    if(ind1==-1) ind1 = theCookie.length;
	    return unescape(theCookie.substring(ind+cookieName.length+1,ind1));
	}
	function SetCookie(cookieName,cookieValue,nDays) {
	    var today = new Date();
	    var expire = new Date();
	    if(nDays == null || nDays == 0) nDays = 1;
	    expire.setTime(today.getTime() + 3600000 * 24 * nDays);
	    document.cookie = cookieName + "=" + escape(cookieValue)
	        + ";expires=" + expire.toGMTString()+";path="+getContextPath();
	}
	function getContextPath(){ 
		var pathName = document.location.pathname; 
		var index = pathName.substr(1).indexOf("/"); 
		var result = pathName.substr(0,index+1); 
		return result; 
	}