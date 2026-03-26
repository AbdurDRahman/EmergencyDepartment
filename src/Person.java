
import java.util.Random;

public class Person {
    static int PersonIdCounter = 0;

    private String name;
    private boolean gender;
    private  int personId;
    NameList nameList;

    public Person(String name, boolean gender) {
        this.name = name;
        this.gender = gender;
        this.personId = PersonIdCounter;
        nameList = new NameList();
        PersonIdCounter++;
    }

    public Person(){
        name = "";
        gender = false;
        personId = PersonIdCounter;
        nameList = new NameList();
        PersonIdCounter++;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPersonId() {
        return personId;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public int getPersonIdCounter(){
        return PersonIdCounter;
    }
    public void AssignNameGender(){
        Random rand = new Random();
        int limit = nameList.SizeOfList()-1;
        int index = rand.nextInt(limit);
        this.setGender(nameList.GetGender(index));
        this.setName(nameList.GetName(index));
    }
    public void setPersonId(int personId) {
        this.personId = personId;
       if(PersonIdCounter <= personId) PersonIdCounter = personId + 1;
    }
}
