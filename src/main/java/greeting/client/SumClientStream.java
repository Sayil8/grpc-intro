package greeting.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClientStream {
    public static void main(String[] args) {
        System.out.println("Hello I am a grpc client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        System.out.println("Creating stub");

        // create a service client
        SumServiceGrpc.SumServiceBlockingStub stub = SumServiceGrpc.newBlockingStub(channel);

        // streaming server
        int number = 567890;
        stub.primeDecomposition(PrimeDecompositionRequest.newBuilder()
                .setNumber(number)
                .build())
                .forEachRemaining(primeDecompositionResponse -> {
                    System.out.println(primeDecompositionResponse.getResult());
                });


        System.out.println("Shuting down channel");
        channel.shutdown();
    }
}
