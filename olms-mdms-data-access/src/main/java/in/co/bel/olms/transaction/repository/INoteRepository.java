package in.co.bel.olms.transaction.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.transaction.entity.INote;

public interface INoteRepository extends JpaRepository<INote, Integer> {
	
	@Query(value="SELECT * FROM tdms.inote WHERE offer_letter_id = :olId ORDER BY created_datetime DESC LIMIT 1", nativeQuery=true)
	public INote getByOlId(int olId);
	
	@Query(value="SELECT  Count(id)"
			+ "FROM   tdms.inote "
			+ "WHERE  inote_date >= "
			+ "       ( "
			+ "              SELECT "
			+ "                     CASE "
			+ "                            WHEN Extract(month FROM CURRENT_DATE) < 4 THEN date_trunc('year', CURRENT_DATE - interval '1 year') + interval '3 months' "
			+ "                            ELSE date_trunc('year', CURRENT_DATE) + interval '3 months' "
			+ "                     END)"
			+ " AND department_id = :deptId ", nativeQuery=true)
	public Integer getInoteCountInCurrentFY(Integer deptId);
	
	@Query(value="SELECT I.inote_no, "
            + "                   TO_CHAR(inote_date, 'dd-MM-yyyy') "
            + "            FROM   tdms.inote I "
            + "                   LEFT JOIN tdms.inspection_note OL "
            + "                          ON I.file_note_id= OL.id "
            + "                   LEFT JOIN tdms.supply_order PO "
            + "                          ON OL.supply_order_id = PO.id "
            + "            WHERE  PO.id = :soId and inote_date is not null "
            + "            ORDER BY I.created_datetime DESC limit 1", nativeQuery = true)
    public List<Object[]> getLastInoteDetailsBySoId(int soId);
	
	@Query(value="SELECT quarter, "
			+ "       SUM(form4_count) AS FORM4_COUNT, "
			+ "       SUM(inote_count) AS INOTE_COUNT "
			+ "FROM   (SELECT 'Qtr ' "
			+ "               || CASE "
			+ "                    WHEN Extract(month FROM inote_date) >= 4 "
			+ "                         AND Extract(month FROM inote_date) <= 6 THEN 1 "
			+ "                    WHEN Extract(month FROM inote_date) >= 7 "
			+ "                         AND Extract(month FROM inote_date) <= 9 THEN 2 "
			+ "                    WHEN Extract(month FROM inote_date) >= 10 "
			+ "                         AND Extract(month FROM inote_date) <= 12 THEN 3 "
			+ "                    ELSE 4 "
			+ "                  END AS QUARTER, "
			+ "               CASE "
			+ "                 WHEN inote_type_id = 2 THEN Count(id) "
			+ "                 ELSE 0 "
			+ "               END    AS FORM4_COUNT, "
			+ "               CASE "
			+ "                 WHEN inote_type_id = 3 THEN Count(id) "
			+ "                 ELSE 0 "
			+ "               END    AS INOTE_COUNT "
			+ "        FROM   tdms.inote "
			+ "        WHERE  inote_date >= :startDate "
			+ "               AND inote_date <= :endDate "
			+ "        GROUP  BY quarter, "
			+ "                  inote_type_id) T "
			+ "GROUP  BY quarter "
			+ "ORDER  BY quarter", nativeQuery=true)
	public List<Object[]> getQtrWiseINotes(LocalDate startDate, LocalDate endDate);
	
	@Query(value="SELECT MONTH_NAME, "
			+ "       COALESCE(SUM(FORM4_COUNT),0) AS FORM4_COUNT, "
			+ "       COALESCE(SUM(INOTE_COUNT),0) AS INOTE_COUNT "
			+ "FROM   (SELECT TO_CHAR(I.INOTE_DATE, 'MON') AS MONTH_NAME, "
			+ "               CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 2 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS FORM4_COUNT, "
			+ "               CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 3 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS INOTE_COUNT "
			+ "        FROM   TDMS.INOTE I "
			+ "        WHERE  I.INOTE_DATE >= :startDate "
			+ "               AND I.INOTE_DATE <= :endDate "
			+ "        GROUP  BY I.INOTE_DATE, "
			+ "                  MONTH_NAME, "
			+ "                  I.INOTE_TYPE_ID) T "
			+ "GROUP  BY MONTH_NAME", nativeQuery=true)
	public List<Object[]> getMonthWiseINotes(LocalDate startDate, LocalDate endDate);
	
