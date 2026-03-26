import java.util.ArrayList;
import java.io.*;
public class NameList {
    ArrayList<String> names;
    ArrayList<Boolean> genders;
    public NameList() {
        names = new ArrayList<String>();
        genders = new ArrayList<Boolean>();
        Load();
    }

    public void Load(){
        String fileName = "Database/Person.csv" ;
        String line ;
        String delimiter = "," ;
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
            while((line = reader.readLine()) != null){
                String[] data = line.split(delimiter);
                String name = data[0];
                String gender = data[1];
                names.add(name);
                if(gender.equals("male")){
                    genders.add(true);
                }
                else{
                    genders.add(false);
                }
            }
        }
        catch(IOException e){
            System.out.println(e);
        }

        }


public int SizeOfList() {
    return names.size();
}
public String GetName(int index){
        return names.get(index);
}
public Boolean GetGender(int index){
        return genders.get(index);
}
    }