package generatorTerenu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Vykreslí graf závislosti dostøelu na poèáteèní rychlosti støelby
 * rozsah rychlosti støelby je od 0 do 2x zadané poèáteèní rychlosti
 * @author Vojtech Danisik
 *
 */
public class Graf1 extends JPanel
{
	/** serial UID*/
	private static final long serialVersionUID = 1L;
	
	/** elevace pod kterou strilime*/
	private double elevace;
	
	/** zadana pocatecni rychlost strelby*/
	private double pocRychlostStrelby;
	
	/** pomocna promenna pro pocitani s rychlosti strelby*/
	private double rychlostStrelby;
	
	/** integracni krok*/
	private double deltaT = 0.01;
	
	/** odpor vetru*/
	private double b = 0.05;
	
	/** gravitacni zrychleni*/
	private int g = 10;
	
	/** pole vzdalenosti, kterou strela urazi za danou rychlost*/
	private double[] vzdalenosti;
	
	/** maximalni vzdalenost*/
	private double maxVzdalenost;
	
	/** pocet vzdalenosti*/
	private int pocetVzdalenosti = 21;
	
	/** 
	 * Vytvori panel pro vykresleni grafu 1
	 * 
	 * @param sirka panelu
	 * @param vyska panelu
	 */
	public Graf1(int sirka, int vyska)
	{
		this.setLocation(30, 30);
		this.setSize(sirka, vyska);
		this.setPreferredSize(this.getSize());
		this.setBackground(Color.WHITE);
	}
	
	@Override
	public void paint(Graphics g) 
	{	
		//vypocetni cast
		naplnVzdalenosti();
		
		//graficka cast
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
			
		double yZacatek = this.getHeight()-40;
		double vyskaOsy = yZacatek - 20;
		double sirkaOsy = this.getWidth()-60;
	
		//vypis elevace
		g2.drawString("Elevace: " + String.valueOf(elevace) + "°", this.getWidth() - 100, 10);
		//popis osy Y
		g2.drawString("Urazena vzdalenost [m]", 0, 10);
		
		g2.translate(40, 0);
		
		//osy
		g2.setColor(Color.RED);
		g2.drawLine(0, 20, 0, (int)yZacatek); //y
		g2.drawLine(0, (int)yZacatek, (int)sirkaOsy, (int)yZacatek); //x
		g2.setColor(Color.BLACK);
		//nastaveni zacatku na 0,0 u grafu
		g2.translate(0, (int)yZacatek);
		
		//popis osy X
		g2.drawString("Rychlost [m/s]", (int)(sirkaOsy/2)-10, 35);
		
		//------Osy---------------
		//------------------------
		//carky na ose X
		g2.drawLine((int)sirkaOsy/4, 0, (int)sirkaOsy/4, -(int)vyskaOsy);
		g2.drawLine((int)sirkaOsy/2, 0, (int)sirkaOsy/2, -(int)vyskaOsy);
		g2.drawLine((int)(sirkaOsy*3)/4, 0, (int)(sirkaOsy*3)/4, -(int)vyskaOsy);
		g2.drawLine((int)sirkaOsy, 0, (int)sirkaOsy, -(int)vyskaOsy);
		
		g2.setColor(Color.RED);
		g2.drawLine((int)sirkaOsy/4, -5, (int)sirkaOsy/4, 5);
		g2.drawLine((int)sirkaOsy/2, -5, (int)sirkaOsy/2, 5);
		g2.drawLine((int)(sirkaOsy*3)/4, -5, (int)(sirkaOsy*3)/4, 5);
		g2.drawLine((int)sirkaOsy, -5, (int)sirkaOsy, 5);
		g2.setColor(Color.BLACK);
		
		//carky na ose Y
		g2.drawLine(0, -(int)vyskaOsy/4, (int)sirkaOsy, -(int)vyskaOsy/4);
		g2.drawLine(0, -(int)vyskaOsy/2, (int)sirkaOsy, -(int)vyskaOsy/2);
		g2.drawLine(0, -(int)(3*vyskaOsy/4), (int)sirkaOsy, -(int)(3*vyskaOsy/4));
		g2.drawLine(0, -(int)vyskaOsy, (int)sirkaOsy, -(int)vyskaOsy);
		
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

		//cisla u X
		g2.drawString(String.valueOf(pocRychlostStrelby/2), (int)(sirkaOsy/4) - 10, 17);
		g2.drawString(String.valueOf(pocRychlostStrelby), (int)(sirkaOsy/2) - 10, 17);
		g2.drawString(String.valueOf((3*pocRychlostStrelby)/2), (int)(3*sirkaOsy/4) - 10, 17);
		g2.drawString(String.valueOf(pocRychlostStrelby*2), (int)(sirkaOsy) - 10, 17);
		
		//cisla u Y
		g2.drawString(String.valueOf(maxVzdalenost/4), -38, -(int)(vyskaOsy/4) + 5);
		g2.drawString(String.valueOf(maxVzdalenost/2), -38, -(int)(vyskaOsy/2) + 5);
		g2.drawString(String.valueOf(3*maxVzdalenost/4), -38, -(int)(3*vyskaOsy/4) + 5);
		g2.drawString(String.valueOf(maxVzdalenost), -38, -(int)(vyskaOsy) + 5);
		//------------------------
		//------------------------
		
		//------vykresleni cary-------
		//----------------------------
		
		
		double[] x = new double[pocetVzdalenosti];
		double[] y = new double[pocetVzdalenosti];
		
		x[0] = sirkaOsy/pocetVzdalenosti;
		y[0] = (vzdalenosti[0] / maxVzdalenost) * vyskaOsy;
		
		for (int i = 1; i < pocetVzdalenosti; i++)
		{
			x[i] = x[i-1] + sirkaOsy/pocetVzdalenosti;
			y[i] = (vzdalenosti[i] / maxVzdalenost) * vyskaOsy;
		}
		
		for (int i = 0; i < pocetVzdalenosti-1; i++)
		{
			g2.drawLine((int)x[i], -(int)y[i], (int)x[i+1], -(int)y[i+1]);
		}
		
		//----------------------------
		//----------------------------
	}
	
