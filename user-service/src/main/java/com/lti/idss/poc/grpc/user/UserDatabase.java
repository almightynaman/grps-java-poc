package com.lti.idss.poc.grpc.user;

import com.github.javafaker.Faker;
import com.google.common.base.Enums;

import java.util.HashMap;
import java.util.Random;

public class UserDatabase {

    private final static HashMap<Integer, HashMap<String, String>> userDetails = generateUsers();

    private enum Status {
        active,
        inactive,
        suspend
    }

    private static HashMap<Integer, HashMap<String, String>> generateUsers() {
        HashMap<Integer, HashMap<String, String>> map = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            map.put(i, getUserData());
        }

        return map;
    }

    private static HashMap<String, String> getUserData() {
        final Faker faker = new Faker();

        HashMap<String, String> map = new HashMap<>();
        map.put("name", faker.name().fullName());
        map.put("country", faker.country().name());
        map.put("status", randomStatus().name());

        return map;
    }

    private static Status randomStatus() {
        int pick = new Random().nextInt(Status.values().length);

        return Status.values()[pick];
    }

    public static HashMap<String, String> getData(Integer userId) {
        return userDetails.get(userId);
    }

    public static HashMap<Integer, HashMap<String, String>> getAllData() {
        return userDetails;
    }

    public static void updateStatus(Integer userId, String status) {
        if (!isValidStatus(status)) {
            return;
        }

        final HashMap<String, String> data = getData(userId);

        if (canUpdate(data.get("status"), status)) {
            data.put("status", status);
        }
    }

    // Only allow:
    // active -> inactive
    // inactive -> active or suspend
    // suspend -> inactive
    private static boolean canUpdate(String fromStatus, String toStatus) {
        return (
                (fromStatus.equals(Status.active.name()) && toStatus.equals(Status.inactive.name()))) ||
                (fromStatus.equals(Status.inactive.name()) && toStatus.equals(Status.suspend.name())) ||
                (fromStatus.equals(Status.inactive.name()) && toStatus.equals(Status.active.name())) ||
                (fromStatus.equals(Status.suspend.name()) && toStatus.equals(Status.inactive.name())
                );
    }

    private static boolean isValidStatus(String status) {
        return Enums.getIfPresent(Status.class, status).isPresent();
    }
}
