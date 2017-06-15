package Graph;
public class Direction
{
    public String Instruction;
    public double Angle;
    public String Parent;
    public String Current;
    public String Child;
    public Direction(String _in)
    {
        Instruction = _in;
        Angle = 0;
    }
    public Direction(String _in, double _ang, String Par, String Cur, String Chi)
    {
        Instruction = _in;
        Angle = _ang;
        Parent = new String(Par);
        Current = new String(Cur);
        Child = new String(Chi);
    }
    public String toString()
    {
        return Instruction + " (" + Angle + ")";
    }
}
