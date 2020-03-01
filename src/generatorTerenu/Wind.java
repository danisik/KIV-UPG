package generatorTerenu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

/**
 * Generace parametru vetru - rychlost a smer
 * 
 * @author Vojtech Danisik
 *
 */
public class Wind 
{

	/** maximalni rychlost vetru*/
	private double maxRychlost;
	
	/** maximalni uhel, ktery muze vitr mit*/
	private double maxAzimut = 2*Math.PI;
	
	/** atribut pro praci s nahodnyma cislama*/
	private Random r = new Random();
	
	/** konstanta pro prevod stupnu na radiany*/
	private double prevodStupneRadiany = Math.PI/180;
	
	/** urceni rychlosti vetru*/
	private double rychlost = r.nextInt(25);
	
	/** urceni uhlu vetru*/
	private double azimut = r.nextInt((int)(2*Math.PI));
	
	/**
	 * Vytvori nam objekt vitr 
	 * @param maxRychlost maximalni rychlost vetru
	 */
	public Wind(double maxRychlost)
	{
		this.maxRychlost = maxRychlost;
	}
	
	/**
	 * Vraci nam rychlost vetru
	 * @return rychlost vetru
	 */
	public double getRychlost()
	{
		return rychlost;
	}
	
	/**
	 * Vraci nam azimut vetru
	 * @return azimut vetru
	 */
	public double getAzimut()
	{
		return azimut;
	}
	
	/** 
	 * Vygeneruje velikost vìtru a azimutu
	 */
	public void generateParams()
	{
		if (rychlost <= maxRychlost && rychlost > 0) rychlost = rychlost + r.nextInt(3) - 1;
		else if (rychlost < 0) rychlost = rychlost + r.nextInt(10);
		else rychlost = rychlost - r.nextInt(3);
		
		if (azimut < maxAzimut) azimut = azimut + (double)(r.nextInt(25) - 12)/100;
		else if (azimut >= maxAzimut) azimut = 0;
		else if (azimut < 0) azimut = maxAzimut;
	}
	
	/**
	 * Vykresleni sipky
	 * @param g2 kreslici nastroj
	 */
	public void draw(Graphics2D g2) 
	{	
		int x1 = 50;
		int y1 = 70;
		int x2 = (int)((x1 + Math.cos(azimut) * 20) + rychlost/2);
		int y2 = (int)((y1 - Math.sin(azimut) * 20) + rychlost/2);
		
		double sx = x2 - x1;
		double sy = y2 - y1;
		
		double dv = Math.sqrt(sx*sx + sy*sy);
		
		sx /= dv;
		sy /= dv;
		
		double kx = -sy;
		double ky = sx;
		
		int tloustkaSipky = 5;
		
		kx *= tloustkaSipky;
		ky *= tloustkaSipky;
		sx *= tloustkaSipky;
		sy *= tloustkaSipky;
		
		g2.setColor(Color.YELLOW);
		g2.setStroke(new BasicStroke(2));
		g2.drawLine(x1,y1,x2,y2); //cara
		
		g2.draw(new Line2D.Double(x2 - sx + kx, y2 - sy + ky, x2, y2));
		g2.draw(new Line2D.Double(x2 - sx - kx, y2 - sy - ky, x2, y2));
	}
	
	/**
 	* Vraci nam x-ovou souradnici vektoru vetru
 	* @return x-ova souradnice vektoru
 	*/
	public double getVx()
	{
		return Math.cos(azimut*prevodStupneRadiany) * rychlost;
	}
	
	/**
 	* Vraci nam y-ovou souradnici vektoru vetru
 	* @return y-ova souradnice vektoru
 	*/
	public double getVy()
	{
		return (-Math.sin(azimut*prevodStupneRadiany)) * rychlost;
	}
	
	/**
 	* Vraci nam z-ovou souradnici vektoru vetru
 	* @return z-ova souradnice vektoru
 	*/
	public double getVz()
	{
		return 0;
	}
}
