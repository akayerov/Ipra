package com.akayerov.firstmvcprj;

public class User {
	String name;
	String password;
	boolean admin;

	public User(String name, String password, boolean admin) {
		super();
		this.name = name;
		this.password = password;
		this.admin = admin;
	}

	public User() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

}
