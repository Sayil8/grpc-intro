package greeting.client;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class SumClient {
    public static void main(String[] args) {
        System.out.println("Hello I am a grpc client");

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();

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


        System.out.println("Shuting down channel");
        channel.shutdown();
    }
}