	/**
	 * nastavi elevaci 
	 * @param elevace zadana elevace
	 */
	public void setElevace(double elevace)
	{
		this.elevace = elevace;
	}
	
	/**
	 * nastavi pocatecni rychlost strelby 
	 * @param pocRychlostStrelby zadana rychlost strelby
	 */
	public void setpocRychlostStrelby(double pocRychlostStrelby)
	{
		this.pocRychlostStrelby = pocRychlostStrelby;
	}
	
	/**
	 * nastavi integracni krok, pokud ho budeme chtit menit
	 * @param deltaT integracni krok
	 */
	public void setDeltaT(double deltaT)
	{
		this.deltaT = deltaT;
	}
	
	/**
	 * nastavi odpor vzduchu, pokud ho budeme chtit menit
	 * @param b odpor vzduchu
	 */
	public void setB(double b)
	{
		this.b = b;
	}
	
	/**
	 * zjisti urazenou vzdalenost strely pri zadane rychlosti strelby
	 * @param rychlostStrelby aktualni rychlost strelby
	 * @return vzdalenost, kterou strela urazi
	 */
	private double zjistiVzdalenost(double rychlostStrelby)
	{
		double radiansEle = elevace * (Math.PI/180);
		
		double z = 0;
		double x = 0;
		double vx = Math.cos(radiansEle) * rychlostStrelby;
		double vz = Math.sin(radiansEle) * rychlostStrelby; 
		
		while(true)
		{
			x = x + vx * deltaT;
			z = z + vz * deltaT;
			
			vx = vx - (vx * b * deltaT);
			vz = vz - (1 * g * deltaT)  - (vz * b * deltaT);
			if (z < 0) break;
		}
		return x;
	}
	
	/**
	 * naplni pole vzdalenosti[] hodnotama vzdalenosti
	 */
	private void naplnVzdalenosti()
	{
		maxVzdalenost = 0;
		vzdalenosti = new double[pocetVzdalenosti];
		rychlostStrelby = 0;
		for (int i = 0; i < vzdalenosti.length; i++)
		{
			vzdalenosti[i] = zjistiVzdalenost(rychlostStrelby);
			if (vzdalenosti[i] > maxVzdalenost) maxVzdalenost = (int)vzdalenosti[i];
			rychlostStrelby += (pocRychlostStrelby/10);
		}
	}
}
