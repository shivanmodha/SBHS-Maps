var ME;
var Map = new Array(6);
var Map_Labels = new Array(6);
var RenderedFloor = 0;
var MouseButton = 0;
var MousePosition = new Point(0, 0);
var DeltaMouse = new Point(0, 0);
var PreviousMousePosition = new Point(0, 0);
function Main()
{
    window.addEventListener("mousedown", Event_Down);
    window.addEventListener("mouseup", Event_Up);
    window.addEventListener("mousemove", Event_Move);
    window.addEventListener("mouseover", Event_Move);
    window.addEventListener("mousewheel", Event_Wheel);
    window.addEventListener("DOMMouseScroll", Event_Wheel);

    var RC3 = document.getElementById("studios.vanish.mc.3D");
    var RC2 = document.getElementById("studios.vanish.mc.2D");
    ME = new Engine(RC2, RC3);

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
    for (var i = 0; i < 6; i++)
    {
        Map[i] = [];
        Map_Labels[i] = [];
    }
    for (var i = 0; i < JSONObject.nodes.length; i++)
    {
        var s = 0;
        var ds = 0;
        var a = 1;
        var type = "U";
        var name = JSONObject.nodes[i].friendly;
        if (JSONObject.nodes[i].object.type == "WastedSpace")
        {
            ds = 0.8745;
            s = 0.8745;
            type = "W";
        }
        else if (JSONObject.nodes[i].object.type == "Building")
        {
            ds = 0.784;
            s = 0.968;
            type = "B";
        }
        else if (JSONObject.nodes[i].object.type == "Path")
        {
            ds = 1;
            s = 1;
            type = "P";
        }
        var offset = 0.01;
        var height = 0.5;
        var vertices = 
        [
            new GraphicsVertex(JSONObject.nodes[i].object.BL.x, JSONObject.nodes[i].object.BL.y, JSONObject.nodes[i].object.BL.z, ds, ds, ds, a),
            new GraphicsVertex(JSONObject.nodes[i].object.TL.x, JSONObject.nodes[i].object.TL.y, JSONObject.nodes[i].object.TL.z, ds, ds, ds, a),
            new GraphicsVertex(JSONObject.nodes[i].object.TR.x, JSONObject.nodes[i].object.TR.y, JSONObject.nodes[i].object.TR.z, ds, ds, ds, a),
            new GraphicsVertex(JSONObject.nodes[i].object.BR.x, JSONObject.nodes[i].object.BR.y, JSONObject.nodes[i].object.BR.z, ds, ds, ds, a),
        ];
        var indices = 
        [
            new Index(0, 1, 2),
            new Index(0, 2, 3)
        ];
        if (type == "B")
        {            
            vertices = 
            [
                /**
                 * BOTTOM
                 */
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x, JSONObject.nodes[i].object.BL.y, JSONObject.nodes[i].object.BL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x, JSONObject.nodes[i].object.TL.y, JSONObject.nodes[i].object.TL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x, JSONObject.nodes[i].object.TR.y, JSONObject.nodes[i].object.TR.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x, JSONObject.nodes[i].object.BR.y, JSONObject.nodes[i].object.BR.z, ds, ds, ds, a),
                /**
                 * TOP
                 */
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x + offset, JSONObject.nodes[i].object.BL.y + offset, JSONObject.nodes[i].object.BL.z + height, s, s, s, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x + offset, JSONObject.nodes[i].object.TL.y - offset, JSONObject.nodes[i].object.TL.z + height, s, s, s, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x - offset, JSONObject.nodes[i].object.TR.y - offset, JSONObject.nodes[i].object.TR.z + height, s, s, s, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x - offset, JSONObject.nodes[i].object.BR.y + offset, JSONObject.nodes[i].object.BR.z + height, s, s, s, a),
                /**
                 * LEFT
                 */
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x, JSONObject.nodes[i].object.BL.y, JSONObject.nodes[i].object.BL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x, JSONObject.nodes[i].object.TL.y, JSONObject.nodes[i].object.TL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x + offset, JSONObject.nodes[i].object.TL.y - offset, JSONObject.nodes[i].object.TL.z + height, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x + offset, JSONObject.nodes[i].object.BL.y + offset, JSONObject.nodes[i].object.BL.z + height, ds, ds, ds, a),
                /**
                 * RIGHT
                 */
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x, JSONObject.nodes[i].object.BR.y, JSONObject.nodes[i].object.BR.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x, JSONObject.nodes[i].object.TR.y, JSONObject.nodes[i].object.TR.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x - offset, JSONObject.nodes[i].object.TR.y - offset, JSONObject.nodes[i].object.TR.z + height, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x - offset, JSONObject.nodes[i].object.BR.y + offset, JSONObject.nodes[i].object.BR.z + height, ds, ds, ds, a),
                /**
                 * FRONT
                 */
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x, JSONObject.nodes[i].object.BL.y, JSONObject.nodes[i].object.BL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x, JSONObject.nodes[i].object.BR.y, JSONObject.nodes[i].object.BR.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BR.x - offset, JSONObject.nodes[i].object.BR.y + offset, JSONObject.nodes[i].object.BR.z + height, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.BL.x + offset, JSONObject.nodes[i].object.BL.y + offset, JSONObject.nodes[i].object.BL.z + height, ds, ds, ds, a),
                /**
                 * BACK
                 */                
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x, JSONObject.nodes[i].object.TL.y, JSONObject.nodes[i].object.TL.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x, JSONObject.nodes[i].object.TR.y, JSONObject.nodes[i].object.TR.z, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TR.x - offset, JSONObject.nodes[i].object.TR.y - offset, JSONObject.nodes[i].object.TR.z + height, ds, ds, ds, a),
                new GraphicsVertex(JSONObject.nodes[i].object.TL.x + offset, JSONObject.nodes[i].object.TL.y - offset, JSONObject.nodes[i].object.TL.z + height, ds, ds, ds, a),
            ];
            indices = 
            [
                //new Index(0, 1, 2),
                //new Index(0, 2, 3),

                new Index(4, 5, 6),
                new Index(4, 6, 7),

                new Index(8, 9, 10),
                new Index(8, 10, 11),
                
                new Index(12, 13, 14),
                new Index(12, 14, 15),
                
                new Index(16, 17, 18),
                new Index(16, 18, 19),
                
                new Index(20, 21, 22),
                new Index(20, 22, 23),
            ];
        }
        if (vertices[0].Z === vertices[1].Z && vertices[1].Z === vertices[2].Z && vertices[2].Z === vertices[3].Z)
        {
            if (vertices[0].Z != -1)
            {
                var floor = vertices[0].Z;
                Map[floor].push(new Object3D(ME, vertices, indices, name));
                Map_Labels[floor].push(new Label(name, Map[floor][Map[floor].length - 1].GetCenterVertexPoint_HighZ(), 12));
            }
        }
    }
    ME.Camera.Location.X = 50;
    ME.Camera.Location.Y = 50;
    ME.Camera.Location.Z = 100;
    //ME.Camera.Rotation.X = -45;
}
function Event_Down(event)
{
    MouseButton = 1;
    PreviousMousePosition = new Point(event.clientX, event.clientY);
}
function Event_Up(event)
{
    MouseButton = 0;
}
function Event_Move(event)
{
    MousePosition = new Point(event.clientX, event.clientY);
}
function Event_Wheel(event)
{
    var e = window.event || event;
	var delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));
    ME.Camera.Location.Z -= (delta * 2);
}
function MainLoop()
{
    requestAnimFrame(MainLoop);
    Render();
    Update();
}
function Update()
{
    if (MouseButton == 1)
    {
        DeltaMouse = new Point(PreviousMousePosition.X - MousePosition.X, PreviousMousePosition.Y - MousePosition.Y);
        PreviousMousePosition = new Point(MousePosition.X, MousePosition.Y);
        ME.Camera.Location.X += (DeltaMouse.X * 18) / (500 - ME.Camera.Location.Z);
        ME.Camera.Location.Y -= (DeltaMouse.Y * 18) / (500 - ME.Camera.Location.Z);
    }
    for (var i = 0; i < Map[RenderedFloor].length; i++)
    {
        if (Map_Labels[RenderedFloor][i].Collision(MousePosition))
        {
            Map_Labels[RenderedFloor][i].b = 255;
        }
        else
        {
            Map_Labels[RenderedFloor][i].b = 0;
        }
    }
}
function Render()
{
    ME.Clear(0.875, 0.875, 0.875, 1);
    for (var i = 0; i < Map[RenderedFloor].length; i++)
    {
        Map[RenderedFloor][i].Render(ME);
        var n = Map[RenderedFloor][i].name;
        if (n.includes("(h)") == false && n.includes("(s)") == false)
        {
            Map_Labels[RenderedFloor][i].Render(ME);
        }
    }
}