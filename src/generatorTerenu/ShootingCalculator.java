package generatorTerenu;

import java.awt.Color;

import javafx.scene.shape.Ellipse;

/**
 * Obsahuje veskere vypocty tykajici se strelby - dopad strely, detekci zasahu.....
 * @author Vojtech Danisik
 *
 */
public class ShootingCalculator 
{
	/** atribut strelce*/
	private NamedPosition strelec;
	
	/** atribut cile*/
	private NamedPosition cil;
	
	/** atribut hitSpotu*/
	private NamedPosition hitSpot;
	
	/** atribut pro vytvareni trajektorie strelby*/
	private Trajectory trajectory;
	
	/** konstanty gravitaèního zrychlení*/
	private double gx; //m / s^-2
	private double gy; //m / s^-2
	private double gz; //m / s^-2
	
	/** konstanta odporu vzduchu*/
	private double b; //s^-1
	
	/** konstanta èasového kroku simulace*/
	private double deltaT; //s
	
	/** atribut pro praci s vetrem*/
	private Wind wind;
	
	/** atribut pro praci s terenem*/
	private Terrain terrain;
	
	/** konstanta pro prevod milimetru na metry*/
	private static double mmToM = 1000.0;
	
	/**
	 * ulozeni hodnot strelce a cile do atributu pro pozdejsi pracovani
	 * @param strelec hodnoty strelce
	 * @param cil hodnoty cile
	 */
	public ShootingCalculator(NamedPosition strelec, NamedPosition cil)
	{
		this.strelec = strelec;
		this.cil = cil;
		wind = null;
		trajectory = null;
		gx = 10;
		gy = 10;
		gz = 10;
		b = 0.05; //nezapomenout zmenit na 0.05
		deltaT = 0.01;
	}
	
	/**
	 * Vytvoreni hitSpotu podle azimutu a delky strelby od strelce
	 * @param azimuth pozadovany azimuth strelby
	 * @param distance delka strelby
	 * @return
	 */
	public void shoot(double azimuth, double elevace, double startV)
	{
		trajectory = new Trajectory();
		double radiansAzi = azimuth * (Math.PI/180);
		double radiansEle = elevace * (Math.PI/180);
		
		double x = strelec.x;
		double y = strelec.y;
		double xSloupec = (strelec.x / (terrain.getDeltaXInMM()/mmToM));
		double ySloupec = (strelec.y / (terrain.getDeltaYInMM()/mmToM));
		
		try
		{
			double z = terrain.getAltitudeInM(xSloupec, ySloupec);
			double vx = Math.cos(radiansAzi) * startV * Math.cos(radiansEle);
			double vy = -Math.sin(radiansAzi) * startV * Math.cos(radiansEle);
			double vz = Math.sin(radiansEle) * startV;
			
			double vwx = wind.getVx();
			double vwy = wind.getVy();
			double vwz = wind.getVz();
			
			trajectory.add(x,y,z);
			//System.out.println(terrain.getAltitudeInM(strelec.x / (terrain.getDeltaXInMM()/mmToM), strelec.y / (terrain.getDeltaYInMM()/mmToM)));
			
			while(true)
			{
				x = x + vx * deltaT;
				y = y + vy * deltaT;
				z = z + vz * deltaT;
				
				trajectory.add(x, y, z);
				//System.out.println(x + " " + y + " " + z);
				
				if (x < 0 || (x * GamePanel.scaleX) > Game.getSirkaPanelu()
						|| y < 0 || (y * GamePanel.scaleY) > Game.getVyskaPanelu())
				{
					System.out.println("Strela je mimo mapu");
					hitSpot = null;
					break;
				}
						
				if (z <= terrain.getAltitudeInM(x, y))
				{
					System.out.println("Zasazen teren");
					hitSpot = new NamedPosition(x, y, new Ellipse(), Color.ORANGE, 60.0, "hitSpot");
					break;
				}
				
				vx = vx + 0 * gx * deltaT + (1*vwx - vx) * b * deltaT;
				vy = vy + 0 * gy * deltaT + (1*vwy - vy) * b * deltaT;
				vz = vz + (-1) * gz * deltaT + (1*vwz - vz) * b * deltaT;
			}
		}
		catch(Exception e)
		{
			System.out.println("Strela je mimo mapu");
		}
		
		
	}
	
	/**
	 * Vraci nam hodnoty zasahu
	 * @return zasah
	 */
	public NamedPosition getHitSpot()
	{
		return hitSpot;
	}
	
	/**
	 * Zjistime, zda jsme trefili cil nebo ne
	 * @return true = trefili, false = netrefili
	 */
	public boolean testTargetHit()
	{
		try
		{
			double vzdalenost = hitSpot.getDistance(cil);
			if (vzdalenost >= (0) && vzdalenost <= hitSpot.size+cil.size) return true;
			return false;
		}
		catch (NullPointerException e)
		{
			return false;
		}
	}
	
	/**
	 * Nastavíme gravitaèní zrychlení x
	 * @param gx hodnota gravitaèního zrychlení x
	 */
	public void setGX(double gx)
	{
		this.gx = gx;
	}
	
	/**
	 * Nastavíme gravitaèní zrychlení y
	 * @param gy hodnota gravitaèního zrychlení y
	 */
	public void setGY(double gy)
	{
		this.gy = gy;
	}
	
	/**
	 * Nastavíme gravitaèní zrychlení z
	 * @param gx hodnota gravitaèního zrychlení z
	 */
	public void setGZ(double gz)
	{
		this.gz = gz;
	}
	
	/**
	 * Nastavíme hodnotu odporu vzduchu
	 * @param b hodnota odporu vzduchu
	 */
	public void setB(double b)
	{
		this.b = b;
	}
	
	/**
	 * Nastavíme hodnotu èasového kroku simulace
	 * @param deltaT hodnota èasového kroku simulace
	 */
	public void setDeltaT(double deltaT)
	{
		this.deltaT = deltaT;
	}
	
	/**
	 * Naplnime atribut vitru
	 * @param wind hodnota pozadovaneho vetru
	 */
	public void setWind (Wind wind)
	{
		this.wind = wind;
	}
	
	/**
	 * Atributu priradime hodnoty terenu
	 * @param terrain hodnoty nadmorskych vysek
	 */
	public void setTerrain (Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	/**
	 * Vraci nam trajektorii letu
	 * @return trajectory
	 */
	public Trajectory getTrajectory()
	{
		return trajectory;
	}
	
	
}

