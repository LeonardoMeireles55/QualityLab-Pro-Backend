package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.services.GenericAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/analytics")
@Validated
public class GenericAnalyticsController {
    private final GenericAnalyticsService genericAnalyticsService;

    @PostMapping
    @Transactional
    @RequestMapping(value = "/sendValues", method = RequestMethod.POST)
    public ResponseEntity<List<GenericAnalytics>> sendValues
            (@RequestBody List<@Valid ValuesOfLevelsGenericRecord> values) {
        Stream<GenericAnalytics> valuesOfGenericsList = genericAnalyticsService.sendValues(values);
          var response = ResponseEntity.ok().body(valuesOfGenericsList.toList());
        if(!Objects.equals(response.getBody(), Collections.emptyList())) {
            return response;
        }
        throw new ErrorHandling.DataIntegrityViolationException();
    }

    @DeleteMapping
    @Transactional
    @RequestMapping(value = "/deleteAnalyticsResultById", method = RequestMethod.DELETE)
    ResponseEntity<Void> deleteAnalyticsResultById(@RequestParam Long id) {
        genericAnalyticsService.deleteAnalyticsById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<GenericAnalytics> getResultsById(@PathVariable Long id){
        var defaultValues = genericAnalyticsService.getResultsById(id);

        return ResponseEntity.ok(defaultValues);
    }

    @GetMapping
    @RequestMapping(value = "/getAllResults" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResults(Pageable pageable) {
        return ResponseEntity.ok().body(genericAnalyticsService.getAllResults(pageable));
    }

    @GetMapping("/getAllResultsHateoas")
    public ResponseEntity<CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>>>
    getAllResultsHateoas(Pageable pageable) {
        List<ValuesOfLevelsGenericRecord> resultsList = genericAnalyticsService.getAllResults(pageable);
    
        List<EntityModel<ValuesOfLevelsGenericRecord>> resultModels = resultsList.stream()
                .map(result -> EntityModel.of(result,
                        linkTo(methodOn(GenericAnalyticsController.class).getAllResultsByName(Pageable.unpaged(), result.name()))
                                .withSelfRel()))
                .collect(Collectors.toList());
    
        CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>> collectionModel =
                CollectionModel.of(resultModels,
                        linkTo(methodOn(GenericAnalyticsController.class).getAllResultsHateoas(pageable))
                                .withSelfRel());
    
        return ResponseEntity.ok().body(collectionModel);
    }
    

    @GetMapping
    @RequestMapping(value = "/getAllResultsByName/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAllResultsByName(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getAllResultsByName(pageable, name));
    }

    @GetMapping
    @RequestMapping(value = "/getAllResultsByName/orderAsc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAllResultsByNameOrderByDateAsc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateAsc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getAllResultsByName/orderDesc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getAllResultsByNameOrderByDateDesc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateDesc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getAllResultsByNameLevel/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getResultsByLevel
            (Pageable pageable, @PathVariable String name, @PathVariable String level) {
            return ResponseEntity.ok()
                    .body(genericAnalyticsService.getAllResultsByNameAndLevel(pageable, name, level));
    }
    @GetMapping
    @RequestMapping(value = "/getAllResultsByDate/{name}/{level}/{dateStart}/{dateEnd}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>> getAllResultsByDate(
                                                                      @PathVariable String name,
                                                                      @PathVariable String level,
                                                                      @PathVariable String dateStart,
                                                                      @PathVariable String dateEnd) {
        return ResponseEntity.ok().body(genericAnalyticsService
                .getAllResultsByNameAndLevelAndDate(name, level, dateStart, dateEnd));
    }

}