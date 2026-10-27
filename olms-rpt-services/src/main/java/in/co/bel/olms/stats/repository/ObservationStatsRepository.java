package in.co.bel.olms.stats.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.co.bel.olms.ate.entity.Observation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * {@link ObservationStatsRepository}
 *
 * @author Utsa Ghosh
 * @version 1.0
 * @since 2025-11-12
 */
@Repository
public interface ObservationStatsRepository extends JpaRepository<Observation, Integer> {

    @Query(value =
            "  select extract(year from submitted_date) as year, "
                    + " count(id) as count "
                    + " from ate.observation "
                    + " where submitted_date >= :from and submitted_date <= :to "
                    + " and (:productId is null or prod_id = :productId) "
                    + " and (:specId is null or spec_id =  :specId) "
                    + " and (:result is null or result = :result) "
                    + " group by year "
                    + " order by year ", nativeQuery = true)
    List<Object[]> getObservationsByYear(Date from, Date to, Integer productId, Integer specId, String result);

    @Query(value =
            "select"
                    + "  case "
                    +"      WHEN "
                    +"          Extract(month FROM submitted_date) >= 4 AND Extract(month FROM submitted_date) <= 6 "
                    +"          THEN 1 "
                    +"      WHEN "
                    +"          Extract(month FROM submitted_date) >= 7 AND Extract(month FROM submitted_date) <= 9 "
                    +"          THEN 2 "
                    +"      WHEN "
                    +"          Extract(month FROM submitted_date) >= 10 AND Extract(month FROM submitted_date) <= 12 "
                    +"          THEN 3 "
                    +"      ELSE 4 "
                    +"  end "
                    + " as quarter, "
                    + " count(id) as count "
                    + " from ate.observation "
                    + " where submitted_date >= :from and submitted_date <= :to "
                    + " and (:productId is null or prod_id = :productId) "
                    + " and (:specId is null or spec_id =  :specId) "
                    + " and (:result is null or result = :result) "
                    + " group by quarter "
                    + " order by quarter ",
            nativeQuery = true)
    List<Object[]> getObservationsByQuarter(Date from, Date to, Integer productId, Integer specId, String result);

    @Query(value =
            " select extract(month from submitted_date) as month, "
                    + " count(id) as count "
                    + " from ate.observation "
                    + " where submitted_date >= :from and submitted_date <= :to "
                    + " and (:productId is null or prod_id = :productId) "
                    + " and (:specId is null or spec_id =  :specId) "
                    + " and (:result is null or result = :result) "
                    + " group by month "
                    + " order by month ",
            nativeQuery = true)
    List<Object[]> getObservationsByMonth(Date from, Date to, Integer productId, Integer specId, String result);

    // new format

    @Query(value = " select "
            + " prod_id, "
            + " count(id) filter (where result = 'Pass') as pass_count, "
            + " count(id) filter (where result = 'Fail') as fail_count "
            + " from "
            + " (select o.id, submitted_date, o.prod_id, spec_id, dev_id, result, f.is_qa "
            + " from ate.observation o "
            + " left join ate.fai f on o.fai_id = f.id) t "
            + " where submitted_date between :from and :to "
            + " and is_qa is not distinct from :isQa "
            + " group by prod_id "
            + " order by fail_count desc, pass_count asc, prod_id asc", nativeQuery = true)
    List<Object[]> getObservationsInRange(LocalDate from, LocalDate to, Boolean isQa);

    @Query(value = " select "
            + " test_id, "
            + " count(id) filter (where result = 'Pass') as pass_count, "
            + " count(id) filter (where result = 'Fail') as fail_count "
            + " from "
            + " (select o.id, submitted_date, o.prod_id, spec_id, dev_id, result, test_id, f.is_qa "
            + " from ate.observation o "
            + " left join ate.fai f on o.fai_id = f.id "
            + " left join ate.spec s on o.spec_id = s.id) t "
            + " where submitted_date between :from and :to "
            + " and prod_id = :productId "
            + " and is_qa is not distinct from :isQa "
            + " group by test_id "
            + " order by fail_count desc, pass_count asc, test_id asc", nativeQuery = true)
    List<Object[]> getObservationsInRangeForProduct(LocalDate from, LocalDate to, Integer productId, Boolean isQa);

    @Query(value = " select "
            + " spec_id, "
            + " count(id) filter (where result = 'Pass') as pass_count, "
            + " count(id) filter (where result = 'Fail') as fail_count "
            + " from "
            + " (select o.id, submitted_date, o.prod_id, spec_id, dev_id, result, test_id, f.is_qa "
            + " from ate.observation o "
            + " left join ate.fai f on o.fai_id = f.id "
            + " left join ate.spec s on o.spec_id = s.id) t "
            + " where submitted_date between :from and :to "
            + " and prod_id = :productId "
            + " and test_id = :testId "
            + " and is_qa is not distinct from :isQa "
            + " group by spec_id "
            + " order by fail_count desc, pass_count asc, spec_id asc", nativeQuery = true)
    List<Object[]> getObservationsInRangeForProductTest(LocalDate from, LocalDate to, Integer productId, Integer testId, Boolean isQa);

    @Query(value = " select "
            + " dev_id, "
            + " count(id) filter (where result = 'Pass') as pass_count, "
            + " count(id) filter (where result = 'Fail') as fail_count "
            + " from "
            + " (select o.id, submitted_date, o.prod_id, spec_id, dev_id, result, test_id, f.is_qa "
            + " from ate.observation o "
            + " left join ate.fai f on o.fai_id = f.id "
            + " left join ate.spec s on o.spec_id = s.id) t "
            + " where submitted_date between :from and :to "
            + " and prod_id = :productId "
            + " and test_id = :testId "
            + " and spec_id = :specId "
            + " and is_qa is not distinct from :isQa "
            + " group by dev_id "
            + " order by fail_count desc, pass_count asc, dev_id asc", nativeQuery = true)
    List<Object[]> getObservationsInRangeForProductTestSpec(LocalDate from, LocalDate to, Integer productId, Integer testId, Integer specId, Boolean isQa);
}