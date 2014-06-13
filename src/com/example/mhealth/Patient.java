package com.example.mhealth;

public class Patient {
	
	
	private Integer patientId = null;
	private String identifier = null;
	private Integer age =0;
	private String givenName = null;
	private String familyName = null;
	private String middleName = null;
	
	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}

	private String birthDate = null;
	private String gender = null;
	
	public Patient() {
		
	}
	

    @Override
    public String toString() {
    	return givenName + " " + middleName + " " + familyName + " " + identifier;
    }
	
	public Integer getPatientId() {
		return patientId;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public String getGivenName() {
		return givenName;
	}
	
	public String getFamilyName() {
		return familyName;
	}
	
	public String getMiddleName() {
		return middleName;
	}
	
	public String getBirthdate() {
		return birthDate;
	}
	
	public String getGender() {
		return gender;
	}

	public void setPatientId(Integer pid) {
		patientId = pid;
	}

	public void setIdentifier(String id) {
		identifier = id;
	}

	public void setFamilyName(String n) {
		familyName = n;
	}

	public void setGivenName(String n) {
		givenName = n;
	}

	public void setMiddleName(String n) {
		middleName = n;
	}

	public void setBirthDate(String b) {
		birthDate = b;
	}

	public void setGender(String g) {
		gender = g;
	}

	public String getName(){
		String name = "";
		
		if(givenName != null)
			name = givenName;
		
		if(middleName != null){
			if(name.length() > 0)
				name += " ";
			
			name += middleName;
		}
		
		if(familyName != null){
			if(name.length() > 0)
				name += " ";
			
			name += familyName;
		}
		
		return name;
	}


}