	@Query(value="SELECT COALESCE(SUM(FORM4_COUNT),0) AS FORM4_COUNT, "
			+ "       COALESCE(SUM(INOTE_COUNT),0) AS INOTE_COUNT "
			+ "FROM   (SELECT CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 2 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS FORM4_COUNT, "
			+ "               CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 3 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS INOTE_COUNT "
			+ "        FROM TDMS.INOTE I "
			+ "        WHERE I.INOTE_DATE >= :startDate "
			+ "               AND I.INOTE_DATE <= :endDate "
			+ "        GROUP BY I.INOTE_DATE, "
			+ "                  I.INOTE_TYPE_ID) T", nativeQuery=true)
	public List<Object[]> getYearWiseINotes(LocalDate startDate, LocalDate endDate);

	@Query(value="SELECT COALESCE(SUM(FORM4_COUNT), 0) AS FORM4_COUNT, "
			+ "       COALESCE(SUM(INOTE_COUNT), 0) AS INOTE_COUNT "
			+ "FROM   (SELECT CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 2 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS FORM4_COUNT, "
			+ "               CASE "
			+ "                 WHEN I.INOTE_TYPE_ID = 3 THEN COUNT(I.ID) "
			+ "                 ELSE 0 "
			+ "               END AS INOTE_COUNT "
			+ "        FROM   TDMS.INOTE I "
			+ "               LEFT JOIN TDMS.OFFER_LETTER OL "
			+ "                      ON I.OFFER_LETTER_ID = OL.ID "
			+ "               LEFT JOIN TDMS.SALE_ORDER SO "
			+ "                      ON OL.SALE_ORDER_ID = SO.ID "
			+ "               LEFT JOIN TDMS.PROJECT P "
			+ "                      ON SO.SUPPLY_ORDER_ID = P.SUPPLY_ORDER_ID "
			+ "        WHERE  P.ID = :projectId "
			+ "        GROUP  BY I.INOTE_DATE, "
			+ "                  I.INOTE_TYPE_ID) T", nativeQuery=true)
	public List<Object[]> getINoteDetailsProjectWise(int projectId);
	
	@Query(value="SELECT month_name, "
			+ "       Sum(inote_value) AS INOTE_VALUE "
			+ "FROM   (SELECT To_char(I.inote_date, 'MON') AS MONTH_NAME, "
			+ "               Sum(ILI.accepted * SLI.unit_cost)/10000000 AS INOTE_VALUE "
			+ "        FROM   tdms.inote I "
			+ "               LEFT JOIN tdms.inote_line_item ILI "
			+ "                      ON ILI.inote_id = I.id "
			+ "               LEFT JOIN tdms.ol_line_item OLI "
			+ "                      ON ILI.ol_line_item_id = OLI.id "
			+ "               LEFT JOIN tdms.so_line_item SLI "
			+ "                      ON OLI.so_line_item_id = SLI.id "
			+ "               LEFT JOIN tdms.sale_order SO "
			+ "                      ON SLI.sale_order_id = SO.id "
			+ "               LEFT JOIN tdms.supply_order PO "
			+ "                      ON SO.supply_order_id = PO.id "
			+ "        WHERE  PO.sales_grp_id = :salesGroupId "
			+ "               AND inote_date >= :startDate "
			+ "               AND inote_date <= :endDate "
			+ "        GROUP  BY inote_date) T "
			+ "GROUP  BY month_name", nativeQuery=true)
	public List<Object[]> getINoteValueBySalesGrpId(int salesGroupId, LocalDate startDate, LocalDate endDate);

