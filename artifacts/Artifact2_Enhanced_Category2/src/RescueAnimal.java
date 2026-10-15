import java.util.List;
import java.util.Locale;
import java.time.LocalDate;
import java.util.Set;
import java.lang.String;

public class RescueAnimal {

    private String name;
    private String gender;
    private String acquisitionCountry;
	private String inServiceCountry;
	private String trainingStatus;
    private LocalDate acquisitionDate;
    private double age;
    private double weight;
    private boolean reserved;
    
    public static final String STATUS_IN_SERVICE = "In Service";  // driver file needs separate access to this string
    public static final LocalDate DATE_EARLIEST = LocalDate.of(2001, 1, 1);  // this value would depend on the organization's history
    
    // master lists of valid genders and statuses, for validation in both child classes and in the driver class
    private static final List<String> VALID_GENDERS = List.of("Male", "Female", "Unknown");
    private static final List<String> VALID_TRAINING_STATUSES = List.of("Intake", "Phase I", "Phase II", "Phase III", STATUS_IN_SERVICE);
    private static final Set<String> VALID_COUNTRY_CODES = Locale.getISOCountries(Locale.IsoCountryCode.PART1_ALPHA3);  // 3-letter country codes
    
    // constructor overridden, implemented by child classes Dog and Monkey
    public RescueAnimal() {
    }
    
	
	public static List<String> getValidGenders() {
		return RescueAnimal.VALID_GENDERS;
	}

	
	public static List<String> getValidTrainingStatuses() {
		return RescueAnimal.VALID_TRAINING_STATUSES;
	}

	
	public static Set<String> getValidCountryCodes() {
		return RescueAnimal.VALID_COUNTRY_CODES;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	
	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		if (VALID_GENDERS.contains(gender)) {
			this.gender = gender;
		}
		else {
			throw new IllegalArgumentException("Invalid country code provided: " + gender);
		}
	}


	public double getAge() {
		return age;
	}


	public void setAge(double age) {
		if (age > 0.0) {
			this.age = age;
		}
		else {
			throw new IllegalArgumentException("Age must be positive. Value provided: " + age);
		}
	}


	public double getWeight() {
		return weight;
	}


	public void setWeight(double weight) {
		if (weight > 0.0) {
			this.weight = weight;
		}
		else {
			throw new IllegalArgumentException("Weight must be positive. Value provided: " + weight);
		}
	}


	public LocalDate getAcquisitionDate() {
		return acquisitionDate;
	}


	public void setAcquisitionDate(LocalDate acquisitionDate) {
		// date cannot be in the future, or before the organization's chosen cutoff
		// !date.isAfter() ensures today's date is allowed; date.isBefore() prohibits it
		if (acquisitionDate.isAfter(DATE_EARLIEST) && !acquisitionDate.isAfter(LocalDate.now())) {
			this.acquisitionDate = acquisitionDate;
        }
		else {
			throw new IllegalArgumentException("Invalid date provided: " + acquisitionDate);
		}
	}


	public String getAcquisitionLocation() {
		return acquisitionCountry;
	}


	public void setAcquisitionLocation(String acquisitionCountry) {
		if (VALID_COUNTRY_CODES.contains(acquisitionCountry)) {
			this.acquisitionCountry = acquisitionCountry;
		}
		else {
			throw new IllegalArgumentException("Invalid country code provided: " + acquisitionCountry);
		}
	}


	public boolean getReserved() {
		return reserved;
	}


	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}


	public String getInServiceCountry() {
		return inServiceCountry;
	}


	public void setInServiceCountry(String inServiceCountry) {
		if (VALID_COUNTRY_CODES.contains(inServiceCountry)) {
			this.inServiceCountry = inServiceCountry;
		}
		else {
			throw new IllegalArgumentException("Invalid country code provided: " + inServiceCountry);
		}
	}


	public String getTrainingStatus() {
		return trainingStatus;
	}


	public void setTrainingStatus(String trainingStatus) {
		this.trainingStatus = trainingStatus;
	}
}
