package com.lti.idss.poc.grpc.user.service;

import com.lti.idss.poc.grpc.AllUserResponse;
import com.lti.idss.poc.grpc.UserRequest;
import com.lti.idss.poc.grpc.UserResponse;
import com.lti.idss.poc.grpc.user.UserDatabase;
import io.grpc.stub.StreamObserver;

import java.util.HashMap;

public class UserStreamObserver implements StreamObserver<UserRequest> {

    private StreamObserver<AllUserResponse> responseObserver;
    private UserResponse.Builder builder = UserResponse.newBuilder();
    private AllUserResponse.Builder allUserBuilder = AllUserResponse.newBuilder();

    public UserStreamObserver(StreamObserver<AllUserResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(UserRequest userRequest) {
        final int userId = userRequest.getUserId();
        final HashMap<String, String> data = UserDatabase.getData(userId);
        builder.setUserId(userId);
        builder.setName(data.get("name"));
        builder.setStatus(data.get("status"));
        builder.setCountry(data.get("country"));
        allUserBuilder.addUser(builder.build());
    }

    @Override
    public void onError(Throwable throwable) {
        responseObserver.onCompleted();
    }

    @Override
    public void onCompleted() {
        responseObserver.onNext(allUserBuilder.build());
        responseObserver.onCompleted();
    }
}
