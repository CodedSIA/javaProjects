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
        userConfig newName = new userConfig(0,0);
        newName.generateUserName(15000);
        newName.printGeneratedResults();
    }
}


class userConfig {
    private ArrayList<String> nounArr;
    private ArrayList<String> adjArr;
    private ArrayList<String> userNames;
    private int generateAmount;
    private int fileAllocation;

    public userConfig(int generateAmount, int fileAllocation) {
        this.generateAmount = generateAmount;
        this.fileAllocation = fileAllocation;
        nounArr = new ArrayList<>();
        adjArr = new ArrayList<>();
        userNames = new ArrayList<>();
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

    //GENERATE USERNAME <---FIX HERE
    public ArrayList<String> generateUserName(int amountCombo) throws Exception {
        generateAmount = amountCombo;
        fillAdjArrFromFile();
        fillNounArrFromFile();
        String firstChar = "";
        String lastChar = "";
        String generatedUsername = "";
        int pickAdjective = 0;
        int pickNoun = 0;
        int adjRangeVal = adjArr.size();
        int nounRangeVal = nounArr.size();
        for(int i = 0; i < generateAmount; i++){
            pickAdjective = (int) (Math.random() * adjRangeVal);
            pickNoun = (int) (Math.random() * nounRangeVal);
            firstChar = adjArr.get(pickAdjective);
            lastChar = nounArr.get(pickNoun);
            generatedUsername = firstChar + lastChar.substring(0, 1).toUpperCase() + lastChar.substring(1);
            userNames.add(generatedUsername);
            System.out.println(i+1 + ". " + generatedUsername);
        }
        return userNames;
    }
    
    //GET TIME
    public String printTime(){
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
        writer.write("\nTotal results: " + userNames.size() + "\nFile written to " + generatedFileName + "\nGenerated at " + printTime());
        writer.close();
        System.out.println("Result successfully generated to " + generatedFileName + " @ " + printTime());
        return null;
    }
}
