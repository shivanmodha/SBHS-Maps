var ME;
var MapObjects = new Array(1);
function Main()
{
    var RenderingCanvas = document.getElementById("studios.vanish.mc");
    RenderingCanvas.width = document.body.clientWidth;
    RenderingCanvas.height = window.innerHeight - 5;
    ME = new Engine(RenderingCanvas);
    Initialize();
    MainLoop();
}
function Initialize()
{
    var source = "";
    var raw = new XMLHttpRequest();
    raw.open("GET", "map.ngm", false);
    raw.onreadystatechange = function()
    {
        if (this.readyState == 4)
        {
            if (this.status == 200)
            {
                source = raw.responseText;
            }
        }
    }
    raw.send(null);
    var JSONObject = JSON.parse(source);
    MapObjects = new Array(JSONObject.nodes.length);
    for (var i = 0; i < JSONObject.nodes.length; i++)
    {
        var s = 0;
        var a = 1;
        if (JSONObject.nodes[i].object.type == "WastedSpace")
        {
            s = 0.8745;
        }
        else if (JSONObject.nodes[i].object.type == "Building")
        {
            s = 0.968;
        }
        else if (JSONObject.nodes[i].object.type == "Path")
        {
            s = 1;
        }
        var vertices = 
        [
            new GraphicsVertex(JSONObject.nodes[i].object.BL.x, JSONObject.nodes[i].object.BL.y, JSONObject.nodes[i].object.BL.z, s, s, s, a),
            new GraphicsVertex(JSONObject.nodes[i].object.TL.x, JSONObject.nodes[i].object.TL.y, JSONObject.nodes[i].object.TL.z, s, s, s, a),
            new GraphicsVertex(JSONObject.nodes[i].object.TR.x, JSONObject.nodes[i].object.TR.y, JSONObject.nodes[i].object.TR.z, s, s, s, a),
            new GraphicsVertex(JSONObject.nodes[i].object.BR.x, JSONObject.nodes[i].object.BR.y, JSONObject.nodes[i].object.BR.z, s, s, s, a),
        ];
        /*vertices = 
        [
            new GraphicsVertex(-1.0, -1.0, +1.0, s, s, s, a),
            new GraphicsVertex(-1.0, +1.0, +1.0, s, s, s, a),
            new GraphicsVertex(+1.0, +1.0, +1.0, s, s, s, a),
            new GraphicsVertex(+1.0, -1.0, +1.0, s, s, s, a),
        ];*/
        var indices = 
        [
            new Index(0, 1, 2),
            new Index(0, 2, 3)
        ];
        MapObjects[i] = new Object3D(ME, vertices, indices);
    }
}
function MainLoop()
{
    requestAnimFrame(MainLoop);
    Render();
    Update();
}
function Update()
{
    ME.Camera.Location.X += 0.01;
    ME.Camera.Location.Y = 50;
    ME.Camera.Location.Z = 90;
}
function Render()
{
    ME.Clear(1, 1, 1, 1);
    for (var i = 0; i < MapObjects.length; i++)
    {
        MapObjects[i].Render(ME);
    }
}