package com.mycompany.project_mid;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class Person extends DataSupport implements Serializable {
    private int id;
    private byte[] imageId;
    private int wisdom;
    private int force;
    private int luck;
    private String name;
    private String birth;
    private String death;
    private String brief;
    private String sex;
    private String nation;
    private String nativePlace;
    private String sentence;

    Person(int id,byte[] imageId, String name, String birth, String death, String brief, String sex, String nation,String nativePlace,String sentence,int wisdom,int force,int luck)
    {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.birth = birth;
        this.death = death;
        this.brief = brief;
        this.sex = sex;
        this.nation = nation;
        this.nativePlace = nativePlace;
        this.sentence = sentence;
        this.wisdom = wisdom;
        this.force = force;
        this.luck = luck;
    }

    public Person(){

    }

    public int getId(){return id;
    }

    public  byte[] getImageId(){
        return imageId;
    }

    public  String getName(){
        return  name;
    }

    public String getBirth() {
        return birth;
    }

    public String getDeath() {
        return death;
    }

    public String getBrief() {
        return brief;
    }

    public String getSex() {
        return sex;
    }

    public String getNation() {
        return nation;
    }

    public String getNativePlace(){return nativePlace;}

    public String getSentence(){return sentence;}

    public int getForce(){return force;}

    public int getWisdom(){return wisdom;}

    public int getLuck(){return luck;}

    public void setId(int id){this.id = id;}

    public void setImageId(byte[] imageId){
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public void setNativePlace(String nativePlace){this.nativePlace=nativePlace;}

    public void setSentence(String sentence){this.sentence=sentence;}

    public void setWisdom(int wisdom){this.wisdom=wisdom;}

    public void setForce(int force){this.force=force;}

    public void setLuck(int luck){this.luck=luck;}

    public void setAll(int id,byte[] imageId, String name, String birth, String death, String brief,String sex,String nation,String nativePlace, String sentence, int wisdom,int force,int luck)
    {
        this.id = id;
        this.imageId = imageId;
        this.name = name;
        this.birth = birth;
        this.death = death;
        this.brief = brief;
        this.sex = sex;
        this.nation  = nation;
        this.nativePlace = nativePlace;
        this.sentence = sentence;
        this.wisdom = wisdom;
        this.force = force;
        this.luck = luck;
    }
}
