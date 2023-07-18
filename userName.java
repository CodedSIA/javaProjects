import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class userName {
    public static void main(String[] args) throws Exception {
        /* r = random word, u = name, n = noun, a = adjective
           if you wish to make the generated word (except name) uppercase, just type the capital letter
        */
        userConfig newName = new userConfig(0,0,false);
        newName.generateUserName("u_R", "sussy", 75, false);
        newName.printGeneratedResults();
    }
}


class userConfig {
    private ArrayList<String> nounArr;
    private ArrayList<String> adjArr;
    private ArrayList<String> userNames;
    private ArrayList<String> wordsArr;
    private ArrayList<String> moderatedWords;
    private int generateAmount;
    private int fileAllocation;
    private boolean moderateWord;

    public userConfig(int generateAmount, int fileAllocation, boolean moderateWord){
        this.generateAmount = generateAmount;
        this.fileAllocation = fileAllocation;
        this.moderateWord = moderateWord;
        nounArr = new ArrayList<>();
        adjArr = new ArrayList<>();
        userNames = new ArrayList<>();
        wordsArr = new ArrayList<>();
        moderatedWords = new ArrayList<>();
    }

    //FORMAT NUMBERS
    public String getNum(int putNum){
        return String.format("%,d", putNum);
    }

    //APPEND FROM FILE AND FILL IN ARRAYLISTS
    public void fillNounArrFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/nounFile.txt"))) {
            String noun;
            while ((noun = br.readLine()) != null) {
                nounArr.add(noun);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillAdjArrFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/adjFile.txt"))) {
            String adjective;
            while ((adjective = br.readLine()) != null) {
                adjArr.add(adjective);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillWordArrFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/infFile.txt"))) {
            String someWord;
            while ((someWord = br.readLine()) != null) {
                wordsArr.add(someWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //APPEND FROM MODERATION FILES
    public void modWords() {
        String blacklistedWord;
        //get moddedwords default
        try (BufferedReader br = new BufferedReader(new FileReader("src/mod_packager/moderFile.txt"))) {
            while ((blacklistedWord = br.readLine()) != null) {
                moderatedWords.add(blacklistedWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get moddedwords set by user
        try (BufferedReader br = new BufferedReader(new FileReader("src/mod_packager/userMod.txt"))) {
            while ((blacklistedWord = br.readLine()) != null) {
                moderatedWords.add(blacklistedWord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        blacklistedWord = null;
    }


    //GENERATE USERNAME
    public ArrayList<String> generateUserName(String wordFormat, String userName, int amountCombo, boolean doModerate) throws Exception {
        moderateWord = doModerate;
        generateAmount = amountCombo;
        fillAdjArrFromFile();
        fillNounArrFromFile();
        fillWordArrFromFile();
        if(doModerate){modWords();}

        int adjRangeVal = adjArr.size();
        int nounRangeVal = nounArr.size();
        int wordRangeVal = wordsArr.size();

        for (int i = 0; i < generateAmount; i++) {
            String generatedUsername = "";
            String setAdj;
            String setNoun;
            String setWord;
            for (int j = 0; j < wordFormat.length(); j++) {
                if (wordFormat.substring(j, j + 1).equalsIgnoreCase("r")) {
                    int pickWord = (int) (Math.random() * wordRangeVal);
                    setWord = wordsArr.get(pickWord);
                    if (wordFormat.substring(j, j + 1).equals("R")) {
                        setWord = setWord.substring(0, 1).toUpperCase() + setWord.substring(1);
                    }
                    generatedUsername += setWord;
                }
                else if (wordFormat.substring(j, j + 1).equalsIgnoreCase("u") && !userName.equalsIgnoreCase("null")) {
                    generatedUsername += userName;
                } 
                else if (wordFormat.substring(j, j + 1).equalsIgnoreCase("n")) {
                    int pickNoun = (int) (Math.random() * nounRangeVal);
                    setNoun = nounArr.get(pickNoun);
                    if (wordFormat.substring(j, j + 1).equals("N")) {
                        setNoun = setNoun.substring(0, 1).toUpperCase() + setNoun.substring(1);
                    }
                    generatedUsername += setNoun;
                } 
                else if (wordFormat.substring(j, j + 1).equalsIgnoreCase("a")) {
                    int pickAdjective = (int) (Math.random() * adjRangeVal);
                    setAdj = adjArr.get(pickAdjective);
                    if (wordFormat.substring(j, j + 1).equals("A")) {
                        setAdj = setAdj.substring(0, 1).toUpperCase() + setAdj.substring(1);
                    }
                    generatedUsername += setAdj;
                }
                else{
                    generatedUsername += wordFormat.substring(j, j + 1);
                }
            }
            userNames.add(generatedUsername);
            System.out.println(i + 1 + ". " + generatedUsername);
        }
        return userNames;
    }


    //GET TIME
    public String printTime() {
        //Get the current date and time in the system's default timezone
        LocalDateTime currentTime = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime currentZonedDateTime = currentTime.atZone(zone);
        //Create a formatter to specify the desired output format
        DateTimeFormatter zoneDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter zoneTime = DateTimeFormatter.ofPattern("HH:mm:ss z");
        //Format
        String moddedTime = currentZonedDateTime.format(zoneDate) + " at " + currentZonedDateTime.format(zoneTime);
        return moddedTime;
    }
    
    //GENERATED NEXT FILE BASED ON PREVIOUS NUMBER
    private String getNextFileName(int fileNumber) {
        String baseFileName = "src/generated_results/generatedResult";
        String fileExtension = ".txt";
        String fileName = baseFileName + fileNumber + fileExtension;
        File file = new File(fileName);
        while (file.exists()) {
            fileNumber++;
            fileName = baseFileName + fileNumber + fileExtension;
            file = new File(fileName);
        }
        fileAllocation = fileNumber;
        return fileName;
    }

    //GENERATE RESULTS
    public String printGeneratedResults() throws Exception {
        int fileNumber = fileAllocation+1;
        String fileDirectory = getNextFileName(fileNumber);
        FileWriter writer = new FileWriter(fileDirectory);
        String generatedFileName = new File(fileDirectory).getAbsolutePath();
        writer.write("GENERATED RESULTS #" + fileAllocation + " - Generated Results for " + getNum(userNames.size()) + " requested entries." + "\n\n");
        for (int i = 0; i < userNames.size(); i++) {
            writer.write(i+1 + ". " + userNames.get(i) + "\n");
        }
        writer.write("\nTotal results: " + userNames.size() + "\nFile written to " + generatedFileName + "\nGenerated at " + printTime() +"\nModeration enabled: " + moderateWord);
        writer.close();
        System.out.println("Result successfully generated to " + generatedFileName + " @ " + printTime());
        return null;
    }
}
