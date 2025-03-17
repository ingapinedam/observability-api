package com.xebia.observability.service;

import com.xebia.observability.model.Person;
import com.xebia.observability.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ObservabilityService {

    UserRepository repository;

    @Autowired
    public ObservabilityService(UserRepository repository) {
        this.repository = repository;
    }

    public Person createPerson(Person Person) {
        return repository.save(Person);
    }

    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    public Optional<Person> getPersonById(Long id) {
        return repository.findById(id);
    }

    public Person updatePerson(Long id, Person Person) {
        if (repository.existsById(id)) {
            Person.setId(id);
            return repository.save(Person);
        }
        return null;
    }

    public boolean deletePerson(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
