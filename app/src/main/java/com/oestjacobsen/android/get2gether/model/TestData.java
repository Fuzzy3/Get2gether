package com.oestjacobsen.android.get2gether.model;

import java.util.ArrayList;
import java.util.List;



public class TestData {

    public static List<User> getTestUsers(){
        List<User> testUsers = new ArrayList<>();

        User u01 = new User();
        u01.setUsername("larstheman");
        u01.setFullName("Lars Hansen");
        u01.setPassword("1234");
        testUsers.add(u01);

        User u02 = new User();
        u02.setUsername("hanzi");
        u02.setFullName("Hans Larsen");
        u02.setPassword("2222");
        testUsers.add(u02);

        User u03 = new User();
        u03.setUsername("princess_line");
        u03.setFullName("Line Balmer");
        u03.setPassword("9898");
        testUsers.add(u03);

        User u04 = new User();
        u04.setUsername("Admin");
        u04.setFullName("Søren Oest Jacobsen");
        u04.setPassword("1551");
        testUsers.add(u04);

        User u05 = new User();
        u05.setUsername("das_robot");
        u05.setFullName("Jeppe Jepsen");
        u05.setPassword("1111");
        testUsers.add(u05);

        return testUsers;
    }


    public static List<Group> getTestGroups() {
        List<Group> testGroups = new ArrayList<>();

        Group g01 = new Group();
        g01.setGroupTitle("The zoo");
        g01.setGroupDesc("Class trip to the zoo");
        testGroups.add(g01);

        return testGroups;
    }
}
