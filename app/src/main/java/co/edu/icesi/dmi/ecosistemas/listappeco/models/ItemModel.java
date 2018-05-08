package co.edu.icesi.dmi.ecosistemas.listappeco.models;

/**
 * Created by Jose Moncada AKA Monk on 26/04/2018.
 * for Application Ecosystems class at Universidad Icesi
 */

public class ItemModel {

    private String uid;
    private String name;
    private String description;
    private String lid;
    private boolean check;

    public ItemModel(){

    }

    public ItemModel(String name, String description, String uid, String lid){
        this.name = name;
        this.description = description;
        this.uid = uid;
        this.lid = lid;
        this.check = false;
    }
    

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
