var http = require("http");
var fs = require("fs");
var server = http.createServer(function (request, response)
{
    var url = request.url;
    if (url.includes("?"))
    {
        url = url.substring(0, url.lastIndexOf("?"));
    }
    if (url.endsWith("/"))
    {
        url = url.substring(0, url.length - 1);
    }
    try
    {
        fs.readFile('./' + url, function(err, data)
        {
            if (!err)
            {
                var offset = url.lastIndexOf('.');
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
                                    '.ngm' : 'text/javascript'
                                    }[ url.substr(offset) ];
                response.setHeader('Content-type' , doctype);
                response.end(data);
            }
            else
            {
                response.writeHead(404, "It seems as if this page doesn't exist");
                response.end();
            }
        });
    }
    catch (e)
    {
        console.log("error: " + e);
    }
}).listen(8080, "localhost");