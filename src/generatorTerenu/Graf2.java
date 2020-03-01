package generatorTerenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import javafx.geometry.Point3D;

/**
 * Vykresli profil terenu na poloprimce z pozice tanku smerem zadaneho azimutu az 
 * do spadnuti strely + bokorys strely 
 * @author Vojtech Danisik
 *
 */
public class Graf2 extends JPanel
{
	/** serial UID*/
	private static final long serialVersionUID = 1L;
	
	/** list obsahujici vysky terenu*/
	private List<Integer> listVysek;
	
	/** list obsahujici Z-tove souradnice trajektorie*/
	private List<Integer> listZ;
	
	/** list obsahujici X-ove souradnice trajektorie*/
	private List<Integer> listX;
	
	/** list obsahujici Y-ove souradnice trajektorie*/
	private List<Integer> listY;
	
	/** atribut pro praci s terenem*/
	private Terrain terrain;
	
	/** vytvoreni nove trajektorie - nepocita se s vlivem vetru*/
	private Trajectory trajectory;
	
	/** atribut pro praci se strelcem*/
	private NamedPosition strelec;
	
	/** elevace strely*/
	private double elevace;
	
	/** azimut strely*/
	private double azimut;
	
	/** zadana pocatecni rychlost strely*/
	private double pocRychlostStrelby;
	
	/** integracni krok*/
	private double deltaT = 0.01;
	
	/** odpor vzduchu*/
	private double b = 0.05;
	
	/** gravitacni pusobeni*/
	private int g = 10;
	
	/** maximalni nadmorska vyska strely-terenu*/
	private double maxVyska;
	
	/** konstanta pro prevod*/
	private final double mmToM = 1000.0;

	/** 
	 * vytvoreni panelu pro vykresleni grafu 2
	 * @param sirka panelu
	 * @param vyska panelu
	 */
	public Graf2(int sirka, int vyska)
	{
		this.setLocation(sirka + 50, 30);
		this.setSize(sirka, vyska);
		this.setPreferredSize(this.getSize());
		this.setBackground(Color.WHITE);
	}
	
	@Override
	public void paint(Graphics g) 
	{	
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		listVysek = new ArrayList<>();
		listX = new ArrayList<>();
		listZ = new ArrayList<>();
		listY = new ArrayList<>();
		double yZacatek = this.getHeight()-40;
		double vyskaOsy = yZacatek - 20;
		double sirkaOsy = this.getWidth()-60;
	
		//vypis elevace a azimutu
		g2.drawString("Azimut: " + String.valueOf(azimut) + "°", this.getWidth() - 200, 10);
		g2.drawString("Elevace: " + String.valueOf(elevace) + "°", this.getWidth() - 100, 10);
		//popis osy Y
		g2.drawString("Nadmorska vyska [m]", 0, 10);
		
		g2.translate(40, 0);
		
		//osy
		g2.setColor(Color.RED);
		g2.drawLine(0, 20, 0, (int)yZacatek); //y
		g2.drawLine(0, (int)yZacatek, (int)sirkaOsy, (int)yZacatek); //x
		g2.setColor(Color.BLACK);
		//nastaveni zacatku na 0,0 u grafu
		g2.translate(0, (int)yZacatek);
		
		//popis osy X
		g2.drawString("Vzdálenost od støelce [m]", (int)(sirkaOsy/2)-10, 35);
		
		//------Osy---------------
		//------------------------
		//carky na ose X
		g2.setColor(Color.RED);
		g2.drawLine((int)sirkaOsy/4, -5, (int)sirkaOsy/4, 5);
		g2.drawLine((int)sirkaOsy/2, -5, (int)sirkaOsy/2, 5);
		g2.drawLine((int)(sirkaOsy*3)/4, -5, (int)(sirkaOsy*3)/4, 5);
		g2.drawLine((int)sirkaOsy, -5, (int)sirkaOsy, 5);
		g2.setColor(Color.BLACK);
		
		//carky na ose Y
		g2.setColor(Color.RED);
		g2.drawLine(-5, -(int)vyskaOsy/4, 5, -(int)vyskaOsy/4);
		g2.drawLine(-5, -(int)vyskaOsy/2, 5, -(int)vyskaOsy/2);
		g2.drawLine(-5, -(int)(3*vyskaOsy/4), 5, -(int)(3*vyskaOsy/4));
		g2.drawLine(-5, -(int)vyskaOsy, 5, -(int)vyskaOsy);
		g2.setColor(Color.BLACK);
		
		//------------------------
		//------------------------
		
		//----hodnoty u carek-----
		//------------------------
		//pocatek
		g2.drawString("0", -3, 17); 
		g2.drawString("0", -17, 3); 
		
		
		vypocitejTrajektorii(); //vypocita trajektorii strely pri zanedbani vetru
		naplnList(); //vyplni list hodnotama vysky terenu
		
		maxVyska = (double)Collections.max(listZ);
		int x = 0;
		int y = (int)(terrain.getAltitudeInM(strelec.x, strelec.y) / maxVyska);
		Polygon p = new Polygon();
		p.addPoint(x, -y);
		for (int i = 0; i < listVysek.size()-1; i++)
		{
			x += 1;
			y = (int)listVysek.get(i);
			p.addPoint(x, -y);
		}
		p.addPoint(x, 0);
		
		Point3D point1 = new Point3D(listX.get(0), listY.get(0), listZ.get(0));
		Point3D point2 = new Point3D(listX.get(listX.size()-1), listY.get(listY.size()-1), listZ.get(listZ.size()-1));
		
		int vzdalenost = (int)point2.distance(point1);
		//cisla u X
		g2.drawString(String.valueOf(vzdalenost/4), (int)(sirkaOsy/4) - 10, 17);
		g2.drawString(String.valueOf(vzdalenost/2), (int)(sirkaOsy/2) - 10, 17);
		g2.drawString(String.valueOf((3*vzdalenost)/4), (int)(3*sirkaOsy/4) - 10, 17);
		g2.drawString(String.valueOf(vzdalenost), (int)(sirkaOsy) - 10, 17);
		
		
		//cisla u Y
		g2.drawString(String.valueOf(maxVyska/4), -38, -(int)(vyskaOsy/4) + 5);
		g2.drawString(String.valueOf(maxVyska/2), -38, -(int)(vyskaOsy/2) + 5);
		g2.drawString(String.valueOf(3*maxVyska/4), -38, -(int)(3*vyskaOsy/4) + 5);
		g2.drawString(String.valueOf(maxVyska), -38, -(int)(vyskaOsy) + 5);
		//------------------------
		//------------------------
		
		double scaleX = sirkaOsy / (double)x;
		double scaleY = vyskaOsy / maxVyska;
		g2.scale(scaleX, scaleY);
		g2.fillPolygon(p);
		g2.setColor(Color.GREEN);
		for (int i = 0; i < x-1; i++)
		{
			g2.drawLine(i, -listZ.get(i), i+1, -listZ.get(i+1));
		}
		
	}

