package src;

public class TestGettersSetters {
    public static void main(String[] args) {
        ElectricGuitar electricGuitar = new ElectricGuitar("Gibson");
        electricGuitar.setPickupsNum(5);
        electricGuitar.setRockStarUse(true);
        System.out.println(electricGuitar);
    }
}

class ElectricGuitar {
    public boolean rockStarUse;
    private String brand;
    private int pickupsNum;

    public ElectricGuitar(String brand) {
        this.brand = brand;
        this.rockStarUse = false;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPickupsNum(int pickupsNum) {
        this.pickupsNum = pickupsNum;
    }

    public void setRockStarUse(boolean rockStarUse) {
        this.rockStarUse = rockStarUse;
    }

    public int getPickupsNum() {
        return pickupsNum;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String toString() {
        return String.format("{rockStarUse: '%s', brand: '%s',  pickupsNum: '%s'}", rockStarUse, brand, pickupsNum);
    }
}
