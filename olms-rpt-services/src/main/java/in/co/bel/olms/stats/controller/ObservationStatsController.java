package in.co.bel.olms.stats.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.stats.dto.ObservationStatsDto;
import in.co.bel.olms.stats.service.ObservationStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ObservationStatsController}
 *
 * @author Utsa Ghosh
 * @version 1.0
 * @since 2025-11-12
 * @see ObservationStatsService
 */
@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
@RequiredArgsConstructor
public class ObservationStatsController {

    private final ObservationStatsService statsService;

    private static final Set<Integer> GROUPINGS = Set.of(12,3,1);

    @Operation(
            summary = "Get Observation Stats",
            description = "Get pass/fail/all observations in a specified fiscal year " +
                    "grouped by specified time intervals " +
                    "optional filtering by productId, specId, resultType"
    )
    @GetMapping("/v1/observations")
    public ResponseEntity<Map<String, Integer>> getObservationStats(
            @Parameter(description = "Fiscal year in which observations are to be considered")
            @RequestParam Integer fy,

            @Parameter(description = "Integer specifying time interval for grouping: {12 : Yearly, 3 : Quarterly, 1 : Monthly}")
            @RequestParam Integer grouping,

            @Parameter(description = "Optional filter by id of the product")
            @RequestParam(required = false) Integer productId,

            @Parameter(description = "Optional filter by Spec Id")
            @RequestParam(required = false) Integer specId,

            @Parameter(description = "Optional filter by result type: {pass: 1, fail: 0, all: null}")
            @RequestParam(required = false) Integer resultType) {

        // Checking validity of arguments
        // fy
        if (fy <= 0){
            throw new IllegalArgumentException("Invalid input for fiscal year");
        }
        // grouping
        if (! GROUPINGS.contains(grouping)){
            throw new IllegalArgumentException("Invalid input for grouping");
        }
        // result type
        if (Objects.nonNull(resultType) && !resultType.equals(0) && !resultType.equals(1)){
            throw new IllegalArgumentException("Invalid input for result type");
        }
        // product and spec
        if (Objects.isNull(productId) && Objects.nonNull(specId)){
            throw new IllegalArgumentException("SpecId provided without any productId");
        }

        // Getting selected fiscal year start and end dates
        LocalDate fyStartDate = LocalDate.of(fy - 1, 4, 1); // Fiscal year starts on April 1st
        LocalDate fyEndDate = LocalDate.of(fy, 3, 31); // Ends on March 31st
        Date fromDate = Date.from(fyStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date toDate = Date.from(fyEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Map<String, Integer> stats = statsService.getObservationStats(fromDate, toDate, productId, specId, resultType, grouping);
        if (Objects.isNull(stats)){
            throw new DataNotFoundException("Data could not be fetched");
        }
        return ResponseEntity.ok(stats);
    }

    @Operation(
            summary = "Get Observation Stats",
            description = "Get observations in a specified fiscal year " +
                    "optional filtering by quarter, productId, testId, specId"
    )
    @GetMapping("/v2/observations")
    public ResponseEntity<List<ObservationStatsDto>> getObservationStatsV2(
            @Parameter(description = "Fiscal year in which observations are to be considered")
            @RequestParam Integer fy,

            @Parameter(description = "Integer specifying quarter")
            @RequestParam(required = false) Integer quarter,

            @Parameter(description = "Optional filter by id of the product")
            @RequestParam(required = false) Integer productId,

            @Parameter(description = "Optional filter by Test Id")
            @RequestParam(required = false) Integer testId,

            @Parameter(description = "Optional filter by Spec Id")
            @RequestParam(required = false) Integer specId,

            @Parameter(description = "Optional filter if done by QA")
            @RequestParam(required = false) Boolean isQa) {

        log.info("Get Observation Stats V2 \n " +
                "params: fy= {}, quarter= {}, productId= {}, testId= {}, specId= {}, isQa= {}", fy, quarter, productId, testId, specId, isQa);

        // Checking validity of arguments
        // fy
        if (fy <= 0){
            throw new IllegalArgumentException("Invalid input for fiscal year");
        }

        // product, test, spec
        if (Objects.nonNull(specId)){
            try {
                Objects.requireNonNull(testId);
                Objects.requireNonNull(productId);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("SpecId provided without valid testId, productId");
            }

        }
        else if (Objects.nonNull(testId)){
            try {
                Objects.requireNonNull(productId);
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("TestId provided without valid productId");
            }

        }

        // Getting start and end dates
        LocalDate startDate = null;
        LocalDate endDate = null;

        if (Objects.nonNull(quarter)){
            switch (quarter){
                case 1:
                    startDate = LocalDate.of(fy - 1, 4, 1); // Qtr-1 starts on April 1st
                    endDate = LocalDate.of(fy - 1, 6, 30); // Ends on June 30th
                    break;
                case 2:
                    startDate = LocalDate.of(fy - 1, 7, 1); // Qtr-2 starts on July 1st
                    endDate = LocalDate.of(fy - 1, 9, 30); // Ends on September 30th
                    break;
                case 3:
                    startDate = LocalDate.of(fy - 1, 10, 1); // Qtr-3 starts on October 1st
                    endDate = LocalDate.of(fy - 1, 12, 31); // Ends on December 31st
                    break;
                case 4:
                    startDate = LocalDate.of(fy, 1, 1); // Qtr-4 starts on January 1st
                    endDate = LocalDate.of(fy, 3, 31); // Ends on March 31st
                    break;
                default:
                    throw new IllegalArgumentException("Invalid input for quarter");
            }
        } else {
            // quarter not specified
            startDate = LocalDate.of(fy - 1, 4, 1); // Fiscal year starts on April 1st
            endDate = LocalDate.of(fy, 3, 31); // Ends on March 31st
        }

        List<ObservationStatsDto> stats = statsService.getObservationStatsV2(startDate, endDate, productId, testId, specId, isQa);
        if (Objects.isNull(stats)){
            throw new DataNotFoundException("Data could not be fetched");
        }
        return ResponseEntity.ok(stats);
    }


}
