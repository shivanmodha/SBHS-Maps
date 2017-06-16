package studios.vanish.PathFinder;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
public class Main
{
	public static String Theme = "Nimbus"; //Metal, Nimbus, CDE/Motif, Windows, Windows Classic
	public static void main(String[] args)
	{
		try
		{
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			{
				if (Theme.equals(info.getName()))
				{
					UIManager.setLookAndFeel(info.getClassName());
				}
			}
		}
		catch (Exception e)
		{
			
		}
		new Main();
	}
	public Main()
	{
		new Editor();
	}
}