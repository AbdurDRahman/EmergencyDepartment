import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class DiseaseList {
    private ArrayList<Disease> list;
    public DiseaseList(){
        list = new ArrayList<Disease>();
        this.GenerateList();
    }
    public void GenerateList(){
        String file = "Database/Disease.csv";
        String line ;
        String delimiter = ",";
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            while((line = reader.readLine()) != null){
                Disease d = new Disease();
                String[] data = line.split(delimiter);
                d.setDiseaseName(data[0]);
                float F = Float.parseFloat(data[1]);
                d.setCategoryOneChance(F);
                F = Float.parseFloat(data[2]);
                d.setCategoryTwoChance(F);
                F = Float.parseFloat(data[3]);
                d.setCategoryThreeChance(F);
                F = Float.parseFloat(data[4]);
                d.setCategoryFourChance(F);
                F = Float.parseFloat(data[5]);
                d.setCategoryFiveChance(F);
                list.add(d);
            }
        }
        catch(IOException e){
            System.out.println("Error reading file ," + e.getMessage());
        }
    }
    public int getListSize(){
        return list.size();
    }
    public Disease getIndexValue(int index){
        return list.get(index);
    }
    public Disease getRandomDisease(){
        Random rand = new Random();
        int index = rand.nextInt(list.size()-1);
        return list.get(index);
    }

    public static void main(String[] args) {
        DiseaseList x = new DiseaseList();
        x.GenerateList();
        int len = x.getListSize();
        for(int i = 0 ; i < len ; i++ ){
           Disease d =  x.getIndexValue(i);
           d.Display();
        }
    }
}

