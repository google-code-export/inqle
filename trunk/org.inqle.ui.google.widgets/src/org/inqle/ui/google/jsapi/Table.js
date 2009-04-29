qx.Class.define( "org.inqle.ui.google.jsapi.Table", {
    extend: qx.ui.layout.CanvasLayout,
    
    construct: function( id ) {
        this.base( arguments );
        this.setHtmlAttribute( "id", id );
        this._id = id;
        this._chart = null;
        
        this.addEventListener( "changeHeight", this._doResize, this );
        this.addEventListener( "changeWidth", this._doResize, this );
    },
    
    properties : {
        widgetData : {
            init : "",
            apply : "load"
        },
        widgetOptions : {
            init : "",
            apply : ""
        },
        selectedItem : {
        	init : "",
            apply : ""
        }
    },
    
    members : {
        _doActivate : function() {
            var shell = null;
            var parent = this.getParent();
            while( shell == null && parent != null ) {
                if( parent.classname == "org.eclipse.swt.widgets.Shell" ) {
                    shell = parent;
                }
                parent = parent.getParent();
            }
            if( shell != null ) {
                shell.setActiveChild( this );
            }
        },
        
        load : function() {
        	alert('this._id=' + this._id);
        	
	    	qx.ui.core.Widget.flushGlobalQueues();
	    	var data = eval('(' + this.getWidgetData() + ')');
	        if( this._chart == null ) {
	            this._chart = new google.visualization.Table(document.getElementById(this._id));
	        }
	        var dataTable  = new google.visualization.DataTable(data);
	        
	        var chart = this._chart;
	        var options = {};
            if (this.getWidgetOptions()) {
            	options = eval('(' + this.getWidgetOptions() + ')');
            }
	        chart.draw(dataTable, options);
	        
	        var widgetId = this._id;
	        
	        google.visualization.events.addListener(chart, 'select', function() {
            	var row = chart.getSelection()[0].row;
            	this.selectedItem = dataTable.getValue(row, 0);
            	//fire selection event
            	var req = org.eclipse.swt.Request.getInstance();
            	req.addParameter(widgetId + ".selectedItem", this.selectedItem);
            	req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
//            	alert('Widget ID: ' + widgetId + '; Sent event for selection of: ' + this.selectedItem);
            	req.send();
	        });
        },
        
        _doResize : function() {
//            qx.ui.core.Widget.flushGlobalQueues();
//            if( this._chart != null ) {
//                this._chart.checkResize();
//            }
        }
    }
    
} );