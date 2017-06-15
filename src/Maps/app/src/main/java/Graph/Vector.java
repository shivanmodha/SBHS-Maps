package Graph;

public class Vector
{
    public double X;
    public double Y;
    public double Z;
    public Vector()
    {
        this(0, 0, 0);
    }
    public Vector(double _in)
    {
        this(_in, _in, _in);
    }
    public Vector(double _x, double _y, double _z)
    {
        X = _x;
        Y = _y;
        Z = _z;
    }
    public Vector(Vector base)
    {
        this(base.X, base.Y, base.Z);
    }
    public Vector add(Vector _point)
    {
        return new Vector(X + _point.X, Y + _point.Y, Z + _point.Z);
    }
    public Vector subtract(Vector _point)
    {
        return new Vector(X - _point.X, Y - _point.Y, Z - _point.Z);
    }
    public Vector multiply(Vector _point)
    {
        return new Vector(X * _point.X, Y * _point.Y, Z * _point.Z);
    }
    public Vector divide(Vector _point)
    {
        return new Vector(X / _point.X, Y / _point.Y, Z / _point.Z);
    }
    public boolean equals(Vector _point)
    {
        if ((X == _point.X) && (Y == _point.Y) && (Z == _point.Z))
        {
            return true;
        }
        return false;
    }
    public double dot(Vector vert)
    {
        return (X * vert.X) + (Y * vert.Y) + (Z * vert.Z);
    }
    public Vector cross(Vector vert)
    {
        return new Vector((Y * vert.Z) - (Z * vert.Y), (X * vert.Z) - (Z * vert.X), (X * vert.Y) - (Y * vert.X));
    }
    public double magnitude()
    {
        return Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2));
    }
    public String toString()
    {
        return "(" + X + ", " + Y + ", " + Z + ")";
    }
}
