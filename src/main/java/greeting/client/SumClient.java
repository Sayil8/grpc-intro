package greeting.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {

    public static void main(String[] args) {
        System.out.println("Hello I am a grpc client");
        SumClient client = new SumClient();

        client.run();

    }

    private void run() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

        doUnaryCall(channel);
        doServerStreamingCall(channel);


        System.out.println("Shuting down channel");
        channel.shutdown();
    }

    private void doServerStreamingCall(ManagedChannel channel) {
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
    }

    private void doUnaryCall(ManagedChannel channel) {
        System.out.println("Creating stub");

        // create a service client
        SumServiceGrpc.SumServiceBlockingStub sumClient = SumServiceGrpc.newBlockingStub(channel);
        Sum sum = Sum.newBuilder()
                .setFirstOperator(1)
                .setSecondOperator(3)
                .build();

        //create request
        SumRequest sumRequest = SumRequest.newBuilder()
                .setSum(sum)
                .build();

        // call the RPC and get back a GreetResponse
        SumResponse sumResponse = sumClient.sum(sumRequest);

        System.out.println(sumResponse.getResult());
    }
}
