package in.co.bel.olms.stats.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import in.co.bel.olms.stats.dto.ObservationStatsDto;
import in.co.bel.olms.stats.repository.ObservationStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link ObservationStatsService}
 * @author Utsa Ghosh
 * @version 1.0
 * @since 2025-11-12
 * @see ObservationStatsRepository
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ObservationStatsService {

    private final ObservationStatsRepository statsRepository;

    private static final Map<Integer, String> RESULT_TYPES = Map.of(1, "Pass", 0, "Fail");
    private static final Map<Integer, String> RESPONSE_TYPES = Map.of(1, "devId", 2, "specId", 3, "testId", 4, "productId");

    private static final Map<Integer, String> MONTHS = Map.ofEntries(
            Map.entry(1, "January"),
            Map.entry(2, "February"),
            Map.entry(3, "March"),
            Map.entry(4, "April"),
            Map.entry(5, "May"),
            Map.entry(6, "June"),
            Map.entry(7, "July"),
            Map.entry(8, "August"),
            Map.entry(9, "September"),
            Map.entry(10, "October"),
            Map.entry(11, "November"), 
            Map.entry(12, "December"));

    private static final Map<Integer, String> QUARTERS = Map.of(
            1, "Qtr-1",
            2, "Qtr-2",
            3, "Qtr-3",
            4, "Qtr-4");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Get total number of observations for a given product within a specified date range and grouped by specified time intervals
     * @param from {@code Date} from which observations are to be included in response (inclusive)
     * @param to {@code Date} up to which observations are to be included in response (inclusive)
     * @param productId {@code Integer} optional filter by id of the product - accepts null
     * @param specId {@code Integer} optional filter by specId - accepts null
     * @param resultType {@code Integer} optional filter by result type - accepts null
     *      <br>{1 : Pass, 0 : Fail}
     * @param grouping {@code Integer} specifies time interval for grouping
     *      <br>{12 : Yearly, 3 : Quarterly, 1 : Monthly}
     * @return {@code Map<Integer, Integer>} key-value pairs of type timeUnitIdentifier : count
     * @throws IllegalArgumentException if from / to are null, specId provided without productId, invalid resultType / grouping
     */
    public Map<String, Integer> getObservationStats(Date from, Date to, Integer productId, Integer specId, Integer resultType, Integer grouping) {

        // Checking validity of arguments

        // date range
        if (Objects.isNull(from) || Objects.isNull(to)){
            throw new IllegalArgumentException("Invalid date range provided");
        }

        // product and spec
        if (Objects.isNull(productId) && Objects.nonNull(specId)){
            throw new IllegalArgumentException("SpecId provided without any productId");
        }

        // result type
        String resultTypeString = RESULT_TYPES.get(resultType);
        if ( Objects.nonNull(resultType) && Objects.isNull(resultTypeString) ){
            log.error("Invalid argument for result type");
            throw new IllegalArgumentException("Invalid argument for result type");
        }

        log.info("getObservationStats from= {}, to= {}, product_id= {}, specId= {}, resultType= {}",
                dateFormat.format(from), dateFormat.format(to), productId, specId, resultTypeString);

        TreeMap<Integer, Integer> rawStats = new TreeMap<>(); // to maintain order by year / month / quarter number
        LinkedHashMap<String, Integer> stats = new LinkedHashMap<>(); // to maintain insertion order

        List<Object[]> data = null;

        switch (grouping){
            case 12: // Yearly
                log.info("grouping= Yearly");
                data = statsRepository.getObservationsByYear(from, to, productId, specId, resultTypeString);
                break;
            case 3: // Quarterly
                log.info("grouping= Quarterly");
                data = statsRepository.getObservationsByQuarter(from, to, productId, specId, resultTypeString);
                break;
            case 1: // Monthly
                log.info("grouping= Monthly");
                data = statsRepository.getObservationsByMonth(from, to, productId, specId, resultTypeString);
                break;
            default: // Invalid argument for grouping
                log.error("Invalid argument for grouping");
                throw new IllegalArgumentException("Invalid argument for grouping");
        }
        if (Objects.nonNull(data) && !data.isEmpty()){
            for (Object[] record : data){
                rawStats.put(new BigDecimal(record[0].toString()).intValue(), new BigDecimal(record[1].toString()).intValue());
            }
        }
        switch (grouping){
            case 12: // Yearly
                rawStats.forEach((year, count) -> stats.put(year.toString(), count));
                break;
            case 3: // Quarterly
                rawStats.forEach((quarter, count) -> stats.put(QUARTERS.get(quarter), count));
                break;
            case 1: // Monthly
                rawStats.forEach((month, count) -> stats.put(MONTHS.get(month), count));
                break;
            default: // Invalid argument for grouping
                throw new IllegalArgumentException("Invalid argument for grouping");
        }
        return stats;
    }

    /**
     * Get observations within a specified date range
     *
     * @param fromDate  {@code LocalDate} from which observations are to be included in response (inclusive)
     * @param toDate    {@code LocalDate} up to which observations are to be included in response (inclusive)
     * @param productId {@code Integer} optional filter by id of the product
     * @param testId    {@code Integer} optional filter by testId
     * @param specId    {@code Integer} optional filter by specId
     * @param isQa      {@code Boolean} optional filter if observation done by QA
     * <br> All optional filters accept {@code null} values
     * @return {@code List<ObservationStatsDto>}
     */
    public List<ObservationStatsDto> getObservationStatsV2(LocalDate fromDate, LocalDate toDate, Integer productId, Integer testId, Integer specId, Boolean isQa) {
        log.info("Get Observation Stats V2 \n " +
                "params: fromDate= {}, toDate= {}, productId= {}, testId= {}, specId= {}, isQa= {}",
                fromDate, toDate, productId, testId, specId, isQa);

        List<Object[]> rawData = null;
        List<ObservationStatsDto> response = new ArrayList<>();
        int condition = 0;

        if (Objects.nonNull(specId)){
            // filter by product, test, spec
            // show by devId
            rawData = statsRepository.getObservationsInRangeForProductTestSpec(fromDate,toDate,productId,testId,specId,isQa);
            condition = 1;
        } else if (Objects.nonNull(testId)) {
            // filter by product, test
            // show by specId
            rawData = statsRepository.getObservationsInRangeForProductTest(fromDate,toDate,productId,testId,isQa);
            condition = 2;
        } else if (Objects.nonNull(productId)) {
            // filter by product
            // show by testId
            rawData = statsRepository.getObservationsInRangeForProduct(fromDate,toDate,productId,isQa);
            condition = 3;
        } else {
            // get all observations in the date range
            // show by productId
            rawData = statsRepository.getObservationsInRange(fromDate,toDate,isQa);
            condition = 4;
        }

        log.info("rawData.size() = {}", Objects.isNull(rawData) ? 0 : rawData.size());

        // create response from raw data
        if (Objects.nonNull(rawData) && !rawData.isEmpty()){
            for (Object[] record : rawData){
                String type = RESPONSE_TYPES.get(condition);
                Integer id = new BigDecimal(record[0].toString()).intValue();
                Integer passCount = new BigDecimal(record[1].toString()).intValue();
                Integer failCount = new BigDecimal(record[2].toString()).intValue();

                response.add(new ObservationStatsDto(type, id, passCount, failCount));
            }
        }
        log.info("response.size() = {}", response.size());
        return response;
    }
}
