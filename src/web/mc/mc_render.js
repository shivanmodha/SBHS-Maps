var ME;
var obj;
var obj2;
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
    var Vertices = 
    [
        new GraphicsVertex(-1.0, -1.0, +1.0, 1.0, 0.0, 0.0, 1.0),
        new GraphicsVertex(-1.0, +1.0, +1.0, 0.0, 1.0, 0.0, 1.0),
        new GraphicsVertex(+1.0, +1.0, +1.0, 0.0, 0.0, 1.0, 1.0),
        new GraphicsVertex(+1.0, -1.0, +1.0, 1.0, 1.0, 0.0, 1.0),
        
        new GraphicsVertex(-1.0, -1.0, -1.0, 0.0, 1.0, 1.0, 1.0),
        new GraphicsVertex(-1.0, +1.0, -1.0, 1.0, 1.0, 0.0, 1.0),
        new GraphicsVertex(+1.0, +1.0, -1.0, 1.0, 1.0, 1.0, 1.0),
        new GraphicsVertex(+1.0, -1.0, -1.0, 0.0, 0.0, 0.0, 1.0),
    ];
    var Indices = 
    [
        new Index(0, 1, 2),
        new Index(0, 2, 3),
        
        new Index(4, 5, 6),
        new Index(4, 6, 7),

        new Index(0, 1, 4),
        new Index(1, 4, 5),

        new Index(2, 3, 6),
        new Index(3, 6, 7),

        new Index(0, 3, 4),
        new Index(3, 4, 7),

        new Index(1, 2, 5),
        new Index(2, 5, 6)
    ];
    obj = new Object3D(ME, Vertices, Indices);
    obj.Location = new Vertex(0, 0, -10);
    obj.RenderMode = "Solid";
    obj2 = new Object3D(ME, Vertices, Indices);
    obj2.Location = new Vertex(0, 0, -10);
    obj2.RevolutionRadius = new Vertex(0, 0, 3);
    obj2.Scale = new Vertex(0.5, 0.5, 0.5);
}
function MainLoop()
{
    requestAnimFrame(MainLoop);
    Render();
    Update();
}
function Update()
{
    ME.Camera.Rotation.X += 0.1;
    obj.Rotation.X += 1;
    obj.Rotation.Y += 0.1;
    obj2.Revolution.Y += 1;
}
function Render()
{
    ME.Clear(0, 0, 0, 1);
    obj.Render(ME);
    obj2.Render(ME);
}