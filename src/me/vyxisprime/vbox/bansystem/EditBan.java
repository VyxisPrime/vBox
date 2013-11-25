package me.vyxisprime.vbox.bansystem;

import java.sql.Timestamp;

public class EditBan {
	  public String name;
	  public String reason;
	  public String admin;
	  public long temptime;
	  
	  public EditBan(String name, String reason, String admin, Timestamp temptime)
	  {
	    this.temptime = temptime.getTime();
	    this.name = name;
	    this.reason = reason;
	    this.admin = admin;
	  }
	}
