package com.revo.auth.user;

/*
 * User class 
 * 
 * Created by Revo
 */

public final class User {

	/*
	 * Data
	 */
	
	private String login;
	private String password = "";
	private String email = "";
	private boolean logged = false;
	
	/*
	 * Getters & Setters
	 */
	
	public User(String login) {
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	
}
