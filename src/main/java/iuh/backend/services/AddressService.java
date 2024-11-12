package iuh.backend.services;

import iuh.backend.models.Address;
import iuh.backend.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address findById(long i) {
        return addressRepository.findById(i).get();
    }

    public List<Address> findAll() {
        return (List<Address>) addressRepository.findAll();
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}
