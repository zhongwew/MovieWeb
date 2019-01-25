var fs = require("fs");

var lines = [],
    linesLen = 0,
    i = 0;

var queryTime = 0,
    servletTime = 0,
    jdbcTime = 0;

// Read file and parse it
var data = fs.readFileSync('/home/tomcat/webapps/MovieServer/timeRecord.txt');
lines = data.toString().split('\n');

lines = lines.filter((line) => {
    return line !== '';
});

// Store length of data
linesLen = lines.length;

// Iterate each data
for (i = 0 ; i < linesLen; i ++) {
    queryTime = queryTime + Number.parseInt(lines[i].split(' ')[0]);
    servletTime = servletTime + Number.parseInt(lines[i].split(' ')[1]);
    jdbcTime = jdbcTime + Number.parseInt(lines[i].split(' ')[2]);
}

// Calculate 3 types time
queryTime = queryTime / linesLen;
servletTime = servletTime / linesLen;
jdbcTime = jdbcTime / linesLen;

console.log(queryTime, servletTime, jdbcTime);
