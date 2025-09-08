package com.digicore.omni.root.services.modules.backoffice.dispute.processor;

import com.digicore.omni.root.services.modules.backoffice.dispute.service.BackOfficeDisputeServiceImpl;
import com.digicore.request.processor.annotations.RequestHandler;
import com.digicore.request.processor.annotations.RequestType;
import com.digicore.request.processor.enums.RequestHandlerType;
import lombok.RequiredArgsConstructor;

/**
 * @author Monsuru
 * @since Dec-01(Thu)-2022
 */
@RequestHandler(type = RequestHandlerType.DISPUTE_MANAGEMENT)
@RequiredArgsConstructor
public class BackOfficeDisputeProcessor {

    private final BackOfficeDisputeServiceImpl backOfficeDisputeServiceImpl;

    @RequestType(name = "logTransactionDispute")
    public Object logTransactionDispute(Object request){
        return backOfficeDisputeServiceImpl.logTransactionDispute(request);
    }

    @RequestType(name = "approveDisputeRejection")
    public Object approveDisputeRejection(Object request)  {
        return backOfficeDisputeServiceImpl.approveDisputeRejection(request);
    }
    @RequestType(name = "declineDisputeRejection")
    public Object declineDisputeRejection(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeRejection(request);
    }

    // where the merchant rejected a dispute + initiator approved it + approver rejected it => + x - = - (dispute open)
    @RequestType(name = "declineApproveDisputeRejection")
    public Object declineApproveDisputeRejection(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeRejection(request);
    }

    // where the merchant rejected a dispute + initiator declined it + approver rejected the initiators decline => - x - = + ???(dispute rejected)
    @RequestType(name = "declineDeclineDisputeRejection")
    public Object declineDeclineDisputeRejection(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeRejection(request);
    }

    @RequestType(name = "approveDisputeManualHandling")
    public Object approveDisputeManualHandling(Object request)  {
        return backOfficeDisputeServiceImpl.approveDisputeManualHandling(request);
    }

    @RequestType(name = "declineDisputeManualHandling")
    public Object declineDisputeManualHandling(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeManualHandling(request);
    }

    // initiator approve + approver decline
    @RequestType(name = "declineApproveDisputeManualHandling")
    public Object declineApproveDisputeManualHandling(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeManualHandling(request);
    }

    // initiator decline + approver decline
    @RequestType(name = "declineDeclineDisputeManualHandling")
    public Object declineDeclineDisputeManualHandling(Object request)  {
        return backOfficeDisputeServiceImpl.declineDisputeManualHandling(request);
    }
}
