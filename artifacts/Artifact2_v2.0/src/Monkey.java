import java.time.LocalDate;
import java.util.List;

public class Monkey extends RescueAnimal {
	private String species;
	private double tailLength, height, bodyLength;
	
    // master list of valid species, for validation in both this class and the driver class
	private static final List<String> VALID_SPECIES = List.of("Capuchin", "Guenon", "Macaque", "Marmoset", "Squirrel monkey", "Tamarin");

	public Monkey(String name, String gender, double age, 
	double weight, double tailLength, double height, 
	double bodyLength, String species, LocalDate acquisitionDate, 
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
	
	public static List<String> getValidSpecies() {
		return Monkey.VALID_SPECIES;
	}

	public double getTailLength() {
		return tailLength;
	}

	public void setTailLength(double tailLength) {
		if (tailLength > 0.0) {
			this.tailLength = tailLength;
		}
		else {
			throw new IllegalArgumentException("Tail length must be positive. Value provided: " + tailLength);
		}
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		if (height > 0.0) {
			this.height = height;
		}
		else {
			throw new IllegalArgumentException("Height must be positive. Value provided: " + height);
		}
	}

	public double getBodyLength() {
		return bodyLength;
	}

	public void setBodyLength(double bodyLength) {
		if (bodyLength > 0.0) {
			this.bodyLength = bodyLength;
		}
		else {
			throw new IllegalArgumentException("Body length must be positive. Value provided: " + bodyLength);
		}
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		if (VALID_SPECIES.contains(species)) {
			this.species = species;
		}
		else {
			throw new IllegalArgumentException("Invalid species provided: " + species + "\nValid species include: " + String.join(", ", VALID_SPECIES));
		}
	}
}
