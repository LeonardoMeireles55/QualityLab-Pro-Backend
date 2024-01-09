package leonardo.labutilities.qualitylabpro.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import leonardo.labutilities.qualitylabpro.infra.exception.ErrorHandling;
import leonardo.labutilities.qualitylabpro.domain.entitys.GenericAnalytics;
import leonardo.labutilities.qualitylabpro.record.genericAnalytics.ValuesOfLevelsGenericRecord;
import leonardo.labutilities.qualitylabpro.repository.GenericAnalyticsRepositoryCustom;
import leonardo.labutilities.qualitylabpro.services.GenericAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/bio")
@Validated
public class GenericAnalyticsController {
    private final GenericAnalyticsService genericAnalyticsService;
    private final GenericAnalyticsRepositoryCustom genericAnalyticsRepositoryCustom;

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

    @GetMapping()
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity <Optional<GenericAnalytics>> getResultsById(@PathVariable Long id){
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
    getAllResultsHateos(Pageable pageable) {
        List<ValuesOfLevelsGenericRecord>
                resultsList = genericAnalyticsService.getAllResults(pageable);

        List<EntityModel<ValuesOfLevelsGenericRecord>>
                resultModels = resultsList.stream()
                .map(result -> {
                    Link selfLink = linkTo(methodOn(GenericAnalyticsController.class)
                            .getResultsByName(Pageable.unpaged(), result.name())).withSelfRel();
                    return EntityModel.of(result, selfLink);
                })
                .collect(Collectors.toList());

        CollectionModel<EntityModel<ValuesOfLevelsGenericRecord>> collectionModel =
                CollectionModel.of(resultModels,
                        linkTo(methodOn(GenericAnalyticsController.class)
                                .getAllResultsHateos(pageable)).withSelfRel());

        return ResponseEntity.ok().body(collectionModel);
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getResultsByName(Pageable pageable, @PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByName(pageable, name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/orderAsc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getResultsByNameOrderByDateAsc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateAsc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByName/orderDesc/{name}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getResultsByNameOrderByDateDesc(@PathVariable String name) {
        return ResponseEntity.ok().body(genericAnalyticsService.getResultsByDateDesc(name));
    }

    @GetMapping
    @RequestMapping(value = "/getResultsByNameLevel/{name}/{level}" , method = RequestMethod.GET)
    public ResponseEntity<List<ValuesOfLevelsGenericRecord>>
    getResultsByLevel
            (Pageable pageable, @PathVariable String name, @PathVariable String level) {
            return ResponseEntity.ok()
                    .body(genericAnalyticsService.getResultsByNameAndLevel(pageable, name, level));
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