	/** 
	 * naplni listy hodnotama 
	 */
	public void naplnList()
	{
		int x = (int)trajectory.getX(0);
		int y = (int)trajectory.getX(1);
		int vyska = (int)terrain.getAltitudeInM(x, y);
		listVysek.add(vyska);
		int predesleX = x;
		for (int i = 3; i < (trajectory.size()*3); i+=3)
		{
			x = (int)trajectory.getX(i);
			y = (int)trajectory.getY(i+1);
			if (predesleX != x)
			{
				listX.add(x);
				listY.add(y);
				listZ.add((int)trajectory.getZ(i+2));
				vyska = (int)terrain.getAltitudeInM(x, y);
				listVysek.add(vyska);
			}
		}
	}
	
	/**
	 * vypocita novou trajektorii strely, kde neni vliv vetru
	 */
	public void vypocitejTrajektorii()
	{
		
		double radiansAzi = azimut * (Math.PI/180);
		double radiansEle = elevace * (Math.PI/180);
		
		double x = strelec.x;
		double y = strelec.y;
		double xSloupec = (strelec.x / (terrain.getDeltaXInMM()/mmToM));
		double ySloupec = (strelec.y / (terrain.getDeltaYInMM()/mmToM));
		
		try
		{
			double z = terrain.getAltitudeInM(xSloupec, ySloupec);
			double vx = Math.cos(radiansAzi) * pocRychlostStrelby * Math.cos(radiansEle);
			double vy = -Math.sin(radiansAzi) * pocRychlostStrelby * Math.cos(radiansEle);
			double vz = Math.sin(radiansEle) * pocRychlostStrelby;
			
			trajectory = new Trajectory();
			trajectory.add(x,y,z);
			//System.out.println(terrain.getAltitudeInM(strelec.x / (terrain.getDeltaXInMM()/mmToM), strelec.y / (terrain.getDeltaYInMM()/mmToM)));
			
			while(true)
			{
				x = x + vx * deltaT;
				y = y + vy * deltaT;
				z = z + vz * deltaT;
				
				trajectory.add(x, y, z);
				
				if (x < 0 || (x * GamePanel.scaleX) > Game.getSirkaPanelu()
						|| y < 0 || (y * GamePanel.scaleY) > Game.getVyskaPanelu())
				{
					break;
				}
						
				if (z < terrain.getAltitudeInM(x, y))
				{
					trajectory.add(x, y, z);
					for (int i = 0; i < 4; i++)
					{
						vx = vx + (0 * g * deltaT)  - (vx * b * deltaT);
						vy = vy + (0 * g * deltaT) - (vy * b * deltaT);
						vz = vz + ((-1) * g * deltaT) - (vz * b * deltaT);
						
						x = x + vx * deltaT;
						y = y + vy * deltaT;
						z = z + vz * deltaT;
						trajectory.add(x, y, z);
					}
					break;
				}
				
				vx = vx + (0 * g * deltaT)  - (vx * b * deltaT);
				vy = vy + (0 * g * deltaT) - (vy * b * deltaT);
				vz = vz + ((-1) * g * deltaT) - (vz * b * deltaT);
			}
		}
		catch(Exception e)
		{
			System.out.println("Strela je mimo mapu");
		}
		
	
	}
	
	/**
	 * nastavi pocatecni rychlost strelby
	 * @param pocRychlostStrelby pocatecni rychlost strelby
	 */
	public void setPocRychlostStrelby(double pocRychlostStrelby)
	{	
		this.pocRychlostStrelby = pocRychlostStrelby;
	}
	
	/**
	 * nastavi teren
	 * @param terrain teren
	 */
	public void setTerrain(Terrain terrain)
	{	
		this.terrain = terrain;
	}
	
	/**
	 * nastavi strelce
	 * @param strelec strelec
	 */
	public void setStrelec(NamedPosition strelec)
	{	
		this.strelec = strelec;
	}
	
	/**
	 * nastavi elevaci strely
	 * @param elevace elevace
	 */
	public void setElevace(double elevace)
	{
		this.elevace = elevace;
	}
	
	/**
	 * nastavi azimut strely
	 * @param azimut azimut
	 */
	public void setAzimut(double azimut)
	{
		this.azimut = azimut;
	}
}
