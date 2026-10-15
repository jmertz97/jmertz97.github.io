import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.HashMap;


public class Driver {
	// defining various string literals
	private static final String MENU_INTAKE = "Intake a new animal";
	private static final String MENU_RESERVE = "Reserve an animal";
	private static final String MENU_PRINT_DOGS = "Print a list of all dogs";
	private static final String MENU_PRINT_MONKEYS = "Print a list of all monkeys";
	private static final String MENU_PRINT_UNRESERVED = "Print a list of all unreserved animals";
	private static final String MENU_EXIT = "Quit application";
	
	private static final String DOG = "Dog";
	private static final String MONKEY = "Monkey";

	// defining option lists as lists of strings
	private static final List<String> OPTIONS_MAIN_MENU = List.of(MENU_INTAKE, MENU_RESERVE, MENU_PRINT_DOGS, MENU_PRINT_MONKEYS, MENU_PRINT_UNRESERVED, MENU_EXIT);
	private static final List<String> OPTIONS_ANIMAL_TYPE = List.of(DOG, MONKEY);
    private static final List<String> OPTIONS_YES_NO = List.of("Yes", "No");

    // the date format to be used across the driver class file
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // hashmaps storing Dogs and Monkeys
    private static HashMap<String, Dog> dogMap = new HashMap<>();
    private static HashMap<String, Monkey> monkeyMap = new HashMap<>();
    private static HashMap<String, ArrayList<Dog>> dogMapPerCountry = new HashMap<>();
    private static HashMap<String, ArrayList<Monkey>> monkeyMapPerCountry = new HashMap<>();
    
    
    public static void main(String[] args) {
        String input;
    	Scanner scnr = new Scanner(System.in);
        
        initializeDogList();
        initializeMonkeyList();
        
        do {
            input = promptMenuInput("\n\n\t\t\t\tRescue Animal System Menu", OPTIONS_MAIN_MENU, scnr);
            
            switch (input) {
		    	case MENU_INTAKE:
		    		intakeNewAnimal(scnr);
		    		break;
		    	case MENU_RESERVE:
		    		reserveAnimal(scnr);
		    		break;
		    	case MENU_PRINT_DOGS:
		    		printAnimals(DOG);
		    		break;
		    	case MENU_PRINT_MONKEYS:
		    		printAnimals(MONKEY);
		    		break;
		    	case MENU_PRINT_UNRESERVED:
		    		printAvailableAnimals();
		    		break;
		    	case MENU_EXIT:
		    		System.out.print("Exiting program...");
		    	default:
		    		// repeat menu loop on any other input
		    		break;
            }
            
        } while (!input.equals(MENU_EXIT)); 
    }


    // Adds Dogs to a list for testing
    public static void initializeDogList() {
    	// populates the hashmaps with Dog instances for testing
    	List<Dog> dogs_to_add = List.of(
	    	new Dog("Spot", "German Shepherd", "Male", 1, 25.6, LocalDate.parse("2019-05-12", DATE_FORMAT), "USA", "Intake", false, "USA"),
	        new Dog("Rex", "Great Dane", "Male", 3, 35.2, LocalDate.parse("2019-02-03", DATE_FORMAT), "USA", "Phase I", false, "USA"),
	        new Dog("Bella", "Chihuahua", "Female", 4, 25.6, LocalDate.parse("2019-12-12", DATE_FORMAT), "CAN", RescueAnimal.STATUS_IN_SERVICE, true, "CAN")
        );
    	
    	String hashKey;
    	for (Dog dog : dogs_to_add) {
    		hashKey = createHashKey(dog);
    		dogMap.put(hashKey, dog);
            dogMapPerCountry.computeIfAbsent(dog.getInServiceCountry(), k -> new ArrayList<Dog>()).add(dog);
    	}
    }

    
    public static void initializeMonkeyList() {
    	// populates the hashmaps with Monkey instances for testing
        List<Monkey> monkeys_to_add = List.of(
        	new Monkey("Ham", "Male", 1, 25.6, 9.75, 9.75, 9.75, "Capuchin", LocalDate.parse("2019-05-12", DATE_FORMAT), "USA", "Intake", false, "USA"),
            new Monkey("Enos", "Male", 3, 35.2, 5.92, 5.92, 5.92, "Marmoset", LocalDate.parse("2020-02-03", DATE_FORMAT), "USA", "Phase I", false, "USA"),
            new Monkey("Martine", "Male", 4, 25.6, 6.0, 6.0, 6.0, "Macaque", LocalDate.parse("2019-12-12", DATE_FORMAT), "FRA", RescueAnimal.STATUS_IN_SERVICE, false, "CAN"),
            new Monkey("Juan", "Male", 3, 35.2, 5.92, 5.92, 5.92, "Capuchin", LocalDate.parse("2025-04-20", DATE_FORMAT), "ARG", RescueAnimal.STATUS_IN_SERVICE, true, "ARG")
    	);
    	
    	String hashKey;
    	for (Monkey monkey : monkeys_to_add) {
    		hashKey = createHashKey(monkey);
    		monkeyMap.put(hashKey, monkey);
    		monkeyMapPerCountry.computeIfAbsent(monkey.getInServiceCountry(), k -> new ArrayList<Monkey>()).add(monkey);
    	}
        
    }


