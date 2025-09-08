package com.digicore.omni.root.services.modules.backoffice.settings.bankussdmanagement.controller;



import com.digicore.omni.data.lib.modules.backoffice.dto.BankUSSDDTO;
import com.digicore.omni.root.services.modules.backoffice.settings.bankussdmanagement.service.BackOfficeBankUSSDService;
import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

;

@RestController
@RequestMapping("/api/v1/backoffice/bank-ussd/process/")
public class BankOfficeUSSDController {

    private final BackOfficeBankUSSDService bankUSSDService;

    public BankOfficeUSSDController(BackOfficeBankUSSDService bankUSSDService) {
        this.bankUSSDService = bankUSSDService;
    }

    @PostMapping("create-bank-ussd")
    public ResponseEntity<Object> createBankUSSD(@RequestBody BankUSSDDTO bankUSSDDTO)  {
        return CommonUtils.buildSuccessResponse(bankUSSDService.createBankUSSD(bankUSSDDTO));
    }

    @GetMapping("get-all-bank-ussd")
    public ResponseEntity<Object> getAllBankUSSD(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                              @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize){
        return CommonUtils.buildSuccessResponse(bankUSSDService.getAllBankUSSD(pageNumber,pageSize));
    }

    @PatchMapping("edit-{id}-bank-ussd")
    public ResponseEntity<Object> updateBankUSSD(@RequestBody BankUSSDDTO bankUSSDDTO, @PathVariable Long id)  {
        return CommonUtils.buildSuccessResponse(bankUSSDService.editBankUSSD(bankUSSDDTO, id));
    }

    @DeleteMapping("delete-{id}-bank-ussd")
    public ResponseEntity<Object> deleteBankUSSD(@PathVariable Long id){
        bankUSSDService.deleteBankUSSDById(id);
        return CommonUtils.buildSuccessResponse();
    }

    @GetMapping("fetch-{name}-bank-ussd")
    public ResponseEntity<Object> searchBankUSSDByName(@PathVariable String name,
                                                                                    @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
                                                                                    @RequestParam(value = "pageSize", defaultValue = "50", required = false) int pageSize)  {
        return CommonUtils.buildSuccessResponse(bankUSSDService.searchBankUSSDBYName(name,pageNumber,pageSize));
    }
}
