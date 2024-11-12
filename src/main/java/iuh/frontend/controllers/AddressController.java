package iuh.frontend.controllers;

import iuh.backend.models.Address;
import iuh.backend.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("")
    public ResponseEntity<List<Address>> showAddressList() {
        Optional<List<Address>> addresses = Optional.of(addressService.findAll());
        return ResponseEntity.ok(addresses.get());
    }
}
