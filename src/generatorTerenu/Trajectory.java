package generatorTerenu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Ellipse;

/**
 * Uchovávání a vykreslení hodnot trajektorie
 * 
 * @author Vojtech Danisik
 *
 */
public class Trajectory 
{
	/** uchovava souradnice trajektorie*/
	private List<Double> trajectory;
	
	/** atribut pro praci s hitspotem*/
	private NamedPosition hitSpot;
	
	/**
	 * Zalozeni seznamu trajektorie
	 */
	public Trajectory()
	{
		trajectory = new ArrayList<>();
	}
	
	/**
	 * prida do seznamu bod
	 * @param x x-ova souradnice bodu
	 * @param y y-ova souradnice bodu
	 * @param z z-ova souradnice bodu
	 */
	public void add (double x, double y, double z)
	{
		trajectory.add(x);
		trajectory.add(y);
		trajectory.add(z);
	}
	
	/**
	 * Vraci nam pocet bodu v seznamu
	 * @return pocet bodu
	 */
	public int size()
	{
		return trajectory.size()/3;
	}
	
	/**
	 * Vraci nam X-ovou souradnici bodu na indexu
	 * @param index index na kterem chceme najit souradnici X
	 * @return souradnice X na indexu
	 */
	public double getX(int index)
	{
		return trajectory.get(index);
	}
	
	/**
	 * Vraci nam Y-ovou souradnici bodu na indexu
	 * @param index index na kterem chceme najit souradnici Y
	 * @return souradnice Y na indexu
	 */
	public double getY(int index)
	{
		return trajectory.get(index);
	}
	
	/**
	 * Vraci nam Z-ovou souradnici bodu na indexu
	 * @param index index na kterem chceme najit souradnici Z
	 * @return souradnice Z na indexu
	 */
	public double getZ(int index)
	{
		return trajectory.get(index);
	}
	
	public void draw(Graphics2D g2, double scaleX, double scaleY, int index)
	{
		double x = getX(index);
		double y = getY(index + 1);
		
		hitSpot = new NamedPosition(x, y, new Ellipse(), Color.ORANGE, 60.0, "hitSpot");
		hitSpot.draw(g2, scaleX, scaleY);	
	}
	
}
