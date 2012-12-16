qx.Class.define( "org.inqle.ui.google.jsapi.MotionChart", {
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
                    this._chart = new google.visualization.MotionChart(document.getElementById(this._id));                    
                }
                
                var dataTable  = new google.visualization.DataTable(data);
                var options = {};
                if (this.getWidgetOptions()) {
                	options = eval('(' + this.getWidgetOptions() + ')');
                }
                this._chart.draw(dataTable, options);
        },
        
//        onLoadCallback : function() {
//        	alert('onLoadCallback() called');
//        	drawMotionChart();
//        },
        
        _doResize : function() {
//            qx.ui.core.Widget.flushGlobalQueues();
//            if( this._chart != null ) {
//                this._chart.checkResize();
//            }
        }
    }
    
} );