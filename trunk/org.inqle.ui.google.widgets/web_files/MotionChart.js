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
alert('loading MotionChart...');
qx.Class.define( "org.inqle.ui.google.jsapi.MotionChart", {
    extend: qx.ui.layout.CanvasLayout,
    
    construct: function( id ) {
        this.base( arguments );
        this.setHtmlAttribute( "id", id );
        this._id = id;
        this._chart = null;
//        if( GBrowserIsCompatible() ) {
//            this._geocoder = new GClientGeocoder();
        
        this.addEventListener( "changeHeight", this._doResize, this );
        this.addEventListener( "changeWidth", this._doResize, this );
        
        google.load('visualization', '1', {'packages':['motionchart']});
        google.setOnLoadCallback(this.onLoadCallback);
//        function drawChart() {
//        	this._chart = new google.visualization.MotionChart(document.getElementById(id));
//	        this.addEventListener( "changeHeight", this._doResize, this );
//	        this.addEventListener( "changeWidth", this._doResize, this );
//        }
//      }
        this.info('Constructed');
//        this.drawMotionChart();
    },
    
//    properties : {
//        address : {
//            init : "",
//            apply : "load"
//        }
//    },
    
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
        
        onLoadCallback : function() {
        	alert('onLoadCallback() called');
        	this.drawMotionChart();
        },
        
        drawMotionChart : function() {
        	this.info('drawMotionChart...');
//            var current = this.getAddress();
//            if( GBrowserIsCompatible() && current != null && current != "" ) {
                qx.ui.core.Widget.flushGlobalQueues();
                if( this._chart == null ) {
                    this._chart = new google.visualization.MotionChart(document.getElementById(this._id));
//                    this._chart.addControl( new GSmallMapControl() );
//                    this._chart.addControl( new GMapTypeControl() );
//                    GEvent.bind( this._chart, "click", this, this._doActivate );
//                    GEvent.bind( this._chart, "moveend", this, this._onMapMove );
                    
                }
    	        
                var data = new google.visualization.DataTable();
                data.addRows(6);
                data.addColumn('string', 'Fruit');
                data.addColumn('date', 'Date');
                data.addColumn('number', 'Sales');
                data.addColumn('number', 'Expenses');
                data.addColumn('string', 'Location');
                data.setValue(0, 0, 'Apples');
                data.setValue(0, 1, new Date (1988,0,1));
                data.setValue(0, 2, 1000);
                data.setValue(0, 3, 300);
                data.setValue(0, 4, 'East');
                data.setValue(1, 0, 'Oranges');
                data.setValue(1, 1, new Date (1988,0,1));
                data.setValue(1, 2, 950);
                data.setValue(1, 3, 200);
                data.setValue(1, 4, 'West');
                data.setValue(2, 0, 'Bananas');
                data.setValue(2, 1, new Date (1988,0,1));
                data.setValue(2, 2, 300);
                data.setValue(2, 3, 250);
                data.setValue(2, 4, 'West');
                data.setValue(3, 0, 'Apples');
                data.setValue(3, 1, new Date(1988,1,1));
                data.setValue(3, 2, 1200);
                data.setValue(3, 3, 400);
                data.setValue(3, 4, "East");
                data.setValue(4, 0, 'Oranges');
                data.setValue(4, 1, new Date(1988,1,1));
                data.setValue(4, 2, 900);
                data.setValue(4, 3, 150);
                data.setValue(4, 4, "West");
                data.setValue(5, 0, 'Bananas');
                data.setValue(5, 1, new Date(1988,1,1));
                data.setValue(5, 2, 788);
                data.setValue(5, 3, 617);
                data.setValue(5, 4, "West");
                
                var chart = this._chart;
                this.chart.draw(data, {width: 600, height:300});
//            }
        },
        
        _doResize : function() {
            qx.ui.core.Widget.flushGlobalQueues();
            if( this._chart != null ) {
                this._chart.checkResize();
            }
        }
    }
    
} );