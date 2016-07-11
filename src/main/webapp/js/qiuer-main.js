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
			menuHtml +=  "<li><a href=\"" + g_menudata[g_index].href +"\"><i class=\""+  g_menudata[g_index].menuclass +"\"></i><span>"+  g_menudata[g_index].menuname + "</span>";
			g_index += 1;
			menuHtml += addsubmenu();
		} 
		else
		{
			menuHtml +=  "<li><a href=\"" + g_menudata[g_index].href +"\" class=\"qiuer-menuitem\"><i class=\""+  g_menudata[g_index].menuclass +"\"></i><span>"+  g_menudata[g_index].menuname + "</span>";
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
					sidemenuHtml +=  "<a href=\"" + g_menudata[g_index].href +"\"><i class=\""+  g_menudata[g_index].menuclass +"\"></i><span>"+ g_menudata[g_index].menuname + "</span>";
					g_index += 1;
					sidemenuHtml += addsubmenu(); 
				}
				else
				{
					sidemenuHtml +=  "<a href=\"" + g_menudata[g_index].href +"\" class=\"qiuer-menuitem\"><i class=\""+  g_menudata[g_index].menuclass +"\"></i><span>"+ g_menudata[g_index].menuname + "</span>";
					g_index += 1;
					sidemenuHtml +=  "</a>";
				}
	     	}
	     } 
	     x.innerHTML=sidemenuHtml;    //改变内容 
	}
