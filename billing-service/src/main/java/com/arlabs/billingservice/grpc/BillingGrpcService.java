package com.arlabs.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import com.arlabs.billingservice.model.BillingAccount;
import com.arlabs.billingservice.repository.BillingAccountRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingServiceImplBase {

    private final BillingAccountRepository billingAccountRepository;

    @Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse> responseObserver) {

        log.info("createBillingAccount request received for patient: {}", billingRequest.getPatientId());

        // Business logic- eg save to database, perform calculations etc

        try {

            UUID patientId = UUID.fromString(billingRequest.getPatientId());

            BillingAccount newAccount = BillingAccount.builder()
                    .patientId(patientId)
                    .balance(BigDecimal.ZERO)
                    .status(BillingAccount.AccountStatus.ACTIVE)
                    .build();

            BillingAccount savedAccount = billingAccountRepository.save(newAccount);

            log.info("Successfully created account for patient: {}", savedAccount.getId());

            BillingResponse response = BillingResponse.newBuilder()
                    .setAccountId(String.valueOf(savedAccount.getId()))
                    .setStatus(savedAccount.getStatus().name())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            log.error("Exception while creating account for patient: {}", billingRequest.getPatientId(), e);

            responseObserver.onError(e);
        }

    }

}
