/*******************************************************************************
 * Copyright (c) 2002-2008 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

qx.Class.define( "org.inqle.ui.google.jsapi.ColumnChart", {
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
	    	var theData = eval('(' + this.getWidgetData() + ')');
	        if( this._chart == null ) {
	            this._chart = new google.visualization.ColumnChart(document.getElementById(this._id));
	//                	this._chart = new google.visualization.ColumnChart(this.getElement());
	//                    this._chart.addControl( new GSmallMapControl() );
	//                    this._chart.addControl( new GMapTypeControl() );
	//                    GEvent.bind( this._chart, "click", this, this._doActivate );
	//                    GEvent.bind( this._chart, "moveend", this, this._onMapMove );
	            
	        }
	        var dataTable  = new google.visualization.DataTable(theData);
	        
	        var chart = this._chart;
	        var _inqlegv_options = {};
	        chart.draw(dataTable, _inqlegv_options);
        },
        
        _doResize : function() {
//            qx.ui.core.Widget.flushGlobalQueues();
//            if( this._chart != null ) {
//                this._chart.checkResize();
//            }
        }
    }
    
} );