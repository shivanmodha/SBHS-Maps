var ME;
var obj;
var obj2;
function Main()
{
    var RenderingCanvas = document.getElementById("studios.vanish.mc");
    RenderingCanvas.width = document.body.clientWidth;
    RenderingCanvas.height = window.innerHeight - 5;

    ME = new Engine(RenderingCanvas);
    var Vertices = 
    [
        new GraphicsVertex(+0.0, +1.0, +0.0, 1.0, 1.0, 1.0, 1.0),
        new GraphicsVertex(-1.0, -1.0, +0.0, 1.0, 0.0, 1.0, 1.0),
        new GraphicsVertex(+1.0, -1.0, +0.0, 1.0, 1.0, 1.0, 1.0),
    ];
    var Indices = 
    [
        new Index(0, 1, 2)
    ];
    obj = new Object3D(ME, Vertices, Indices);
    obj.Location = new Vertex(0, 0, -10);
    obj2 = new Object3D(ME, Vertices, Indices);
    obj2.Location = new Vertex(0, 0, -10);
    obj2.RevolutionRadius = new Vertex(0, 0, 1);
    Loop();
}
function Loop()
{
    requestAnimFrame(Loop);
    Render();
    Update();
}
function Update()
{
    obj2.Revolution.X = obj2.Revolution.X + 1;
}
function Render()
{
    ME.Clear(0, 0, 0, 1);
    obj.Render(ME);
    obj2.Render(ME);
}