	@Query(value="SELECT sales_grp_name, "
			+ "       Sum(inote_value) AS INOTE_VALUE "
			+ "FROM   (SELECT SG.NAME AS SALES_GRP_NAME, "
			+ "               Sum(ILI.accepted * SLI.unit_cost) / 10000000 AS INOTE_VALUE "
			+ "        FROM   tdms.inote I "
			+ "               LEFT JOIN tdms.inote_line_item ILI "
			+ "                      ON ILI.inote_id = I.id "
			+ "               LEFT JOIN tdms.ol_line_item OLI "
			+ "                      ON ILI.ol_line_item_id = OLI.id "
			+ "               LEFT JOIN tdms.so_line_item SLI "
			+ "                      ON OLI.so_line_item_id = SLI.id "
			+ "               LEFT JOIN tdms.sale_order SO "
			+ "                      ON SLI.sale_order_id = SO.id "
			+ "               LEFT JOIN tdms.supply_order PO "
			+ "                      ON SO.supply_order_id = PO.id "
			+ "               LEFT JOIN mdms.sales_group SG "
			+ "                      ON PO.sales_grp_id = SG.id "
			+ "         WHERE  (:orgId = -1 or PO.organization_id = :orgId) "
			+ "               AND inote_date >= :fyStartDate "
			+ "               AND inote_date <= :fyEndDate "
			+ "        GROUP  BY sales_grp_name) T "
			+ "GROUP  BY sales_grp_name", nativeQuery=true)
	public List<Object[]> getINoteValueByFY(int orgId, LocalDate fyStartDate, LocalDate fyEndDate);

	@Query(value="SELECT inote_no, " +
			"       To_char(inote_date, 'dd-MM-yyyy') " +
			"FROM   tdms.inote I " +
			"       LEFT JOIN tdms.offer_letter OL " +
			"              ON I.offer_letter_id = OL.id " +
			"       LEFT JOIN tdms.sale_order SO " +
			"              ON OL.sale_order_id = SO.id " +
			"       LEFT JOIN tdms.supply_order PO " +
			"              ON SO.supply_order_id = PO.id " +
			"WHERE  PO.id = :soId " +
			"       AND i.id < :inoteId " +
			"ORDER  BY inote_date DESC " +
			"LIMIT  1", nativeQuery=true)
	public List<Object[]> getPrevInoteByInoteId(int soId, int inoteId);
	
