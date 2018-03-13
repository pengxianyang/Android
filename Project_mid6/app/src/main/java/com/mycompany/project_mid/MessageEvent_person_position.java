package com.mycompany.project_mid;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class MessageEvent_person_position{
    private int personId;
    private int position;


    MessageEvent_person_position(int personId, int position) {
        this.personId = personId;
        this.position = position;
    }

    int getPersonId(){
        return personId;
    }

    int getPosition(){
        return  position;
    }

}
