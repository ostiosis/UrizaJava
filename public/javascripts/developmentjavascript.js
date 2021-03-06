	$(document).ready(function(){
	
		$(document).on('click', '.btn-success', function(event){
	
	    	var name = $('#name').val();
	    	var title = $('#title').val();
	    	var description = $('#description').val();
	    	alert( name + title + description );
	    	
	    	var route = r = jsRoutes.controllers.Development.add(name, title, description).ajax({
				success: function(data) {
					//alert(data)
			    	//$('#result').html(data);
					window.location.replace("/development/" + name);
			  	},
			  	error: function() {
			    	alert("Error!")
			  	}
			});
	    	
		});
	
		$(document).on('click', '#open-menu', function(event){
	
	    	var route = r = jsRoutes.controllers.Development.openMenu().ajax({
				success: function(data) {
					//alert(data);
			    	$('#open-result').html(data);
			    	
			  	},
			  	error: function() {
			    	alert("Error!")
			  	}
			});
	    	
		});

	    $('input[type=file]').change(function() {
			var file = this.files[0];
			uploadFile(file);
	    });
	
		if (Modernizr.draganddrop) 
		{
			// Browser supports HTML5 DnD.
		  	//alert("drag and drop");
		} 
		else 
		{
		  	// Fallback to a library solution.
			//alert("no drag and drop");
		}
		
  		// Setup the dnd listeners.
  		var dropZone = document.getElementById('drop-zone');
  		dropZone.addEventListener('dragover', handleDragOver, false);
  		dropZone.addEventListener('drop', handleFileSelect, false);

		mediaThumbnails(event);

		resizeTargets();
		createGrid();
		setDroppable();
		
		$(window).resize(function(){
			resizeTargets();
			createGrid();
		});
		
		draggableTemplate();
		
		editText();
					
	});
	
	function createGrid()
	{
		var length = $("#grid-display > .row").length;
		var height = $("#grid-display > .row").height();
		
		var htmlRow = ['<div class="row">',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    		'<div class="col-xs-1 grid-cell"></div>',
	    	'</div>'].join('\n');
		
		while (length * height < $('#result').height())
		{
			$('#grid-display').append(htmlRow);
			resizeTargets()
			length = $("#grid-display > .row").length;			
		}
		

	}
	
	function resizeTargets()
	{
		var length = ($("#overlay").width() / 12);
		
		$('.col-xs-1').each(function(){
			$(this).css('height', length);
	   	});
	   
	   	$('#grid-display > .row').each(function(){
		   	$(this).css('height', length);   
	   	});
	}
	
	//Functions
    function uploadFile(file)
    {
		//this only uploads raw file
    	var route = r = jsRoutes.controllers.Development.uploadAjax(file.name).ajax({
	    	data: file,
	    	processData: false,
            contentType: false,
            cache: false,
            type: 'POST',
			success: function(data) {
				mediaThumbnails(event);
		  	},
		  	error: function() {
		    	alert("Error!");
		  	}
		});	    
    }
	
	function mediaThumbnails(event){
		var route = r = jsRoutes.controllers.Development.showImageThumbnails("medium").ajax({
			success: function(data) {
				//alert(data);
		    	$('#media-thumbnails').html(data);
		    	draggableThumbnail();
		  	},
		  	error: function() {
		    	alert("Error!");
		  	}
		});
	}
	//TODO
	//this needs to be updated so set template and set all template children
	function setDroppable()
	{
		$('#result').droppable({
			drop:		function(event, ui){
				//alert('drop');
				var gridWidth = $('#overlay').width() / 12;
				var gridHeight = gridWidth;
				
				var helperOffset = ui.helper.offset();
				var clonedComponent = ui.helper.clone();
				
				var offsetTop = helperOffset.top - $("#result").offset().top;
				var offsetLeft = helperOffset.left - $("#result").offset().left;
				
				var divTop = parseInt((offsetTop + 5) / gridHeight);
				var divLeft = parseInt((offsetLeft + 5) / gridWidth);

				var topPosition = divTop * gridHeight;
				var leftPosition = divLeft * gridWidth;
				
				var width = 0;
				var height = 0;

				var componentId = 0;
				
				var componentType = "image";

				var elementCheck = document.createElement('element');

				var templateId = 0;				
				var pageId = $('.custom-page').attr('id');
				
				if (clonedComponent.children('.component-wrapper').length > 0)
				{
					clonedComponent.children('.component-wrapper').each(
						function(){
							
							attr = $(this).attr('id');
							if (typeof attr !== 'undefined' && attr !== false)
							{
								componentId = $(this).attr('id');
							}							
							code = $(this).html();
							componentType = $(this).attr('type');
						}
					);						
				}

				//TODO: clonedComponent.find( 'component-item' ) for multiple components
				if (clonedComponent.hasClass('component-item'))
				{
					code = clonedComponent.html();
					componentType = clonedComponent.attr('type');
					//alert("2code: " + code);
				}
				
				if (clonedComponent.hasClass("template-wrapper"))
				{
					templateId = clonedComponent.attr('id');
				}
				
				ui.helper.remove();
				
				
				
			 	var route = r = jsRoutes.controllers.Development.updateComponent(code, componentId, componentType, Math.floor(topPosition), Math.floor(leftPosition), width, height, templateId, pageId).ajax({
					success: function(data) {
			    		//alert(data);
			    		$(clonedComponent).replaceWith(data);	

			    		draggableTemplate();	
			    		editText();
			    						    	
				  	},
				  	error: function() {
				    	alert("Error!");
				  	}
				});
				
			 	$(clonedComponent).detach();
				//$(clonedComponent).css('top', topPosition);
				//$(clonedComponent).css('left', leftPosition);	

				
				$(clonedComponent).draggable({
                    helper:         "original",
                    containment:    "#result",
                    tolerance:      "fit",
                    cursor:         "move",
                    snap:			".grid-cell"
                });
				
				
				
				$(clonedComponent).appendTo( "#result" );
			}
		});
		
		
	}
	
	function draggableTemplate()
	{
		$('.template-wrapper').draggable({
            helper:         "original",
            containment:    "#result",
            tolerance:      "fit",
            cursor:         "move",
            snap:			".grid-cell"
        });
	}
	
	function draggableThumbnail()
	{
        $('.draggable-thumbnail').draggable({
            helper:			function(event){
				var result = $(this).clone();
            	
			 	var route = r = jsRoutes.controllers.Development.getImage($(this).attr('id'), "large").ajax({
					success: function(data) {
			    		$(result).html(data);					    	
				  	},
				  	error: function() {
				    	alert("Error!");
				  	}
				});
				
				return $(result);		            
            },
            containment:    "#result",
            tolerance:      "fit",
            cursor:         "move",
            revert:			true,
            snap:			".col-xs-1",
            /**
            drag: function( event, ui ) {
                var snapTolerance = $(this).draggable('option', 'snapTolerance');
                var topRemainder = ui.position.top % 68;
                var leftRemainder = ui.position.left % 63;
                
                if (topRemainder <= snapTolerance) {
                    ui.position.top = ui.position.top - topRemainder;
                }
                
                if (leftRemainder <= snapTolerance) {
                    ui.position.left = ui.position.left - leftRemainder;
                }
            }, 
            /**/
            start:  function( event, ui ){
                //zIndexTemp = $(this).zIndex();
                //$(this).zIndex( 9999 );
            },
            stop:   function( event, ui ){
                //zIndexTemp = $(this).zIndex();
                //$(this).zIndex( zIndexTemp );
            }

        });
    }
	
	function handleDragOver(event) 
	{
   		event.stopPropagation();
   		event.preventDefault();
   		event.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
	}
		
	function handleFileSelect(event) 
	{
		event.stopPropagation();
  		event.preventDefault();

  		var files = event.dataTransfer.files; // FileList object.

  		// files is a FileList of File objects. List some properties.
  		
  		var output = [];
  		
  		for (var i = 0, f; f = files[i]; i++) 
  		{
   			uploadFile(f);
   			
   			output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',
         			f.size, ' bytes, last modified: ',
               	f.lastModifiedDate ? f.lastModifiedDate.toLocaleDateString() : 'n/a',
               	'</li>');
		}
 			
  		document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
	}

	
	function editText()
	{

		$('.editable-text').popline();
	
		$('.editable-text').mouseenter(function() 
		{
			$(this).parents('.template-wrapper').draggable('disable');
			//alet('mouse enter');
		});
		
		$('.editable-text').mouseleave(function() 
		{
			$(this).parents('.template-wrapper').draggable('enable');
		});
		

	}