    public static void intakeNewAnimal(Scanner scnr) {
        // pre-declaring specific variables
        LocalDate acquisitionDate;
        boolean reserved;

        System.out.println("INTAKE FORM\nFollow the prompts to enter a new animal into the system.");
        // pressing enter without typing anything allows the user to cancel out of the intake flow
        System.out.println("You may cancel the intake process at any point by providing empty input.");
        
        // get the animal type first
        String animalType = promptMenuInput("Select the type of animal to be added:", OPTIONS_ANIMAL_TYPE, scnr);
        if (animalType.isEmpty()) {
        	return;
        }
        
        // then fill fields used for hashing
        System.out.println("Enter the " + animalType.toLowerCase() + "'s name:");
        String name = scnr.nextLine().trim();
        if (name.isEmpty()) {
        	return;
        }

        // date
        LocalDate now = LocalDate.now();
        while (true) {
            System.out.println("Enter the acquisition date for the " + animalType.toLowerCase() + " in YYYY-MM-DD format:");
            String dateInput = scnr.nextLine().trim();
            if (dateInput.isEmpty()) {
                return;
            }
            try {
                acquisitionDate = LocalDate.parse(dateInput, DATE_FORMAT);
        		// !date.isAfter() ensures today's date is allowed; date.isBefore() prohibits it
                if (acquisitionDate.isAfter(RescueAnimal.DATE_EARLIEST) && !acquisitionDate.isAfter(now)) {
                	break;
                }
                System.out.println("Date must be between " + RescueAnimal.DATE_EARLIEST + " and " + now + ".");
            } catch (DateTimeParseException e) {
                System.out.println("Invalid entry provided.");
            }
        }
        
        // with the name and date, hash can be built and checked
        String hashKey = createHashKey(name, acquisitionDate);
        if ((animalType.equals(DOG) && dogMap.containsKey(hashKey)) || (animalType.equals(MONKEY) && monkeyMap.containsKey(hashKey))) {
            System.out.println(animalType + " \"" + name + "\" (acquired: " + acquisitionDate + ") already present in the system.");
            return;
        }
        
        // then fill remaining fields
        String gender = promptMenuInput("Select the " + animalType.toLowerCase() + "'s gender from the following options:", 
        		RescueAnimal.getValidGenders(), scnr);
        if (gender.isEmpty()) {
        	return;
        }
        
        Optional<Double> ageInput = promptDouble("Enter the " + animalType.toLowerCase() + "'s age:", scnr);
        if (ageInput.isEmpty()) {
        	return;
        }
        double age = ageInput.get();
        
        Optional<Double> weightInput = promptDouble("Enter the " + animalType.toLowerCase() + "'s weight in lbs:", scnr);
        if (weightInput.isEmpty()) {
        	return;
        }
        double weight = weightInput.get();
        
        String trainingStatus = promptMenuInput("Select the " + animalType.toLowerCase() + "'s training status from the following options:", 
        		RescueAnimal.getValidTrainingStatuses(), scnr);
        if (trainingStatus.isEmpty()) {
        	return;
        }
        
        String reservedInput = promptMenuInput("Select whether the " + animalType.toLowerCase() + " is reserved:", OPTIONS_YES_NO, scnr);
        if (reservedInput.isEmpty()) {
        	return;
        }
        else {
        	reserved = reservedInput.equals("Yes");
        }
        
        String inServiceCountry = promptCountryCode("Enter the " + animalType.toLowerCase() + "'s current country of service as a three-letter country code:", scnr);
        if (inServiceCountry.isEmpty()) {
        	return;
        }
        
        String acqCountry = promptCountryCode("Enter the country in which the " + animalType.toLowerCase() + " was acquired as a three-letter country code:", scnr);
        if (acqCountry.isEmpty()) {
        	return;
        } 
        
        // obtain type-specific fields, then create the object and return
        if (animalType.equals(DOG)) {
            System.out.println("Enter the dog's breed:");
            String breed = scnr.nextLine().trim();
            if (breed.isEmpty()) {
            	return;
            }
            
            Dog newDog = new Dog(name, breed, gender, age, weight, acquisitionDate, acqCountry, trainingStatus, reserved, inServiceCountry);
            dogMap.put(hashKey, newDog);
            dogMapPerCountry.computeIfAbsent(inServiceCountry, k -> new ArrayList<Dog>()).add(newDog);
            
            System.out.println("New dog \"" + name + "\" (acquired: " + acquisitionDate + ") has been registered.");
            return;
        }
        else if (animalType.equals(MONKEY)) {
        	String species = promptMenuInput("Select the monkey's species:", Monkey.getValidSpecies(), scnr);
            if (species.isEmpty()) {
            	return;
            }
            
            Optional<Double> heightInput = promptDouble("Enter the monkey's height:", scnr);
            if (heightInput.isEmpty()) {
            	return;
            }
            double height = heightInput.get();
            
            Optional<Double> bodyLengthInput = promptDouble("Enter the monkey's body length:", scnr);
            if (bodyLengthInput.isEmpty()) {
            	return;
            }
            double bodyLength = bodyLengthInput.get();
            
            Optional<Double> tailLengthInput = promptDouble("Enter the monkey's tail length:", scnr);
            if (tailLengthInput.isEmpty()) {
            	return;
            }
            double tailLength = tailLengthInput.get();
            
            Monkey newMonkey = new Monkey(name, gender, age, weight, tailLength, height, bodyLength, species, acquisitionDate, acqCountry, trainingStatus, reserved, inServiceCountry);
            monkeyMap.put(hashKey, newMonkey);
            monkeyMapPerCountry.computeIfAbsent(inServiceCountry, k -> new ArrayList<Monkey>()).add(newMonkey);
            
            System.out.println("New monkey \"" + name + "\" (acquired: " + acquisitionDate + ") has been registered.");
            return;
        }
    }

    
    public static void reserveAnimal(Scanner scnr) {
        String animalType = promptMenuInput("Select a type of animal to reserve:", OPTIONS_ANIMAL_TYPE, scnr);
        if (animalType.isEmpty()) {
        	return;
        }        
        
        String inServiceCountry = promptCountryCode("Enter the country to reserve a " + animalType.toLowerCase() + " in, as a three-letter country code.", scnr);
        if (inServiceCountry.isEmpty()) {
        	return;
        }
        
        if (animalType.equals(DOG)) {
            if (!dogMapPerCountry.containsKey(inServiceCountry)) {
                System.out.println("No dogs registered in " + inServiceCountry + ".");
                return;
            }
            for (Dog dog : dogMapPerCountry.get(inServiceCountry)) {
                if (!dog.getReserved()) {  // reserve the first dog that matches 
                    System.out.println("Reserving " + dog.getName() + " in " + dog.getInServiceCountry() + ".");
                    dog.setReserved(true);
                    return;
                }
            }
            System.out.println("No available dogs in " + inServiceCountry + ".");
            return;
        }
        else if (animalType.equals(MONKEY)) {
            if (!monkeyMapPerCountry.containsKey(inServiceCountry)) {
                System.out.println("No monkeys registered in " + inServiceCountry + ".");
                return;
            }
            for (Monkey monkey : monkeyMapPerCountry.get(inServiceCountry)) {
                if (!monkey.getReserved()) {
                    System.out.println("Reserving " + monkey.getName() + " in " + monkey.getInServiceCountry() + ".");
                    monkey.setReserved(true);
                    return;
                }
            }
            System.out.println("No available monkeys in " + inServiceCountry + ".");
            return;
        }
    }

    
    public static void printAnimals(String animalType) {
    	// animalType passed instead of menu option/selection
    	// available animals print option moved to separate method
    	String output = "";
    	
    	if (animalType.equals(DOG)) {
    		for (Dog dog : dogMap.values()) {
	    		output += "\n\t\t" + dog.getName() + " (country: " + dog.getInServiceCountry() + ")";
    		}
        }
    	else if (animalType.equals(MONKEY)) {
    		for (Monkey monkey : monkeyMap.values()) {
	    		output += "\n\t\t" + monkey.getName() + " (country: " + monkey.getInServiceCountry() + ")";
    		}
    	}
    	else {
    		return;
    	}
    	
        if (output.isEmpty()) {
        	output = "\tNo " + animalType.toLowerCase() + "s found.";
        }    	
        System.out.println(animalType.toUpperCase() + "S:\n" + output);
    }
    