	@Query(value="SELECT I.INOTE_NO ,"
            +"SG.NAME AS SECTION,"
            + "SG.ID AS SECTION_ID,"
            +"I.INOTE_DATE,"
            +"I.SUPPLY_ORDER_NO,"
            +"I.SUPPLY_ORDER_DATE,"
            +"    IND.NAME AS INDENTER_NAME ,"
            +"    I.OFFER_LETTER_NO,"
            +"        I.OFFER_LETTER_DATE,"
            +"        PO.FILE_NO,"
            +"        COALESCE(DE.LAST_DATE, PO.DD) AS DD, "
            +"        INO.TYPE_OF_DELIVERY,"
            +"        COUNT(OLI.*) AS TOTAL_ITEMS,"
            +"        SUM(OLI.OFFERED_QTY) AS TOTAL_QTY,"
            +"        SUM(OLI.OFFERED_QTY * SLI.UNIT_COST) AS OL_VALUE,"
            +"    SUM(ILI.ACCEPTED * SLI.UNIT_COST) / 10000000 AS INOTE_VALUE ,"
                + " PO.DESCRIPTION "
            +"    FROM TDMS.INOTE I "
            +"    LEFT JOIN TDMS.INOTE_LINE_ITEM ILI ON ILI.INOTE_ID = I.ID "
            +"    LEFT JOIN TDMS.OL_LINE_ITEM OLI ON ILI.OL_LINE_ITEM_ID = OLI.ID "
        +"        LEFT JOIN TDMS.SO_LINE_ITEM SLI ON OLI.SO_LINE_ITEM_ID = SLI.ID "
            +"    LEFT JOIN TDMS.SALE_ORDER SO ON SLI.SALE_ORDER_ID = SO.ID  "
            +"    LEFT JOIN TDMS.SUPPLY_ORDER PO ON SO.SUPPLY_ORDER_ID = PO.ID  "
            +"    LEFT JOIN TDMS.INSPECTION_NOTE INO ON I.FILE_NOTE_ID = INO.ID  "
            +"    LEFT JOIN MDMS.SALES_GROUP SG ON PO.SALES_GRP_ID = SG.ID  "
            +"  LEFT JOIN MDMS.INDENTER IND ON PO.INDENTER_ID = IND.ID "
            + "LEFT JOIN "
            + "        (SELECT "
            + "            SUPPLY_ORDER_ID,"
            + "            MAX(new_dd_date) AS LAST_DATE "
            + "        FROM "
            + "            TDMS.DD_EXTENSION "
            + "        GROUP BY "
            + "            SUPPLY_ORDER_ID "
            + "    ) DE ON PO.ID = DE.SUPPLY_ORDER_ID"
            + "    LEFT JOIN TDMS.OFFER_LETTER OL ON I.OFFER_LETTER_ID = OL.ID "
            +"    WHERE TO_TIMESTAMP(CAST(I.INOTE_DATE AS TEXT),'YYYY-MM-DD') >= TO_TIMESTAMP(:fromDate,'YYYY-MM-DD') "
            +"        AND TO_TIMESTAMP(CAST(I.INOTE_DATE AS TEXT),'YYYY-MM-DD') <= TO_TIMESTAMP(:toDate,'YYYY-MM-DD') "
                +"    AND PO.ORGANIZATION_ID = :orgId GROUP  BY I.INOTE_NO,"
                +"        I.INOTE_DATE,"
                +"        SECTION,"
                    +"            I.SUPPLY_ORDER_NO,"
                +"        I.SUPPLY_ORDER_DATE,"
                +"        IND.NAME,"
                +"        I.OFFER_LETTER_NO, "
                +"        I.OFFER_LETTER_DATE, "
                +"        PO.FILE_NO, "
                +"        INO.TYPE_OF_DELIVERY, "
                +"        PO.DD, "
                +"        SECTION_ID, "
                +"    OL.STORES_DESCRIPTION, "
                +"po.description, "
                + "DE.LAST_DATE", nativeQuery=true)
        public List<Object[]> getINoteRegister(int orgId, String fromDate, String toDate);
	@Query(value = "SELECT  " +
			"  COALESCE( " +
			"    SUM(FORM4_COUNT),  " +
			"    0 " +
			"  ) AS FORM4_COUNT,  " +
			"  COALESCE( " +
			"    SUM(INOTE_COUNT),  " +
			"    0 " +
			"  ) AS INOTE_COUNT  " +
			"FROM  " +
			"  ( " +
			"    SELECT  " +
			"      CASE WHEN I.INOTE_TYPE_ID = 2 THEN COUNT(I.ID) ELSE 0 END AS FORM4_COUNT,  " +
			"      CASE WHEN I.INOTE_TYPE_ID = 3 THEN COUNT(I.ID) ELSE 0 END AS INOTE_COUNT  " +
			"    FROM  " +
			"      TDMS.INOTE I  " +
			"      left join tdms.offer_letter ol on i.offer_letter_id = ol.id  " +
			"      left join tdms.sale_order so on ol.sale_order_id = so.id  " +
			"      left join tdms.supply_order po on so.supply_order_id = po.id  " +
			"    WHERE  " +
			"      po.organization_id = :orgId  " +
			"      and I.INOTE_DATE >= :startDate  " +
			"      AND I.INOTE_DATE <= :endDate  " +
			"    GROUP BY  " +
			"      I.INOTE_DATE,  " +
			"      I.INOTE_TYPE_ID " +
			"  ) T ", nativeQuery = true)
	public List<Object[]> getINoteDetailsForDGQAChart(int orgId, LocalDate startDate, LocalDate endDate);

