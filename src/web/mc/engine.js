var Point = class Point
{
    constructor(x, y)
    {
        this.X = x;
        this.Y = y;
    }
}
var Vertex = class Vertex
{
    constructor(x, y, z)
    {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }
}
var Camera = class Camera
{
    constructor(location, rotation)
    {
        this.Location = location;
        this.Rotation = rotation;
    }
}
var Engine = class Engine
{
    /**
     * Creates and initializes a new rendering window for OpenGL
     * @param {Element} canvas 
     */
    constructor(canvas2D, canvas)
    {
        this.RenderingCanvas = canvas;
        this.Device = this.RenderingCanvas.getContext('webgl');
        this.Device2D = canvas2D.getContext('2d');
        var style = window.getComputedStyle(canvas);
        canvas2D.width = parseInt(style.width);
        canvas2D.height = parseInt(style.height);
        this.RenderingCanvas.width = parseInt(style.width);
        this.RenderingCanvas.height = parseInt(style.height);
        this.Device.viewportWidth =  canvas.width;
        this.Device.viewportHeight = canvas.height;
        this.Shader_Vertex = this.LoadShaderFile("shader.MSV");
        this.Shader_Pixel = this.LoadShaderFile("shader.MSP");
        this.Shader_Program = this.Device.createProgram();
        this.Device.attachShader(this.Shader_Program, this.Shader_Vertex);
        this.Device.attachShader(this.Shader_Program, this.Shader_Pixel);
        this.Device.linkProgram(this.Shader_Program);
        if (!this.Device.getProgramParameter(this.Shader_Program, this.Device.LINK_STATUS))
        {
            alert("Could not initialize shaders");
        }
        this.Device.useProgram(this.Shader_Program);
        this.Shader_Program.vertexPositionAttribute = this.Device.getAttribLocation(this.Shader_Program, "vertexPosition");
        this.Device.enableVertexAttribArray(this.Shader_Program.vertexPositionAttribute);
        this.Shader_Program.vertexColorAttribute = this.Device.getAttribLocation(this.Shader_Program, "vertexColor");
        this.Device.enableVertexAttribArray(this.Shader_Program.vertexColorAttribute);
        this.Shader_Program.pMatrixUniform = this.Device.getUniformLocation(this.Shader_Program, "uPMatrix");
        this.Shader_Program.mvMatrixUniform = this.Device.getUniformLocation(this.Shader_Program, "uMVMatrix");
        this.Device.clearColor(0.0, 0.0, 0.0, 1.0);
        this.Device.enable(this.Device.DEPTH_TEST);
        //this.ViewProjectionMatrix = mat4.create();
        this.ViewProjectionMatrix = m4.perspective(degToRad(60), this.Device.viewportWidth / this.Device.viewportHeight, 0.1, 1000);
        this.Camera = new Camera(new Vertex(0, 0, 0), new Vertex(0, 0, 0))
    }
    Clear(r, g, b, a)
    {
        this.Device.clearColor(r, g, b, a);
        this.Device.viewport(0, 0, this.Device.viewportWidth, this.Device.viewportHeight);
        this.Device.clear(this.Device.COLOR_BUFFER_BIT | this.Device.DEPTH_BUFFER_BIT);
        this.Device2D.clearRect(0, 0, this.Device.viewportWidth, this.Device.viewportHeight);
        this.ViewProjectionMatrix = m4.perspective(degToRad(60), this.Device.viewportWidth / this.Device.viewportHeight, 0.1, 1000);
}
    SetShaderWorlds(WorldMatrix)
    {
        this.Device.uniformMatrix4fv(this.Shader_Program.pMatrixUniform, false, this.ViewProjectionMatrix);
        this.Device.uniformMatrix4fv(this.Shader_Program.mvMatrixUniform, false, WorldMatrix);
    }
    GetDevice()
    {
        return this.Device;
    }
    /**
     * Reads a shader file and loads it into a shader object for later use
     * @param {String} file 
     */
    LoadShaderFile(file)
    {
        var location = window.location.href.substr(0, window.location.href.lastIndexOf("/"));
        var source = "";
        var raw = new XMLHttpRequest();
        raw.open("GET", file, false);
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
        raw.send();
        var shader;
        if (file.endsWith(".MSV"))
        {
            shader = this.Device.createShader(this.Device.VERTEX_SHADER);
        }
        else if (file.endsWith(".MSP"))
        {
            shader = this.Device.createShader(this.Device.FRAGMENT_SHADER);
        }
        else
        {
            return null;
        }
        this.Device.shaderSource(shader, source);
        this.Device.compileShader(shader);
        if (!this.Device.getShaderParameter(shader, this.Device.COMPILE_STATUS))
        {
            alert(this.Device.getShaderInfoLog(shader));
            return null;
        }
        return shader;
    }
}
var GraphicsVertex = class GraphicsVertex
{
    constructor(x, y, z, r, g, b, a)
    {
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.R = r;
        this.G = g;
        this.B = b;
        this.A = a;
    }
}
var Index = class Index
{
    constructor(i1, i2, i3)
    {
        this.indices = [i1, i2, i3];
    }
}
var Object3D = class Object3D
{
    constructor(engine, vert, inde)
    {
        var device = engine.Device;
        /**
         * Transpose the vertex objects to arrays
         */
        this.vertices = new Array(vert.length * 3);
        this.colors = new Array(vert.length * 4);
        this.indices = new Array(inde.length * 3);
        var vertexLocation = 0;
        var colorLocation = 0;
        for (var i = 0; i < vert.length; i++)
        {
            this.vertices[vertexLocation + 0] = vert[i].X;
            this.vertices[vertexLocation + 1] = vert[i].Y;
            this.vertices[vertexLocation + 2] = vert[i].Z;
            vertexLocation = vertexLocation + 3
            
            this.colors[colorLocation + 0] = vert[i].R;
            this.colors[colorLocation + 1] = vert[i].G;
            this.colors[colorLocation + 2] = vert[i].B;
            this.colors[colorLocation + 3] = vert[i].A;
            colorLocation = colorLocation + 4;
        }
        var indexLocation = 0;
        for (var i = 0; i < inde.length; i++)
        {
            this.indices[indexLocation + 0] = inde[i].indices[0];
            this.indices[indexLocation + 1] = inde[i].indices[1];
            this.indices[indexLocation + 2] = inde[i].indices[2];
            indexLocation = indexLocation + 3;
        }
        /**
         * Creating the VertexBuffer
         */
        this.VertexBuffer = device.createBuffer();
        device.bindBuffer(device.ARRAY_BUFFER, this.VertexBuffer);
        device.bufferData(device.ARRAY_BUFFER, new Float32Array(this.vertices), device.STATIC_DRAW);
        this.VertexBuffer.itemSize = 3;
        this.VertexBuffer.numItems = vert.length;
        /**
         * Creating the ColorBuffer
         */
        this.ColorBuffer = device.createBuffer();
        device.bindBuffer(device.ARRAY_BUFFER, this.ColorBuffer);
        device.bufferData(device.ARRAY_BUFFER, new Float32Array(this.colors), device.STATIC_DRAW);
        this.ColorBuffer.itemSize = 4;
        this.ColorBuffer.numItems = vert.length;
        /**
         * Creating the IndexBuffer
         */
        this.IndexBuffer = device.createBuffer();
        device.bindBuffer(device.ELEMENT_ARRAY_BUFFER, this.IndexBuffer);
        device.bufferData(device.ELEMENT_ARRAY_BUFFER, new Uint16Array(this.indices), device.STATIC_DRAW);
        this.IndexBuffer.itemSize = 1;
        this.IndexBuffer.numItems = this.indices.length;

        this.WorldMatrix = m4.scaling(1, 1, 1);

        this.Location = new Vertex(0, 0, 0);
        this.Revolution = new Vertex(0, 0, 0);
        this.RevolutionRadius = new Vertex(0, 0, 0);
        this.Rotation = new Vertex(0, 0, 0);
        this.Scale = new Vertex(1, 1, 1);

        this.RenderMode = "Solid";
    }
    Update(engine)
    {
        this.WorldMatrix = m4.scaling(this.Scale.X, this.Scale.Y, this. Scale.Z);
        this.WorldMatrix = m4.xRotate(this.WorldMatrix, degToRad(engine.Camera.Rotation.X));
        this.WorldMatrix = m4.yRotate(this.WorldMatrix, degToRad(engine.Camera.Rotation.Y));
        this.WorldMatrix = m4.zRotate(this.WorldMatrix, degToRad(engine.Camera.Rotation.Z));
        this.WorldMatrix = m4.translate(this.WorldMatrix, this.Location.X - engine.Camera.Location.X, this.Location.Y - engine.Camera.Location.Y, this.Location.Z - engine.Camera.Location.Z);
        this.WorldMatrix = m4.xRotate(this.WorldMatrix, degToRad(this.Revolution.X));
        this.WorldMatrix = m4.yRotate(this.WorldMatrix, degToRad(this.Revolution.Y));
        this.WorldMatrix = m4.zRotate(this.WorldMatrix, degToRad(this.Revolution.Z));        
        this.WorldMatrix = m4.translate(this.WorldMatrix, this.RevolutionRadius.X, this.RevolutionRadius.Y, this.RevolutionRadius.Z);
        this.WorldMatrix = m4.xRotate(this.WorldMatrix, degToRad(this.Rotation.X));
        this.WorldMatrix = m4.yRotate(this.WorldMatrix, degToRad(this.Rotation.Y));
        this.WorldMatrix = m4.zRotate(this.WorldMatrix, degToRad(this.Rotation.Z));
    }
    Render(engine)
    {
        this.Update(engine);
        var Device = engine.Device;
        Device.bindBuffer(Device.ARRAY_BUFFER, this.VertexBuffer);
        Device.vertexAttribPointer(engine.Shader_Program.vertexPositionAttribute, this.VertexBuffer.itemSize, Device.FLOAT, false, 0, 0);
        Device.bindBuffer(Device.ARRAY_BUFFER, this.ColorBuffer);
        Device.vertexAttribPointer(engine.Shader_Program.vertexColorAttribute, this.ColorBuffer.itemSize, Device.FLOAT, false, 0, 0);
        Device.bindBuffer(Device.ELEMENT_ARRAY_BUFFER, this.IndexBuffer);
        engine.SetShaderWorlds(this.WorldMatrix);
        var mode = Device.TRIANGLES;
        if (this.RenderMode === "WireFrame")
        {
            mode = Device.LINE_LOOP;
        }
        else if (this.RenderMode === "Lines")
        {
            mode = Device.LINES;
        }
        Device.drawElements(mode, this.IndexBuffer.numItems, Device.UNSIGNED_SHORT, 0);
    }
}
function degToRad(degrees)
{
    return degrees * Math.PI / 180;
}