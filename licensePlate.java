import java.util.ArrayList; //Can't decide your vanity license plate? Need to generate one quickly? Want to make it harder to find? Generate or matcherate your plate today!


public class licensePlate {
    public static void main(String[] args) throws Exception {
        //# = NUMERIC, $ = LETTER
        plate newPlate = new plate(0, 0, 0, null, 0, null, 0, null); //create new instance of the plate class
        
        
        //Generate Plate - # = number, $ = letter
        System.out.println(newPlate.generatePlate("#$$$###"));
        

        //Find Plate  
        ArrayList<Object> plateInfo = new ArrayList<>();

        String chosenPlate = newPlate.generatePlate("F8T3"); //register plate
        plateInfo.add(chosenPlate);
        System.out.println(chosenPlate);

        String calcCombo = newPlate.plateCombo(chosenPlate); //calculate the number of possible combinations to get a match of plate
        plateInfo.add(calcCombo);
        System.out.println(calcCombo);

        String crackPlate = newPlate.matchPlate(chosenPlate); //find the plate 
        System.out.println(crackPlate);
        plateInfo.add(newPlate.getNum(newPlate.getGuesses()));
        plateInfo.add(newPlate.getTime());
        
        System.out.println(plateInfo); //return the generated plate, number of combinations, number of guesses, and time to match the plate
    }
}


class plate{
    private int minVal;
    private int maxVal;
    private int rangeVal;
    private String licenseChar;
    private int licenseNum;
    private String plateFormat;
    private int totalGuess;
    private String loggedTime;
    
    //PARAMETERIZED CONSTRUCTOR
    public plate(int minimum, int maximum, int range, String character, int numeric, String format, int attempts, String time){
        minVal = minimum;
        maxVal = maximum;
        rangeVal = range;
        licenseChar = character;
        licenseNum = numeric;
        plateFormat = format;
        totalGuess = attempts;
        loggedTime = time;
    }

    //FORMAT NUMBERS
    public String getNum(int putNum){
        return String.format("%,d", putNum);
    }

    //SCIENTIFIC NOTATION CONVERSION
    public static String printScientificNotation(long number) {
        String scientificNotation = String.format("%e", (double) number);
        return scientificNotation;
    }

    //GENERATE NUMBER
    public int generateNumeric(){
        int randomVal = 0;
        minVal = 0;
        maxVal = 9;
        rangeVal = (maxVal-minVal)+1;
        for(int i = 0; i < maxVal; i++){
            randomVal = (int)(Math.random() * rangeVal) + minVal;
        }
        return randomVal;
    }
    
    //GENERATE LETTER
    public String generateCharacter(){
        String finalLetter = "";
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = letters.length();
        int letterRange = ((index-1)-0)+1;
        int indentifiedLetter = (int)(Math.random() * letterRange);
        for(int i = 0; i < index; i++){
            finalLetter = letters.substring(indentifiedLetter, indentifiedLetter+1); //SUBSTRING(0,1) FOR LETTER A
        }
        return finalLetter;
    }

    //GETTERS
    public int getMinimum(){
        return minVal;
    }
    public int getMaximum(){
        return maxVal;
    }
    public int getRange(){
        return rangeVal;
    }
    public String getCharacter(){
        return licenseChar;
    }
    public int getNumeric(){
        return licenseNum;
    }
    public String getFormat(){
        return plateFormat;
    }

    public int getGuesses(){
        return totalGuess;
    }

    public String getTime(){
        return loggedTime;
    }

    //TO-STRINGS
    public int returnNumerical(){
        return generateNumeric();
    }
    public String returnLetter(){
        return generateCharacter();
    }
    
    //GENERATE PLATE ACCORDING TO INPUT FORMAT
    public String generatePlate(String plateFormat){
        //# = numeric, $ = letter
        String finalPlate = "";
        for(int i = 0; i < plateFormat.length(); i++){
            if(plateFormat.substring(i, i+1).equals("#")){
                finalPlate += generateNumeric();
            }
            else if(plateFormat.substring(i,i+1).equals("$")){
                finalPlate += generateCharacter();
            }
            else {
                finalPlate += plateFormat.substring(i, i+1);
            }
        }
        return finalPlate;
    }
    
    //MATCHERATE PLATE ACCORDING TO INPUT FORMAT
    public String matchPlate(String matchPlate) {
        //Array to store generated guesses
        ArrayList<String> generatedGuesses = new ArrayList<>();
        //Timer system
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        loggedTime = "";
        //Detect format
        plateFormat = "";
        for (int i = 0; i < matchPlate.length(); i++) {
            if(Character.isDigit(matchPlate.charAt(i))){
                plateFormat += "#";
            }
            else if(Character.isLetter(matchPlate.charAt(i))){
                plateFormat += "$";
            }
            else{
                plateFormat += matchPlate.substring(i, i + 1);
            }
        }
        // Match plate
        String checkPlate = generatePlate(plateFormat);
        int matchAttempts = 0;
        while(!checkPlate.equals(matchPlate)){
            //Calculate elapsed time
            long currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - startTime;
            System.out.println(checkPlate);
            generatedGuesses.add(checkPlate); // Add the generated guess to the array
            checkPlate = generatePlate(plateFormat);
            //Check if the generated guess has already been generated
            while(generatedGuesses.contains(checkPlate)) {
                checkPlate = generatePlate(plateFormat);
            }
            matchAttempts++;
        }
        totalGuess = matchAttempts;
        // Convert elapsed time to hours, minutes, seconds, and milliseconds
        long hours = elapsedTime / (60 * 60 * 1000);
        long minutes = (elapsedTime % (60 * 60 * 1000)) / (60 * 1000);
        long seconds = (elapsedTime % (60 * 1000)) / 1000;
        long milliseconds = elapsedTime % 1000;
        loggedTime += hours + "h " + minutes + "m " + seconds + "s " + milliseconds + "ms";
        return checkPlate + "\n" + getNum(matchAttempts) + " matches found within " + hours + "h " + minutes + "m " + seconds + "s " + milliseconds + "ms.";
    }


    //GET NUMBER OF COMBINATIONS TO FIND PLATE
    public String plateCombo(String calculatePlate) {
        int numAmt = 10; // total of 10 integers
        int charAmt = 26; // total of 26 letters
        int numInt = 0;
        int numChar = 0;
        for (int i = 0; i < calculatePlate.length(); i++) {
            if (Character.isDigit(calculatePlate.charAt(i))) {
                numInt++;
            }
            else if(Character.isLetter(calculatePlate.charAt(i))) {
                numChar++;
            }
        }
        long combinations = (long) Math.pow(numAmt, numInt) * (long) Math.pow(charAmt, numChar);
        if(combinations <= Integer.MIN_VALUE || combinations >= Integer.MAX_VALUE) {
            String sciComb = printScientificNotation(combinations);
            return "Combinations: " + sciComb;
        }
        else{
            String possibleCombo = getNum((int) combinations);
            return "Combinations: " + possibleCombo;
        }
    }
}
