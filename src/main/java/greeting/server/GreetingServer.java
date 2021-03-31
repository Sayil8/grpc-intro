package greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Hello grpc");
        Server server = ServerBuilder.forPort(50051)
                .addService(new SumServiceImpl())
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Recived shutdown request");
            server.shutdown();
            System.out.println("Sucessfully stopped the server");
        }));

        server.awaitTermination();
    }
}
