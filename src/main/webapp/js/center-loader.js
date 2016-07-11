/*!
 * Center-Loader PACKAGED v1.0.0
 * http://plugins.rohitkhatri.com/center-loader/
 * MIT License
 * by Rohit Khatri
 */

$.fn.loader = function(action,spinner) {
	var action = action || 'show';
	if(action === 'show') {
		if(this.find('.loader').length == 0) {
			parent_position = this.css('position');
			this.css('position','relative');
			paddingTop = parseInt(this.css('padding-top'));
			paddingRight = parseInt(this.css('padding-right'));
			paddingBottom = parseInt(this.css('padding-bottom'));
			paddingLeft = parseInt(this.css('padding-left'));

			var loaderElement = window.document.getElementById('sidebar-loader');
			var height = 0;
			if( loaderElement.nextSibling.tagName !=  null)
			{
				height = loaderElement.nextSibling.offsetTop -  loaderElement.offsetTop ;  //高度
			}
			else
			{
				height = loaderElement.parentNode.offsetHeight -  loaderElement.offsetTop ;  //高度
			}

			$loader = $('<div class="loader"></div>').css({
				'position': 'absolute',
				'top': 0,
				'left': 0,
				'width': '100%',
				'height': height,
				'z-index':	50,
				'background-color': 'rgba(255,255,255,0.2)',
				'border-radius': '3px'
			});

			$loader.attr('parent_position',parent_position);

			$spinner = $(spinner).css({
				'position': 'absolute',
				'top': '40%',
				'left': '50%',
				'color': '#ffffff',
				'margin-top': '-'+paddingTop+'px',
				'margin-right': '-'+paddingRight+'px',
				'margin-bottom': '-'+paddingBottom+'px',
				'margin-left': '-'+paddingLeft+'px'
			});

			$loader.html($spinner);
			this.prepend($loader);
			marginTop = $spinner.height()/2;
			marginLeft = +$spinner.width()/2;
			$spinner.css({
				'margin-top': '-'+marginTop+'px',
				'margin-left': '-'+marginLeft+'px'
			});
		}
	} else if(action === 'hide') {
		this.css('position',this.find('.loader').attr('parent_position'));
		this.find('.loader').remove();
	}
};