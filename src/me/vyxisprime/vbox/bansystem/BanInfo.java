package me.vyxisprime.vbox.bansystem;

public class BanInfo{
	  private String player;
	  private String admin;
	  private String information;
	  private int x;
	  private int y;
	  private int z;
	  private int id;
	  
	  public BanInfo(String player, String admin, String information, int x, int y, int z, int id)
	  {
	    this.player = player;
	    this.admin = admin;
	    this.x = x;
	    this.y = y;
	    this.z = z;
	    this.information = information;
	    this.id = id;
	  }
	  
	  public String getPlayer()
	  {
	    return this.player;
	  }
	  
	  public String getAdmin()
	  {
	    return this.admin;
	  }
	  
	  public String getInfo()
	  {
	    return this.information;
	  }
	  
	  public int getX()
	  {
	    return this.x;
	  }
	  
	  public int getY()
	  {
	    return this.y;
	  }
	  
	  public int getZ()
	  {
	    return this.z;
	  }
	  
	  public int getID()
	  {
	    return this.id;
	  }
	}
