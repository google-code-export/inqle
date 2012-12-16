qx.Class.define( "org.inqle.ui.google.jsapi.AreaChart", {
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
        selectedRow : {
        	init : "",
            apply : ""
        },
        selectedColumn : {
        	init : "",
            apply : ""
        },
        selectedValue : {
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
	    	qx.ui.core.Widget.flushGlobalQueues();
	    	var data = eval('(' + this.getWidgetData() + ')');
	        if( this._chart == null ) {
	            this._chart = new google.visualization.AreaChart(document.getElementById(this._id));
//	            google.visualization.events.addListener(this._chart, 'select', function() {
//	                var selection = this._chart.getSelection();
//	                alert('You selected ' + selection);
//	              });
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
            	var selArray = chart.getSelection();
            	var selObj = selArray[0];
            	var selection = dataTable.getValue(selObj.row, 0) + "," + dataTable.getColumnId(selObj.column) + "," + dataTable.getValue(selObj.row, selObj.column);
            	this.selectedItem = selection;
            	this.selectedRow = dataTable.getValue(selObj.row, 0);
            	this.selectedColumn = dataTable.getColumnId(selObj.column);
            	this.selectedValue = dataTable.getValue(selObj.row, selObj.column);

            	//fire selection event
            	var req = org.eclipse.swt.Request.getInstance();
            	req.addParameter(widgetId + ".selectedItem", this.selectedItem);
            	req.addParameter(widgetId + ".selectedRow", this.selectedRow);
            	req.addParameter(widgetId + ".selectedColumn", this.selectedColumn);
            	req.addParameter(widgetId + ".selectedValue", this.selectedValue);
            	req.addEvent( "org.eclipse.swt.events.widgetSelected", widgetId );
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