	@Query(value = "select " +
			"  org_name, " +
			"  Sum(inote_value) as INOTE_VALUE " +
			"from " +
			"  (" +
			"    select " +
			"      o.organization_name as org_name, " +
			"      Sum(ILI.accepted * SLI.unit_cost) / 10000000 as INOTE_VALUE " +
			"    from " +
			"      tdms.inote I " +
			"      left join tdms.inote_line_item ILI on ILI.inote_id = I.id " +
			"      left join tdms.ol_line_item OLI on ILI.ol_line_item_id = OLI.id " +
			"      left join tdms.so_line_item SLI on OLI.so_line_item_id = SLI.id " +
			"      left join tdms.sale_order SO on SLI.sale_order_id = SO.id " +
			"      left join tdms.supply_order PO on SO.supply_order_id = PO.id " +
			"      left join mdms.organization o on PO.organization_id = o.id " +
			"    where " +
			"      inote_date >= :fyStartDate " +
			"      and inote_date <= :fyEndDate " +
			"    group by " +
			"      organization_name" +
			"  ) T " +
			"group by " +
			"  org_name", nativeQuery = true)
	public List<Object[]> getINoteValueByFYForDGQA(LocalDate fyStartDate, LocalDate fyEndDate);

	@Query(value = "SELECT * FROM tdms.inote WHERE file_note_id = :fileNoteId ORDER BY created_datetime DESC LIMIT 1", nativeQuery = true)
	public INote getByFilenoteId(int fileNoteId);
	
	
	@Query(value="SELECT inote_no,TO_CHAR(inote_date, 'dd-MM-yyyy') "
            + "            FROM  tdms.inote  "
            + "                   ORDER BY created_datetime  desc LImit 1" , nativeQuery = true)
    public Object[]  getLastFileNoteDetails( );

	@Query(value = "WITH offer_numbers AS ( " +
			"    SELECT ol_number " +
			"    FROM tdms.offer_letter " +
			"    WHERE id in (:olIds) " +
			") " +
			"SELECT * " +
			"FROM tdms.inote " +
			"JOIN offer_numbers " +
			"ON offer_numbers.ol_number = ANY( " +
			"    SELECT unnest(string_to_array(tdms.inote.offer_letter_no, ','))) LIMIT 1", nativeQuery = true)
	public INote getByOlNumbers(List<Integer> olIds);
	
	@Query(value = "SELECT  COUNT(i.id) AS i_note_count, SUM(sli. item_value) AS i_note_value "
 		+ "FROM tdms.inote i "
 		+ "LEFT JOIN tdms.inote_line_item inln on inln.inote_id = i.id "
 		+ "LEFT JOIN tdms.ol_line_item oli ON   inln.ol_line_item_id = oli.id "
 		+ "LEFT JOIN tdms.so_line_item sli ON sli.id = oli.so_line_item_id; ", nativeQuery = true)
	public	List<Object[]> getInoteDashboardDetails();
	 
	@Query(value = "SELECT  * from tdms.inote where offer_letter_id=:internalInoteId ", nativeQuery = true)
	public	INote getInoteByInternalInoteId(int internalInoteId);
		
	@Query(value = "SELECT COALESCE(MAX(CAST(SUBSTRING(cqae_inote_no FROM '[0-9]{5}') AS INTEGER)), 0) FROM tdms.inote", nativeQuery = true)
	public Integer getLastInoteNumber();

	public INote findByOlId(Integer id);
	
	 @Query(value = "SELECT COALESCE(SUM(ili.accepted), 0) FROM tdms.inote_line_item ili "
	    		+ "LEFT JOIN tdms.ol_line_item oli ON oli.id = ili.ol_line_item_id "
	    		+ "WHERE oli.so_line_item_id = :soLineItemId ", nativeQuery = true)
	public BigDecimal getTotalAcceptedQtyforSOLineItem(@Param("soLineItemId") Integer soLineItemId);
	 
}
