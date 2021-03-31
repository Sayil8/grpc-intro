package greeting.server;

import com.proto.sum.Sum;
import com.proto.sum.SumRequest;
import com.proto.sum.SumResponse;
import com.proto.sum.SumServiceGrpc;
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
}
