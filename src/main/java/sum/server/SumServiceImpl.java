package sum.server;

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

    @Override
    public StreamObserver<ComputeAverageRequest> computeAverage(StreamObserver<ComputeAverageResponse> responseObserver) {
        return new StreamObserver<ComputeAverageRequest>() {
            Double result = 0.0;
            int count = 0;

            @Override
            public void onNext(ComputeAverageRequest value) {
                result += value.getNumber();
                count ++;
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(
                        ComputeAverageResponse.newBuilder()
                                .setN(result/count)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };
    }
}
