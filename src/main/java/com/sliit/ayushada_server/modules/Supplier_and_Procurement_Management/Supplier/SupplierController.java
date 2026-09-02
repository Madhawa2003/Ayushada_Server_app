package com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier;

import com.sliit.ayushada_server.modules.Supplier_and_Procurement_Management.Supplier.Dto.SupplierSendDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
    @Autowired
    SupplierService supplierService;

    @GetMapping()
    public List<Supplier> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }

    @GetMapping("getbyId/{id}")
    public SupplierSendDataDto getSupplierById(@PathVariable int id){
        return supplierService.getSupplierById(id);
    }

    @PostMapping("Create")
    public SupplierSendDataDto createSupplier(@RequestBody SupplierSendDataDto supplierData){
        return supplierService.saveSupplier(supplierData);
    }
}
