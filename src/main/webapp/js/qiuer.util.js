(function($){
	$.extend({
		getUrlParam : function() {
			var ret = new Array();
			var u = window.location.search.substr(1);
			if (u != "") {
				var p = decodeURIComponent(u).split('&');
				$.each(p, function(i, item) {
					var f = item.split("=");
					ret[f[0]] = f[1];
				});
			}
			return ret;
		},
		pad : function(num, p, n) {
			var len = num.toString().length;
			while (len < n) {
				num = p + num;
				len++;
			}
			return num;
		},
		pad0 : function(num, n) {
			return $.pad(num, "0", n);
		},
		padSpace : function(num, n) {
			return $.pad(num, " ", n);
		},
		sign : function(num) {
			if (num < 0)
				return num;
			else
				return "+"+num;
		},
		makeDate : function(year, month, day) {
			return new Date(year,month-1,day);
		},
		parseDate : function(datestr, sep) {
			if (sep==undefined) {
				var arr = [];
				arr[0] = datestr.substr(0,4);
				arr[1] = datestr.substr(4,2);
				arr[2] = datestr.substr(6,2);
			} else {
				var arr = datestr.split(sep);
			}
			if (arr.length==3);
				return new Date(arr[0],arr[1]-1,arr[2]);
		},
		getDate : function(date, sep) {
			if (date==undefined) date=new Date();
			if (sep==undefined) sep="";
			if (sep.length>=3)
				return date.getFullYear()+sep.substr(0,1)+(date.getMonth()+1)+sep.substr(1,1)+date.getDate()+sep.substr(2,1);
			else
				return date.getFullYear()+sep+$.pad0(date.getMonth()+1,2)+sep+$.pad0(date.getDate(),2);
		},
		getTime : function(date, sep) {
			if (date==undefined) date=new Date();
			if (sep==undefined) sep="";
			if (sep.length>=3)
				return date.getHours()+sep.substr(0,1)+date.getMinutes()+sep.substr(1,1)+date.getSeconds()+sep.substr(2,1);
			else
				return $.pad0(date.getHours(),2)+sep+$.pad0(date.getMinutes()+1,2)+sep+$.pad0(date.getSeconds(),2);
		},
		addMonth : function(date, add) {
			var vdate = new Date(date.getFullYear(),date.getMonth()+add+1,1).valueOf()-24*60*60*1000;
			var ndate = new Date(vdate);
			var day = date.getDate();
			var nday = ndate.getDate();
			if (nday>day) nday=day;
			return new Date(ndate.getFullYear(),ndate.getMonth(),nday);
		},
		addDay : function(date, add) {
			var ndate = date;
			ndate.setDate(date.getDate()+add);
			return ndate;
		},
		addArray : function(arr, add) {
			var item = arr[add];
			if (item==undefined) item=1;
			else item++;
			arr[add] = item;
		},
		distinctArray : function(arr) {
			var dist = new Array();
			for (var item in arr)
				dist.push(item);
			return dist;
		},
		sumArray : function(arr) {
			var sum = 0;
			for (var item in arr)
				sum += arr[item];
			return sum;
		},
		thousandSep : function(numstr) {
			// return numstr.replace(/\B(?=(\d{3})+$)/g,',');
			return numstr.replace(/(?=(?!^)(?:\d{3})+(?:\.|$))(\d{3}(\.\d+$)?)/g,',$1');
		},
		other : function() {
		}
	});
})(jQuery);

