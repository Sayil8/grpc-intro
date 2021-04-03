package greeting.server;

import com.proto.sum.*;
import io.grpc.stub.StreamObserver;

public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

    @Override
    public void sum(SumRequest request, StreamObserver<SumResponse> responseObserver) {
        // extract fields
        Sum sum = request.getSum();
        int first = sum.getFirstOperator();
        int second = sum.getSecondOperator();

        // create response
        int response = first + second;
        SumResponse sumResponse = SumResponse.newBuilder()
                .setResult(response)
                .build();

        // send response
        responseObserver.onNext(sumResponse);

        //complate the call
        responseObserver.onCompleted();
    }

    @Override
    public void primeDecomposition(PrimeDecompositionRequest request, StreamObserver<PrimeDecompositionResponse> responseObserver) {
       int number = request.getNumber();
       int divisor = 2;

       while (number > 1) {
           if (number % divisor == 0) {
               number = number / divisor;
               responseObserver.onNext(PrimeDecompositionResponse.newBuilder()
                       .setResult(divisor)
                       .build());
           } else {
               divisor ++;
           }
       }

       responseObserver.onCompleted();
    }
}