    public static void printAvailableAnimals() {
    	// empty strings to be built
        String outputDog = "";
        String outputMonkey = "";
        
        // build the output text as strings before printing
        for (Dog dog : dogMap.values()) {
            if (!dog.getReserved()) {
            	outputDog += "\n\t\t" + dog.getName() + " (country: " + dog.getInServiceCountry() + ")";
            }
        }
        if (!outputDog.isEmpty()) {
        	// if the output string has been populated, prepend the header
        	outputDog = "\n\tDOGS:" + outputDog;
        }
        else {
        	outputDog = "\n\tDOGS:\n\tNo dogs are available.";
        }
        for (Monkey monkey : monkeyMap.values()) {
            if (!monkey.getReserved()) {
            	outputMonkey += "\n\t\t" + monkey.getName() + " (country: " + monkey.getInServiceCountry() + ")";
            }
        }
        if (!outputMonkey.isEmpty()) {
        	outputMonkey = "\n\tMONKEYS:" + outputMonkey;
        }
        else {
        	outputMonkey = "\n\tMONKEYS:\n\tNo monkeys are available.";
        }
        
        System.out.println("UNRESERVED ANIMALS:\n" + outputDog + "\n" + outputMonkey);
    }
    
    
    // helper methods for live input validation during intake
    private static String promptMenuInput(String prompt, List<String> options, Scanner scnr) {
    	int i = 0;
    	int userChoice = 0;
    	String optionsMenu = "";
    	String userInput;
    	for (String option : options) {
    		i += 1;  // index 0 displayed to user as option 1
    		optionsMenu += "\n[" + i + "] " + option;
        }
    	do {
    		System.out.println(prompt + optionsMenu);
    		System.out.println();
    		userInput = scnr.nextLine();
    		if (userInput.isEmpty()) {
    			// empty string returned with empty input
    			// represents "canceling" input, must be handled by caller
    			return "";
    		}
    		userChoice = Character.getNumericValue(userInput.charAt(0));
    	}
    	while (userChoice < 1 || userChoice > options.size());
    	
    	return options.get(userChoice - 1);
    }
    
    
    // optional returns 
    private static Optional<Double> promptDouble(String prompt, Scanner scnr) {
    	Double inputDouble = 0.0;
    	String input;
        while (true) {
        	System.out.println(prompt);
        	input = scnr.nextLine().trim();
        	if (input.isEmpty()) {
        		return Optional.empty();
        	}
        	try {
        		inputDouble = Double.parseDouble(input);
        		if (inputDouble > 0.0) {
        			return Optional.of(inputDouble);
        		}
        		else {
        			// all instances of doubles in RescueAnimal & child classes presumed to be constrained to non-zero values
        			System.out.println("Negative or zero value provided.");
        		}
        	} catch (NumberFormatException e) {
        		System.out.println("Invalid entry provided.");
        	}
        }
    }
    
    
    private static String promptCountryCode(String prompt, Scanner scnr) {
        String input;
    	while (true) {
        	System.out.println(prompt);
        	input = scnr.nextLine().trim().toUpperCase();
        	if (input.isEmpty()) {
    			// empty string returned with empty input
    			// represents "canceling" input, must be handled by caller
        		return "";
        	}
        	// country code must be an ISO-compatible 3-letter country code
        	else if (RescueAnimal.getValidCountryCodes().contains(input)) {
        		return input;
        	}
        	System.out.println("Invalid country code entered.");
        }
    }
    
    
    // method that handles all hash key creation
    private static String createHashKey(String name, LocalDate acquisitionDate) {    	
    	return (name + "||" + acquisitionDate);
    }
    
    // method overload to allow hash key creation from pre-instantiated object
    private static String createHashKey(RescueAnimal animal) {
        // grabs the relevant values from the object and sends them to the other createHashKey() method
    	return createHashKey(animal.getName(), animal.getAcquisitionDate());
    }
}

