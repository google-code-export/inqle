
qx.Class.define("Application",
{
  extend : qx.application.Gui,

  members :
  {
    main : function()
    {
	alert('main()...');

      // Call super class
      this.base(arguments);

      // Create button
      var button1 = new qx.ui.form.Button("First Button", "./button.png");

      // Set button location
      button1.setTop(50);
      button1.setLeft(50);

      // Add button to document
      button1.addToDocument();

      // Add an event listener
      button1.addEventListener("execute", function(e) {
        alert("Hello World!");
      });
      
      google.load('visualization', '1', {'packages':['motionchart']});
     google.setOnLoadCallback(drawChart);
     function drawChart() {
    	 alert('Callback happened.');
       var json =
       {
    		   cols:[
    		   {id:"Model",label:"Model",type:"string"},
    		   {id:"Date",label:"Date",type:"date"},
    		   {id:"CO2",label:"CO2",type:"number"},
    		   {id:"Temperature",label:"Temperature",type:"number"}
    		   ],

    		   rows:[
	    		   {c:[
	    		   	{v:"Model1"},
	    		   	{v:new Date(1970,0,14,16,5,45)},
	    		   	{v:377},
	    		   	{v:22}
	    		   ]},
	    		   {c:[
	        		   {v:"Model1"},
	        		   {v:new Date(2009,3,10,21,59,19)},
	        		   {v:400},
	        		   {v:24}
	        	   ]}
	          ]
	     };
	alert('Creating data table from: ' + json);
       var dt= new google.visualization.DataTable(json);
       var chart = new google.visualization.MotionChart(document.getElementById('motionchart'));
       chart.draw(dt, {width: 500, height: 500, is3D: true, title:
'Application Usage'});

     }
	}
  }
});
