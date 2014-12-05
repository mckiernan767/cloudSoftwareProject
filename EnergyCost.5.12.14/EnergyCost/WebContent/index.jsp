<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>

<head>

<title>Energy Cost Reduction Tool</title>

<link rel="stylesheet" href="css/bootstrap.min.css" />
<link rel="stylesheet" href="css/datepicker.css" />
<link rel="stylesheet" href="style.css" />
<script src="jquery-1.11.1.min.js"></script>
<script src="RGraph.common.core.js"></script>
<script src="RGraph.common.dynamic.js"></script>
<script src="RGraph.gantt.js"></script>

<script src="amcharts.js" type="text/javascript"></script>
<script src="serial.js" type="text/javascript"></script>
<script type="text/javascript" src="canvasjs.min.js"></script>
<script src="https://code.jquery.com/jquery.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootstrap-datepicker.js"></script>

<%@ page import="com.cloud.*"%>
<%
	SimpleMain.simple();
	String stepLineChart = SimpleMain.cpuUsage.toString();
	int energyCost = SimpleMain.energyCost;
	String tarriffLevel = SimpleMain.tarriffLevel;
	String cpuUsage = SimpleMain.cpuUsage.toString();
	String data = SimpleMain.data.toString();
%>

<script>
	function validateForm() {
		var x = document.forms["myForm"]["date"].value;
		if (x == null || x == "") {
			alert("You should select a proper date");
			return false;
		}
	}
</script>

<script src="jquery-1.11.1.min.js"></script>
<script>
	$(document).ready(function() {
		$('#submit').click(function(event) {
			var start = $('#eventStart').val();
			var id = $('#eventID').val();

			$.get('ActionServlet', {
				eventStart : start,
				eventID : id
			}, function(responseText) {
				$('#newCost').text(responseText);
			});
		});
	});
</script>

<script>
		$(function() {
			$('.date-pick').datePicker();
			$('.dp-disable').bind(
					'click',
					function() {
						var $this = $(this);
						var whichInput = $this.attr('rel');
						var $dateInput = $('#date' + whichInput);
						var status = $dateInput.is('.dp-disabled');
						$dateInput.dpSetDisabled(!status);
						$this.text((status ? 'Disable' : 'Enable') + ' date '
								+ whichInput);
						this.blur();
						return false;
					});
		});
	</script>
	
	<%@ page import="com.cloud.*"%>

	<%
		SimpleMain.simple();
		String ganttChartString = SimpleMain.output.toString();
	%>
	
	
	<script>
		var targetId;
		var targetDuration;
		var targetStart;
		var targetCpuUsage;
		window.onload = function(e) {

			gantt_events =	<%=ganttChartString%>
		;

			var gantt = new RGraph.Gantt({
				id : 'cvs',
				data : gantt_events,
				options : {
					xmin : 0,
					xmax : 1440,
					defaultcolor : '#0c0',
					labels : [ '1h', '2h', '3h', '4h', '5h', '6h', '7h', '8h',
							'9h', '10h', '11h', '12h', '13h', '14h', '15h',
							'16h', '17h', '18h', '19h', '20h', '21h', '22h',
							'23h', '24h' ],
					borders : true,
					adjustable : true
				}

			})

			.Set(
					'vbars',
					[ [ 0, 1, 'rgba(232, 232, 232, 1)' ],
							[ 1, 1, 'rgba(204, 204, 204, 1)' ],
							[ 2, 1, 'rgba(232, 232, 232, 1)' ],
							[ 3, 1, 'rgba(204, 204, 204, 1)' ],
							[ 4, 1, 'rgba(232, 232, 232, 1)' ],
							[ 5, 1, 'rgba(204, 204, 204, 1)' ],
							[ 6, 1, 'rgba(232, 232, 232, 1)' ],
							[ 7, 1, 'rgba(204, 204, 204, 1)' ],
							[ 8, 1, 'rgba(232, 232, 232, 1)' ],
							[ 9, 1, 'rgba(204, 204, 204, 1)' ],
							[ 10, 1, 'rgba(232, 232, 232, 1)' ],
							[ 11, 1, 'rgba(204, 204, 204, 1)' ],
							[ 12, 1, 'rgba(232, 232, 232, 1)' ],
							[ 13, 1, 'rgba(204, 204, 204, 1)' ],
							[ 14, 1, 'rgba(232, 232, 232, 1)' ],
							[ 15, 1, 'rgba(204, 204, 204, 1)' ],
							[ 16, 1, 'rgba(232, 232, 232, 1)' ],
							[ 17, 1, 'rgba(204, 204, 204, 1)' ],
							[ 18, 1, 'rgba(232, 232, 232, 1)' ],
							[ 19, 1, 'rgba(204, 204, 204, 1)' ],
							[ 20, 1, 'rgba(232, 232, 232, 1)' ],
							[ 21, 1, 'rgba(204, 204, 204, 1)' ],
							[ 22, 1, 'rgba(232, 232, 232, 1)' ],
							[ 23, 1, 'rgba(204, 204, 204, 1)' ]

					]).draw();

			RGraph.addCustomEventListener(gantt, 'onadjustend', function(obj) {
				var idx = RGraph.Registry.get('chart.adjusting.gantt')[0];
			});

			RGraph
					.addCustomEventListener(
							gantt,
							'onadjust',
							function(obj) {
								var events = obj.data;
								var conf = RGraph.Registry
										.get('chart.adjusting.gantt');

								if (conf) {
									var idx = conf['index'];
									document.getElementById("eventID").value = idx;
									document.getElementById("eventStart").value = events[idx][0];
									document.getElementById("eventDuration").value = events[idx][1];
									document.getElementById("cpuUsage").value = events[idx][2];
									
								console.log('Event ID: ' + idx
									+ ', Event start: '
									+ events[idx][0]
									+ ' Event duration: '
									+ events[idx][1]);
								}

							});

			var chart = new CanvasJS.Chart("chartContainer", {
				axisY : {
					includeZero : false,
					interval : 10,
					valueFormatString : "#'%'",
					title : "CPU Usage (%)"
				},
				axisX : {
					title : "Time in (mins/10)"
				},
				data : [ {
					type : "stepLine",
					toolTipContent : "{x}: {y}%",
					markerSize : 6,
					dataPoints :
	<%=cpuUsage%>
		}

				]
			});

			chart.render();
		}
	</script>

