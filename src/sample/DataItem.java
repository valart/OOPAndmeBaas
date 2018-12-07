package sample;

public class DataItem {

    private String firma;
    private String model;
    private String suurus;
    private String hind;

    public DataItem(String firma, String model, String suurus, String hind) {
        this.firma = firma;
        this.model = model;
        this.suurus = suurus;
        this.hind = hind;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSuurus() {
        return suurus;
    }

    public void setSuurus(String suurus) {
        this.suurus = suurus;
    }

    public String getHind() {
        return hind;
    }

    public void setHind(String hind) {
        this.hind = hind;
    }
}
