package com.xebia.observability.controller;

import com.xebia.observability.model.Person;
import com.xebia.observability.service.ObservabilityService;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class ObservabilityController {

    private final ObservabilityService observerService;

    private static final Logger logger = LoggerFactory.getLogger(ObservabilityController.class);
    private final Tracer tracer;

    @Autowired
    public ObservabilityController(ObservabilityService observerService) {
        this.observerService = observerService;
        this.tracer = GlobalOpenTelemetry.get().getTracer("com.xebia.observability.controller");
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person newPerson = observerService.createPerson(person);
        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }

    @GetMapping("/prueba")
    public String prueba() {
        Span span = tracer.spanBuilder("prueba-endpoint")
                .startSpan();
        try (Scope scope = span.makeCurrent()) {
            System.out.println("pruebaaaaaaaaaaaa");
            return "ESTA VIVO!";
        } finally {
            span.end();
        }
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return observerService.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> user = observerService.getPersonById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        Person personUpdated = observerService.updatePerson(id, person);
        return personUpdated != null ? ResponseEntity.ok(personUpdated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return observerService.deletePerson(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
