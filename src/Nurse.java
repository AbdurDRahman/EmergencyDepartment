public class Nurse extends Staff {
    public Nurse(String name, boolean gender) {
        super(name, gender);
        capacity = 2 ;
    }
    public Nurse(){
        super();
        capacity = 2 ;
    }
}
