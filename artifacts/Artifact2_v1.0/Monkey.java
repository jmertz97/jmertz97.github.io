// Joshua Mertz
// Project 2
// 4/20/2025

// package compsci; // package used on my local environment

public class Monkey extends RescueAnimal {
	private String tailLength, height, bodyLength, species;
	
	// constructor
	public Monkey(String name, String gender, String age, 
	String weight, String tailLength, String height, 
	String bodyLength, String species, String acquisitionDate, 
	String acquisitionCountry, String trainingStatus, 
	boolean reserved, String inServiceCountry) {
	    setName(name);
	    setGender(gender);
	    setAge(age);
	    setWeight(weight);
	    setTailLength(tailLength);
	    setHeight(height);
	    setBodyLength(bodyLength);
	    setSpecies(species);
	    setAcquisitionDate(acquisitionDate);
	    setAcquisitionLocation(acquisitionCountry);
	    setTrainingStatus(trainingStatus);
	    setReserved(reserved);
	    setInServiceCountry(inServiceCountry);
	}

	// getter (accessor) method
	public String getTailLength() {
		return tailLength;
	}

	// setter (mutator) method
	public void setTailLength(String tailLength) {
		this.tailLength = tailLength;
	}

	// getter method
	public String getHeight() {
		return height;
	}

	// setter method
	public void setHeight(String height) {
		this.height = height;
	}

	// getter method
	public String getBodyLength() {
		return bodyLength;
	}

	// setter method
	public void setBodyLength(String bodyLength) {
		this.bodyLength = bodyLength;
	}

	// getter method
	public String getSpecies() {
		return species;
	}

	// setter method
	public void setSpecies(String species) {
		this.species = species;
	}
}
