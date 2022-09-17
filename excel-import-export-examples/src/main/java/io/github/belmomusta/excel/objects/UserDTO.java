package io.github.belmomusta.excel.objects;

import io.github.belmomusta.excel.api.annotation.ExcelColumn;
import io.github.belmomusta.excel.api.annotation.ExcelRow;

@ExcelRow(ignoreHeaders = false)
public class UserDTO {
	@ExcelColumn
	private String email;
	@ExcelColumn
	private String profile;
	@ExcelColumn
	private char[] password;
	@ExcelColumn
	private char[] passwordConfirmation;
	
	
	public char[] getPassword() {
		return password;
	}
	
	
	public char[] getPasswordConfirmation() {
		return passwordConfirmation;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getProfile() {
		return profile;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}