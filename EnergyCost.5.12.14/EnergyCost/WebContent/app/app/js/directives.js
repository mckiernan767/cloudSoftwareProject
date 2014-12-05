'use strict';

/* Directives */


var directives = angular.module('myApp.directives', []);

directives.directive('appVersion', ['version', function(version) {
  return function(scope, elm, attrs) {
    elm.text(version);
  }; 
}]);

/*
*
* the upload file code inspired from http://buildinternet.com/2013/08/drag-and-drop-file-upload-with-angularjs/
* note that it has been changed for our needs
*
*/
directives.directive('powerModelDropzone', ['$route', 'PowerModelService', 'Utils', function( routeService, pmService, utils) {
    return {
      restrict: 'A',
      scope: {
        file: '=',
        fileName: '=',
        powerModelGroup: '@',
        reloadCallback: '&reloadCallback'
      },
      link: function(scope, element, attrs) {
        var checkSize, isTypeValid, processDragOverOrEnter, validMimeTypes, checkPowerModelGroupIsChosen;
        var pmg;
        attrs.$observe('powerModelGroup', function(value) {
          pmg = value;
        });
        processDragOverOrEnter = function(event) {
          if (event != null) {
            event.preventDefault();
          }
          event.dataTransfer.effectAllowed = 'copy';
          return false;
        };
        checkPowerModelGroupIsChosen = function() {
          if (pmg) {
            return true;
          } else {
             scope.$apply(function () {
                utils.toasterWarning('Select powermodel group before adding powermodels');
              });
            return false;
          }
        };
        validMimeTypes = attrs.powerModelDropzone;
        checkSize = function(size) {
          var _ref;
          if (((_ref = attrs.maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < attrs.maxFileSize) {
            return true;
          } else {
             scope.$apply(function () {
                utils.toasterWarning("File must be smaller than " + attrs.maxFileSize + " MB");
              });
            return false;
          }
        };
        isTypeValid = function(type) {
          if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
            return true;
          } else {
             scope.$apply(function () {
                utils.toasterWarning("Invalid file type.  File must be one of following types " + validMimeTypes);
              });
            return false;
          }
        };
        element.bind('dragover', processDragOverOrEnter);
        element.bind('dragenter', processDragOverOrEnter);
        return element.bind('drop', function(event) {
          var file, name, reader, size, type;
          if (event != null) {
            event.preventDefault();
          }
          reader = new FileReader();
          reader.onload = function(evt) {
            if (checkSize(size) && isTypeValid(type) && checkPowerModelGroupIsChosen()) {
              return scope.$apply(function() {
                scope.file = evt.target.result;
                var x2js = new X2JS();
                var pm = x2js.xml_str2json( evt.target.result );                              
                pmService.create(angular.fromJson(pmg).id, pm.powerModel, 
                  function(){ 
                    scope.reloadCallback();
                  },  utils.onError);
                if (angular.isString(scope.fileName)) {
                  return scope.fileName = name;
                }
              });
            }
          };
          file = event.dataTransfer.files[0];
          name = file.name;
          type = file.type;
          size = file.size;
          reader.readAsText(file);
          return false;
        });
      }
    };
  }]);


directives.directive('datacenterDropzone', ['$route', 'DatacenterService', 'Utils', function( routeService, pmService, utils) {
    return {
      restrict: 'A',
      scope: {
        reloadCallback: '&reloadCallback'
      },
      link: function(scope, element, attrs) {
        var checkSize, isTypeValid, processDragOverOrEnter, validMimeTypes;
        processDragOverOrEnter = function(event) {
          if (event != null) {
            event.preventDefault();
          }
          event.dataTransfer.effectAllowed = 'copy';
          return false;
        };
        validMimeTypes = attrs.powerModelDropzone;
        checkSize = function(size) {
          var _ref;
          if (((_ref = attrs.maxFileSize) === (void 0) || _ref === '') || (size / 1024) / 1024 < attrs.maxFileSize) {
            return true;
          } else {
             scope.$apply(function () {
                utils.toasterWarning("File must be smaller than " + attrs.maxFileSize + " MB");
              });
            return false;
          }
        };
        isTypeValid = function(type) {
          if ((validMimeTypes === (void 0) || validMimeTypes === '') || validMimeTypes.indexOf(type) > -1) {
            return true;
          } else {
             scope.$apply(function () {
                utils.toasterWarning("Invalid file type.  File must be one of following types " + validMimeTypes);
              });
            return false;
          }
        };
        element.bind('dragover', processDragOverOrEnter);
        element.bind('dragenter', processDragOverOrEnter);
        return element.bind('drop', function(event) {
          var file, name, reader, size, type;
          if (event != null) {
            event.preventDefault();
          }
          reader = new FileReader();
          reader.onload = function(evt) {
            if (checkSize(size) && isTypeValid(type)) {
              return scope.$apply(function() {
                pmService.upload(file, 
                  function(data){ 
                    scope.reloadCallback();
                  },  utils.onError);
              });
            }
          };
          file = event.dataTransfer.files[0];
          name = file.name;
          type = file.type;
          size = file.size;
          reader.readAsText(file);
          return false;
        });
      }
    };
  }]);


/*
*
* D3 directives using the d3 service to be able to have d3 graphs with angularjs 
*
*/

directives.directive('d3BarChart', ['$window', '$timeout', 'd3Service', 
  function($window, $timeout, d3Service) {
    return {
      restrict: 'EA',
      scope: {
        data: '=',
        label: '@',
        onClick: '&'
      },
      link: function(scope, ele, attrs) {
        var d3 = d3Service;
          var renderTimeout;
          var margin = parseInt(attrs.margin) || 20,
              barHeight = parseInt(attrs.barHeight) || 20,
              barPadding = parseInt(attrs.barPadding) || 5;
 
          var svg = d3.select(ele[0])
            .append('svg')
            .style('width', '100%');
 
          $window.onresize = function() {
            scope.$apply();
          };
 
          scope.$watch(function() {
            return angular.element($window)[0].innerWidth;
          }, function() {
            scope.render(scope.data);
          });
 
          
          scope.$watch('data', function(newData) {
            scope.render(newData);
          }, true);
          
          scope.render = function(data) {
            svg.selectAll('*').remove();
 
            if (!data) return;
            if (renderTimeout) clearTimeout(renderTimeout);
 
 
            renderTimeout = $timeout(function() {
              var width = d3.select(ele[0])[0][0].offsetWidth - margin,
                  height = scope.data.length * (barHeight + barPadding),
                  color = d3.scale.category20(),
                  xScale = d3.scale.linear()
                    .domain([0, d3.max(data, function(d) {
                      return d.score;
                    })])
                    .range([0, width]);
 
              svg.attr('height', height);
 
              svg.selectAll('rect')
                .data(data)
                .enter()
                  .append('rect')
                  .on('click', function(d,i) {
                    return scope.onClick({item: d});
                  })
                  .attr('height', barHeight)
                  .attr('width', 140)
                  .attr('x', Math.round(margin/2))
                  .attr('y', function(d,i) {
                    return i * (barHeight + barPadding);
                  })
                  .attr('fill', function(d) {
                    return color(d.score);
                  })                  
                  .transition()
                    .duration(1000)
                    .attr('width', function(d) {
                      return xScale(d.score);
                    });
              svg.selectAll('text')
                .data(data)
                .enter()
                  .append('text')
                  .attr('fill', '#fff')
                  .attr('y', function(d,i) {
                    return i * (barHeight + barPadding) + 15;
                  })
                  .attr('x', 15)
                  .text(function(d) {
                    return d.name + " (scored: " + d.score + ")";
                  });
            }, 200);
          };
      }}
}]);

//expects an array of name value pair
directives.directive('d3PieChart', ['$window', '$timeout', 'd3Service', 
  function($window, $timeout, d3Service) {
    return {
      restrict: 'EA',
      scope: {
        data: '=',
        label: '@',
        onClick: '&'
      },
      link: function(scope, ele, attrs) {
          var d3 = d3Service;
          var renderTimeout;

          var parseBool = function(b) {
          return !(/^(false|0)$/i).test(b) && !!b;
          }

          var margin = parseInt(attrs.margin) || 20,
              width = parseInt(attrs.width) || 600,
              height = parseInt(attrs.height) || 150,
              doughnut = parseBool(attrs.doughnut) || false;

          var innerRadius = 0;
          var legendWidth = width * 0.5;        
          var outerRadius = Math.max(width, height) * 0.5/2;
          if(doughnut)
          {
            innerRadius = outerRadius * 0.1;
          }

          var svg = d3.select(ele[0])
            .append('svg')
             .attr('height', outerRadius * 2) 
             .attr('width', outerRadius * 2);  

            //.style('width', '100%');

          var legend = d3.select(ele[0]).append("svg")
              .attr("class", "legend")
              .attr("width", legendWidth + margin)
              .attr("height", outerRadius * 2);
 
          $window.onresize = function() {
            scope.$apply();
          };
 
          scope.$watch(function() {
            return angular.element($window)[0].innerWidth;
          }, function() {
            scope.render(scope.data);
          });
 
          scope.$watch('data', function(newData) {
            scope.render(newData);
          }, true);
 
          scope.render = function(data) {
            svg.selectAll('*').remove();
            legend.selectAll('*').remove();
 
            if (!data) return;
            if (renderTimeout) clearTimeout(renderTimeout);
            renderTimeout = $timeout(function() {
              var width = outerRadius * 2,
                  height = outerRadius * 2,
                  color = d3.scale.category20(),
                  oRadius = outerRadius,
                  iRadius = innerRadius;

              var arc = d3.svg.arc() 
                .innerRadius(iRadius)
                .outerRadius(oRadius);

              var pie = d3.layout.pie()
                .value(function(d) { return d.value; });

             // we'll use svg group elements for pie wedges
              var wedges = svg.selectAll('g')
                .data(pie(data)) // pass in our 'pie-ized' data
                .enter()
                .append('g')
                .attr({
                    'class' : 'wedge',
                    // we need to translate each wedge into the center
                    'transform' : 'translate(' + oRadius  + ', ' + oRadius + ')'
                });


                // draw arcs for wedges
                wedges.append('path')
                    .attr({
                        'fill' : function(d,i) {
                            return color(i);
                        },
                        // pass in the arc generator for the 'd' attribute of the path,
                        // which is the path description
                        'd' : arc
                    });           

              var legendItems =  legend.selectAll("g")
                  .data(color.domain().slice().reverse())
                  .enter().append("g")
                  .attr("transform", function(d, i) { return "translate("+margin+"," + i * 20 + ")"; }); 


                legendItems.append("rect")
                    .attr("width", 18)
                    .attr("height", 18)
                    .style("fill", function(d, i){
                      return color(i);
                    });

                legendItems.append("text")
                    .attr("x", 24)
                    .attr("y", 9)
                    .attr("dy", ".35em")
                    //.text('qwertyuiopåasdfghjklöä');
                    .text(function(d, i) { return data[i].name; });

/*
                //writes the text inside each wedge
                wedges.append('text')
                   .attr({
                       'transform' : function(d) {
                           return 'translate(' + arc.centroid(d) + ')';
                       },
                       'text-anchor' : 'middle'
                   })
                   .text(function(d, i) { return data[i].value; }); 
*/                  
            }, 200);
          };
      }}
}]);

//Note: at the moment this linechart is not generic. it espects a timestamp 
//      and power at each data point. Also the labels are hard coded. Can 
//      be generalised on a later stage
directives.directive('d3LineChart', ['$window', '$timeout', 'd3Service', 
  function($window, $timeout, d3Service) {
    return {
      restrict: 'EA',
      scope: {
        data: '=',
        label: '@',
        onClick: '&'
      },
      link: function(scope, ele, attrs) {
          var d3 = d3Service;
          var renderTimeout;
          
          
          var width = parseInt(attrs.width) || 600,
              height = parseInt(attrs.height) || 300;

          
          var margin = {top: 20, right: 20, bottom: 30, left: 50};
          width = width - margin.left - margin.right,
          height = height - margin.top - margin.bottom;

          var svg = d3.select(ele[0])
            .append('svg')
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")") 
            .style('width', '100%');

          $window.onresize = function() {
            scope.$apply();
          };
 
          scope.$watch(function() {
            return angular.element($window)[0].innerWidth;
          }, function() {
            scope.render(scope.data);
          });
          
          scope.$watch('data', function(newData) {
            scope.render(newData);
          }, true);



          scope.render = function(data) {
            svg.selectAll('*').remove();
 
            if (!data) return;

            if (renderTimeout) clearTimeout(renderTimeout);

            renderTimeout = $timeout(function() {
              var x = d3.time.scale().range([0, width]);
              var y = d3.scale.linear().range([height, 0]);
              var format = d3.time.format("%Y-%m-%d %H:%M"); 
              var xAxis = d3.svg.axis().scale(x).orient("bottom").ticks(5).tickFormat(format);
              var yAxis = d3.svg.axis().scale(y).orient("left");

              var line = d3.svg.line().x(function(d) { return x(d.timeStamp); })
                                      .y(function(d) { return y(d.power); });
                                       
                                
              x.domain(d3.extent(data, function(d) { return d.timeStamp; }));
              y.domain(d3.extent(data, function(d) { return d.power; }));   


              svg.append("g")
                .attr("class", "x axis")
                .call(xAxis)
                .attr("transform", "translate(0," + height + ")")
                .append("text")
                .attr("x", width)
                .attr("dy", "-0.3em")
                .attr("dx", "-0.3em")
                .style("text-anchor", "end")
                .text("Time");
                

              svg.append("g")
                .attr("class", "y axis")
                .call(yAxis)
                .append("text")
                .attr("transform", "rotate(-90)")
                .attr("y", 6)
                .attr("dy", ".71em")
                .style("text-anchor", "end")
                .text("Power (Watts)");

              svg.selectAll(".xaxis text")
              .style("text-anchor", "end")
              .attr("dx", "-.8em")
              .attr("dy", ".15em")
              .attr("transform", function(d) {
                return "rotate(-65)" 
                });


              svg.selectAll("circle") 
                .data(data) 
                .enter() 
                .append("svg:circle") 
                .attr("cx", function(d) { return x(d.timeStamp); }) 
                .attr("cy", function(d) { return y(d.power); }) 
                .attr("r", 3) 
                .attr("opacity", 3) 
                .on('click', function(d) {
                  return scope.onClick({item: d});
                })    
                .on('mouseover', function() {
                    d3.select(this).attr("r", 6); 
                })            
                .on('mouseout', function() {
                    d3.select(this).attr("r", 3); 
                })            
                .append("svg:title").text(function(d) {return "Time: " + format(d.timeStamp) + "\nPower: " + d.power;});                      

              svg.append("path")
                  .datum(data)
                  .attr("class", "line")
                  .attr("d", line);

            }, 200);
          };
      }}
}]);