</head>

<body>
	<div class="navbar navbar-inverse navbar-static-top">
		<div class="container">
			<a href="#" class="navbar-brand">Energy Cost Reduction Tool</a>
			<button class="navbar-toggle" data-toggle="collapse"
				data-target=".navHeaderCollapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<div class="collapse navbar-collapse navHeaderCollapse">
				<ul class="nav navbar-nav navbar-right">
					<li class="active"><a href="#">Home</a></li>
					<li><a href="#">About</a></li>
					<li><a href="#">Contact</a></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="container text-center">
		<h1>Energy Cost Reduction Tool</h1>
		<form id="form1">
			<label>Datacentre: </label> <select name='drivers'><option>Please select a datacentre:</option>
				<option id="dc" value="1">1</option>
			</select> &nbsp; <label>Floor: </label> <select name='drivers'><option>Please
					select a floor:</option>
				<option id="fl" value="1">1</option>
			</select> &nbsp; <label>Rack: </label> <select name='drivers'><option>Please
					select a rack:</option>
				<option id="rc" value="1">1</option>
			</select> &nbsp; <label>Host: </label> <select name='drivers'><option>Please
					select a host:</option>
				<option id="ht" value="1">1</option>
			</select> &nbsp; <br> <br> <label for="datepicker">Date:
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input name="date" type="text" id="datepicker"
				data-date-format="dd/mm/yyyy" data-date-endDate="19/11/2014"
				data-provide="datepicker" />
			</label> <input type="button" value="Submit"> &nbsp; &nbsp; <input
				type="button" id="submit" value="Calculate" /> <br /> <label>Intial
				cost: <input type="text" value="<%=energyCost%>" name="intialCost"
				id="initialCost" readonly />
			</label> <label>Change in cost:
				<div id="newCost"></div>
			</label>


		</form>
	</div>

	<canvas id="cvs" width="1200" height="500">[No canvas support]</canvas>
	<br>
	<label> Event ID:
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="text" id="eventID" readonly />
	</label>
	<br>
	<label>Event start: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="text" id="eventStart" readonly />
	</label>
	<br>
	<label>Event duration: &nbsp; 
		<input type="text" id="eventDuration" readonly /></label>
	</label>
	<br>
	<label>CPU Usage:
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
		<input type="text" id="cpuUsage" readonly />
	</label>
	<br>
	<br>


	<h2><label>CPU Usage Chart</label></h2>
	
	<div id="chartContainer" style="height: 700px; width: 100%;"></div>

	<h3>Process Summary for 24 hours</h3>
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>Process Name</th>
				<th>No. of runs</th>
				<th>Total Duration</th>
			</tr>
		</thead>
		<tbody>
			<%=data%>
		</tbody>
	</table>

	<div class="navbar-default navbar-bottom-center">
		<div class="container">
			<p class="navbar-text pull-left">Energy Cost Reduction Tool
				&copy;</p>
		</div>
	</div>
</body>

</html>