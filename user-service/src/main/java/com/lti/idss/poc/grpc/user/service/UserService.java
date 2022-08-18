package com.lti.idss.poc.grpc.user.service;

import com.lti.idss.poc.grpc.*;
import com.lti.idss.poc.grpc.user.UserDatabase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

@GrpcService
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void getUserDetails(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        final UserResponse.Builder builder = UserResponse.newBuilder();
        HashMap<String, String> userData = UserDatabase.getData(request.getUserId());

        builder.setName(userData.get("name"));
        builder.setCountry(userData.get("country"));
        builder.setStatus(userData.get("status"));

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUserDetailsViaUnary(AllUserRequest request, StreamObserver<AllUserResponse> responseObserver) {
        HashMap<Integer, HashMap<String, String>> userData = UserDatabase.getAllData();
        ArrayList<UserResponse> responseList = new ArrayList<>();

        userData.forEach((userId, user) -> {
            final UserResponse.Builder builder = UserResponse.newBuilder();

            builder.setName(user.get("name"));
            builder.setCountry(user.get("country"));
            builder.setStatus(user.get("status"));

            System.out.println("Unary Call: Just got user with name: " + user.get("name") + " at " + LocalTime.now());

            try {
                sleep(2000); // Added sleep to simulate time consuming operation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            responseList.add(builder.build());
        });

        final AllUserResponse.Builder builder = AllUserResponse.newBuilder();
        builder.addAllUser(responseList);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUserDetailsViaStream(AllUserRequest request, StreamObserver<UserResponse> responseObserver) {
        HashMap<Integer, HashMap<String, String>> userData = UserDatabase.getAllData();

        userData.forEach((userId, user) -> {
            final UserResponse.Builder builder = UserResponse.newBuilder();

            builder.setName(user.get("name"));
            builder.setCountry(user.get("country"));
            builder.setStatus(user.get("status"));

            responseObserver.onNext(builder.build());

            System.out.println("Stream Server: Just got user with name: " + user.get("name") + " at " + LocalTime.now());

            try {
                sleep(2000); // Added Thread.sleep to simulate time consuming operation
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<UserRequest> logUserDetailsViaClientStream(StreamObserver<AllUserResponse> responseObserver) {
        return new UserStreamObserver(responseObserver);
    }
}
