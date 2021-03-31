package greeting.server;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;
import com.proto.greet.Greeting;
import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceGrpc.GreetServiceImplBase {

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {

        //extract fields
        Greeting greeting = request.getGreeting();
        String firstName = greeting.getFirstName();
        String lastName = greeting.getLastName();

        // create response
        String result = "Hello " + firstName + " " + lastName;
        GreetResponse response = GreetResponse.newBuilder().
                setResult(result)
                .build();

        //send response
        responseObserver.onNext(response);

        //complate the rpc call
        responseObserver.onCompleted();

    }
}
