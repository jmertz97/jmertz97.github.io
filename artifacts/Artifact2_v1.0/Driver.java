// Joshua Mertz
// Project 2
// 4/20/2025

// package compsci; // package used on my local environment

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Driver {
    private static ArrayList<Dog> dogList = new ArrayList<Dog>();
    private static ArrayList<Monkey> monkeyList = new ArrayList<Monkey>();

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        Character input;
        
        initializeDogList();
        initializeMonkeyList();
        
        // Add a loop that displays the menu, accepts the users input and takes the appropriate action.
        // For the project submission you must also include input validation and appropriate feedback to the user.
        // Hint: create a Scanner and pass it to the necessary methods 
        // Hint: Menu options 4, 5, and 6 should all connect to the printAnimals() method.
        do {
            displayMenu();
            // only uses the first character of the input - e.g. 11 will invoke case '1', "quick" will invoke 'q' and end the loop
            input = scnr.nextLine().charAt(0); 
            switch (input) {  // switch statement to handle user input
                case '1':
                    intakeNewDog(scnr);
                    break;
                case '2':
                    intakeNewMonkey(scnr);
                    break;
                case '3':
                    reserveAnimal(scnr);
                    break;
                case '4':
                case '5':
                case '6':
                    printAnimals(input);
                    break;
            }
        } while (!input.equals('q')); 
    }


    // This method prints the menu options
    public static void displayMenu() {
        System.out.println("\n\n");
        System.out.println("\t\t\t\tRescue Animal System Menu");
        System.out.println("[1] Intake a new dog");
        System.out.println("[2] Intake a new monkey");
        System.out.println("[3] Reserve an animal");
        System.out.println("[4] Print a list of all dogs");
        System.out.println("[5] Print a list of all monkeys");
        System.out.println("[6] Print a list of all animals that are not reserved");
        System.out.println("[q] Quit application");
        System.out.println();
        System.out.println("Enter a menu selection");
    }


    // Adds dogs to a list for testing
    public static void initializeDogList() {
        Dog dog1 = new Dog("Spot", "German Shepherd", "male", "1", "25.6", "05-12-2019", "United States", "intake", false, "United States");
        Dog dog2 = new Dog("Rex", "Great Dane", "male", "3", "35.2", "02-03-2020", "United States", "Phase I", false, "United States");
        Dog dog3 = new Dog("Bella", "Chihuahua", "female", "4", "25.6", "12-12-2019", "Canada", "in service", true, "Canada");

        dogList.add(dog1);
        dogList.add(dog2);
        dogList.add(dog3);
    }


    // Adds monkeys to a list for testing
        // Optional for testing
    public static void initializeMonkeyList() {
        Monkey monkey1 = new Monkey("Ham", "male", "1", "25.6", "9.2", "9'9\"", "9'9\"", "Capuchin", "05-12-2019", "United States", "intake", false, "United States");
        Monkey monkey2 = new Monkey("Enos", "male", "3", "35.2", "5.5", "5'11\"", "5'11\"", "Marmoset", "02-03-2020", "United States", "Phase I", false, "United States");
        Monkey monkey3 = new Monkey("Martine", "male", "4", "25.6", "6.1", "6'0\"", "6'0\"", "Macaque", "12-12-2019", "France", "in service", true, "Canada");
        Monkey monkey4 = new Monkey("Juan", "male", "3", "35.2", "5.5", "5'11\"", "5'11\"", "Capuchin", "04-20-2025", "Argentina", "in service", false, "Argentina");

        monkeyList.add(monkey1);
        monkeyList.add(monkey2);
        monkeyList.add(monkey3);
        monkeyList.add(monkey4);

    }


    // Complete the intakeNewDog method
        // The input validation to check that the dog is not already in the list is done for you
    public static void intakeNewDog(Scanner scanner) {
        boolean reserved;  // initialize separate boolean val for reserved status
        Dog newDog;  // initialize new dog var
        System.out.println("What is the dog's name?");
        String name = scanner.nextLine();
        for(Dog dog: dogList) {
            if(dog.getName().equalsIgnoreCase(name)) {
                System.out.println("\n\nThis dog is already in our system\n\n");
                return; //returns to menu
            }
        }
        System.out.println("What is the dog's breed");
        String breed = scanner.nextLine();
        System.out.println("What is the dog's gender?");
        String gender = scanner.nextLine();
        System.out.println("What is the dog's age?");
        String age = scanner.nextLine();
        System.out.println("What is the dog's weight");
        String weight = scanner.nextLine();
        System.out.println("When was the dog acquired?");
        String acquisitionDate = scanner.nextLine();
        System.out.println("In which country was the dog acquired?");
        String acquisitionCountry = scanner.nextLine();
        System.out.println("What is the dog's training status?");
        String trainingStatus = scanner.nextLine();
        System.out.println("Is the dog reserved? (yes/no)");
        // prompt asks for yes or no, but in practice will default to 'no' -> false unless "yes" is provided
        reserved = "yes".equalsIgnoreCase(scanner.nextLine());
        System.out.println("What is the dog's current country of service?");
        String inServiceCountry = scanner.nextLine();   
        
        newDog = new Dog(name, breed, gender, age, weight, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry);
        dogList.add(newDog);
        return;
    }

    
    // Complete intakeNewMonkey
        // Instantiate and add the new monkey to the appropriate list
        // For the project submission you must also validate the input to make sure the monkey doesn't already exist and the species type is allowed
    public static void intakeNewMonkey(Scanner scanner) {
        boolean reserved;  // initialize separate boolean val for reserved status
        Monkey newMonkey;  // initialize new monkey var
        System.out.println("What is the monkey's name?");
        // get monkey name, check that it doesnt already exist
        String name = scanner.nextLine();
        for(Monkey monkey: monkeyList) {
            if(monkey.getName().equalsIgnoreCase(name)) {
                System.out.println("\n\nThis monkey is already in our system\n\n");
                return; //returns to menu
            }
        }
        // prompt user for monkey species
        System.out.println("What is the monkey's species?");
        String species = scanner.nextLine();
        // ensure provided species is in the list
        List<String> validSpecies = List.of("Capuchin", "Guenon", "Macaque", "Marmoset", "Squirrel monkey", "Tamarin");
        if (!validSpecies.contains(species)) {
            System.out.println("\n\nInvalid species provided.\n\n");
            return;
        }
        // now get the rest of the data - initialize and populate each variable in succession
        System.out.println("What is the monkey's gender?");
        String gender = scanner.nextLine();
        System.out.println("What is the monkey's age?");
        String age = scanner.nextLine();
        System.out.println("What is the monkey's weight?");
        String weight = scanner.nextLine();
        System.out.println("What is the monkey's height?");
        String height = scanner.nextLine();
        System.out.println("What is the monkey's body length?");
        String bodyLength = scanner.nextLine();
        System.out.println("What is the monkey's tail length?");
        String tailLength = scanner.nextLine();
        System.out.println("When was the monkey acquired?");
        String acquisitionDate = scanner.nextLine();
        System.out.println("In which country was the monkey acquired?");
        String acquisitionCountry = scanner.nextLine();
        System.out.println("What is the monkey's training status?");
        String trainingStatus = scanner.nextLine();
        System.out.println("Is the monkey reserved? (yes/no)");
        // prompt asks for yes or no, but in practice will default to 'no' -> false unless "yes" is provided
        reserved = "yes".equalsIgnoreCase(scanner.nextLine());
        System.out.println("What is the monkey's current country of service?");
        String inServiceCountry = scanner.nextLine();   
        
        newMonkey = new Monkey(name, gender, age, weight, tailLength, height, bodyLength, species, acquisitionDate, acquisitionCountry, trainingStatus, reserved, inServiceCountry);
        monkeyList.add(newMonkey);
        return;
    }


    // Complete reserveAnimal
        // You will need to find the animal by animal type and in service country
    public static void reserveAnimal(Scanner scanner) {
        String animalType, country;
        System.out.println("Enter the desired animal type: ");
        animalType = scanner.nextLine();
        System.out.println("Enter the animal's country: ");
        country = scanner.nextLine();
        
        switch (animalType.toLowerCase()) {
            case "dog":
                for (Dog dog : dogList) {  // iterate over the list of dogs to find first match
                    if (!dog.getReserved() && country.equalsIgnoreCase(dog.getInServiceLocation())) {
                        System.out.println("Reserving " + dog.getName() + " in " + dog.getInServiceLocation() + ".");
                        dog.setReserved(true);
                        return;  // return out of the function
                    }
                }
                // if no matches, notify the user and return
                System.out.println("No available dogs in " + country + ".");
                return;
            case "monkey":
                for (Monkey monkey : monkeyList) {  // iterate over the list of dogs to find first match
                    if (!monkey.getReserved() && country.equalsIgnoreCase(monkey.getInServiceLocation())) {
                        System.out.println("Reserving " + monkey.getName() + " in " + monkey.getInServiceLocation() + ".");
                        monkey.setReserved(true);
                        return;  // return out of the function
                    }
                }
                // if no matches, notify the user and return
                System.out.println("No available monkeys in " + country + ".");
                return;
            default:  // if the user typed an invalid animal type, notify user and return
                System.out.println("Invalid animal type specified.");
                return;
        }
    }

    // Complete printAnimals
        // Include the animal name, status, acquisition country and if the animal is reserved.
        // Remember that this method connects to three different menu items.
        // The printAnimals() method has three different outputs based on the listType parameter
            // dog - prints the list of dogs
            // monkey - prints the list of monkeys
            // available - prints a combined list of all animals that are
            // fully trained ("in service") but not reserved 
    // Remember that you only have to fully implement ONE of these lists. 
    // The other lists can have a print statement saying "This option needs to be implemented".
    // To score "exemplary" you must correctly implement the "available" list.
    public static void printAnimals(Character selection) {
        switch (selection) {
            case '4':
            case '5':
                System.out.println("This option needs to be implemented");
                break;
            case '6':
                boolean foundAnimal = false;  // set to true when the first animal is found to be listed
                for (Dog dog : dogList) {
                    if ("in service".equals(dog.getTrainingStatus()) && !dog.getReserved()) {
                        if (!foundAnimal) {
                            System.out.println("Trained & available animals:");
                            // constructed this way to avoid printing this line if no available animals are found
                            foundAnimal = true;
                        }
                        // print the animal's name and country
                        System.out.print("\t" + dog.getName() + " (country: ");
                        System.out.println(dog.getInServiceLocation() + ")");
                    }
                }
                for (Monkey monkey : monkeyList) {
                    if (!foundAnimal) {
                        System.out.println("Trained & available animals:");
                        foundAnimal = true;
                    }
                    if ("in service".equals(monkey.getTrainingStatus()) && !monkey.getReserved()) {
                        System.out.print("\t" + monkey.getName() + " (country: "); 
                        System.out.println(monkey.getInServiceLocation() + ")");
                    }
                }
                if (!foundAnimal) {
                    // allows for separate print statement if there are no available animals
                    System.out.println("No trained animals are currently available.");
                }
                break;
        }
    }
}

