package sum.client;

import com.proto.sum.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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

//        doUnaryCall(channel);
//        doServerStreamingCall(channel);
        doClientStreamingCall(channel);

        System.out.println("Shuting down channel");
        channel.shutdown();
    }

    private  void doClientStreamingCall(ManagedChannel channel) {
        System.out.println("Creating stub");

        // create a service client
        SumServiceGrpc.SumServiceStub asyncClient = SumServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ComputeAverageRequest> requestObserver =
                asyncClient.computeAverage(new StreamObserver<ComputeAverageResponse>() {
                    @Override
                    public void onNext(ComputeAverageResponse value) {
                        System.out.println("Recived response from a server");
                        System.out.println(value.getN());
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("Server has compleated sending us something");
                        latch.countDown();
                    }
                });


        // send messages
        System.out.println("Sending messages");
        for (int i = 0; i < 10000; i++) {
            requestObserver.onNext(ComputeAverageRequest.newBuilder()
                    .setNumber(i)
                    .build());
        }

        requestObserver.onCompleted();
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
