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
        	var _inqlegv_theData = eval('(' + this.getWidgetData() + ')');
                if( this._chart == null ) {
                    this._chart = new google.visualization.MotionChart(document.getElementById(this._id));
//                	this._chart = new google.visualization.MotionChart(this.getElement());
//                    this._chart.addControl( new GSmallMapControl() );
//                    this._chart.addControl( new GMapTypeControl() );
//                    GEvent.bind( this._chart, "click", this, this._doActivate );
//                    GEvent.bind( this._chart, "moveend", this, this._onMapMove );
                    
                }
                
                var _inqlegv_dataTable  = new google.visualization.DataTable(_inqlegv_theData);
//                var _inqlegv_options = {"xZoomedIn":false,"orderedByY":false,"xZoomedDataMax":400,"sizeOption":"_UNISIZE","dimensions":{"iconDimensions":["dim0"]},"xZoomedDataMin":377,"yZoomedDataMin":22,"yZoomedIn":false,"yLambda":1,"iconKeySettings":[],"colorOption":"_UNICOLOR","duration":{"multiplier":1,"timeUnit":"D"},"xLambda":1,"stateVersion":3,"yAxisOption":"3","showTrails":true,"iconType":"BUBBLE","yZoomedDataMax":24,"xAxisOption":"2","playDuration":15,"nonSelectedAlpha":0.4,"time":"1970-01-14","orderedByX":false};
//                var _inqlegv_options = {width: 500, height: 500};
                var _inqlegv_options = {};
                this._chart.draw(_inqlegv_dataTable, _inqlegv_options);
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