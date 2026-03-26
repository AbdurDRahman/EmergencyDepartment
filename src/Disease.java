
public class Disease {
    private String diseaseName;
    private float[] categoryChance;
    private final int index;

    public Disease() {
        index = 5;
        categoryChance = new float[index];

    }

    public void setDiseaseName(String name) {
        this.diseaseName = name;
    }

    public void setCategoryOneChance(float chance) {
        categoryChance[0] = chance;
    }

    public void setCategoryTwoChance(float chance) {
        categoryChance[1] = chance;
    }

    public void setCategoryThreeChance(float chance) {
        categoryChance[2] = chance;
    }

    public void setCategoryFourChance(float chance) {
        categoryChance[3] = chance;
    }

    public void setCategoryFiveChance(float chance) {
        categoryChance[4] = chance;
    }

    public String getName() {
        return diseaseName;
    }

    public float getCategoryOneChance() {
        return categoryChance[0];
    }

    public float getCategoryTwoChance() {
        return categoryChance[1];
    }

    public float getCategoryThreeChance() {
        return categoryChance[2];
    }

    public float getCategoryFourChance() {
        return categoryChance[3];
    }

    public float getCategoryFiveChance() {
        return categoryChance[4];
    }

    public void Display() {
        System.out.println("Name: " + diseaseName + " 1: " + categoryChance[0] + " 2: " + categoryChance[1] + " 3: " + categoryChance[2] + " 4: " + categoryChance[3] + " 5: " + categoryChance[4]);
    }
}
