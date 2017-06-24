var http = require("http");
var fs = require("fs");
var server = http.createServer(function (request, response)
{
    fs.readFile('./' + request.url, function(err, data)
    {
        if (!err)
        {
            var offset = request.url.lastIndexOf('.');
            var doctype = offset == -1
                            ? 'text/plain'
                            : {
                                '.html' : 'text/html',
                                '.ico' : 'image/x-icon',
                                '.jpg' : 'image/jpeg',
                                '.png' : 'image/png',
                                '.gif' : 'image/gif',
                                '.css' : 'text/css',
                                '.js' : 'text/javascript',
                                '.MSV' : 'text/plain',
                                '.MSP' : 'text/plain',
                                }[ request.url.substr(offset) ];
            response.setHeader('Content-type' , doctype);
            response.end(data);
        }
        else
        {
            response.writeHead(404, "It seems as if this page doesn't exist");
            response.end();
        }
    });
}).listen(8080, "localhost");