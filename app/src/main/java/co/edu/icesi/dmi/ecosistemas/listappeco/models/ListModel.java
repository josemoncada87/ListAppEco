package co.edu.icesi.dmi.ecosistemas.listappeco.models;

/**
 * Created by Jose Moncada AKA Monk on 26/04/2018.
 * for Application Ecosystems class at Universidad Icesi
 */

public class ListModel {

    private String lid;
    private String name;
    private int count;

    public ListModel() {

    }

    public ListModel(String name, String lid) {
        this.name = name;
        this.count = 0;
        this.lid = lid;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
