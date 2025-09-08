package com.digicore.omni.root.services.modules.backoffice.settings.bankussdmanagement.service;



import com.digicore.omni.data.lib.modules.backoffice.dto.BankUSSDDTO;
import com.digicore.omni.data.lib.modules.backoffice.model.BankUSSD;
import com.digicore.omni.data.lib.modules.common.apimodel.PaginatedResponseApiModel;
import com.digicore.omni.root.lib.modules.backoffice.service.BankUSSDService;
import org.springframework.stereotype.Service;



@Service
public class BackOfficeBankUSSDService {

    private final BankUSSDService bankUSSDService;

    public BackOfficeBankUSSDService(BankUSSDService bankUSSDService) {
        this.bankUSSDService = bankUSSDService;
    }

    public BankUSSD createBankUSSD(BankUSSDDTO bankUSSDDTO)  {
        return bankUSSDService.createBankUSSD(bankUSSDDTO);
    }

    public PaginatedResponseApiModel<BankUSSD> getAllBankUSSD(int pageNumber, int pageSize){
      return bankUSSDService.allBankUSSD(pageNumber, pageSize);
    }

    public void deleteBankUSSDById(Long id){
        bankUSSDService.deleteBankUSSD(id);
    }

    public BankUSSD editBankUSSD(BankUSSDDTO bankUSSDDTO, Long id)  {
        return bankUSSDService.updateBankUSSD(bankUSSDDTO, id );
    }

    public PaginatedResponseApiModel<BankUSSD> searchBankUSSDBYName(String bankName, int page, int size)  {
        return bankUSSDService.searchBankUSSD(bankName,page,size);
    }
}
