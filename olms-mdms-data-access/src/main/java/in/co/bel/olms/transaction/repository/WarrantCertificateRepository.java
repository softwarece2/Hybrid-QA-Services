package in.co.bel.olms.transaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.transaction.entity.WarrantCertificate;

public interface WarrantCertificateRepository extends JpaRepository<WarrantCertificate, Integer> {

  @Query(value = "select  " +
      " p.part_no,  " +
      " p.material_description,  " +
      " p.drawing_no,  " +
      " sli.order_type_no,  " +
      " i.id as inote_id,  " +
      " i.inote_no,  " +
      " ili.tendered,  " +
      " ili.reg_no,  " +
      " i.inote_date,  " +
      " wc.start_date,  " +
      " wc.end_date,  " +
      " wc.approval_date,  " +
      " wt.type,  " +
          " emp.ename," +
          " wc.is_doa_approved," +
          " wc.warrant_type, " +
          " wc.id," +
          " wc.doa_approver_id ," +
          "sli.order_type_date ," +
          "sli.warr_period_qty " +
      " from mdms.part_number p    " +
      " join tdms.so_line_item sli on sli.part_number_id = p.id    " +
      " join tdms.sale_order so on so.id = sli.sale_order_id   " +
      " join tdms.offer_letter ol on ol.sale_order_id = so.id  " +
      " join tdms.inote i on i.offer_letter_id = ol.id    " +
      " join tdms.inote_line_item ili on ili.inote_id = i.id   " +
      " left join tdms.warrant_certificate wc on wc.inote_id = i.id    " +
      " left join mdms.warrant_type wt on wt.id = sli.warr_period_type    " +
      " left join mdms.employee emp on emp.id = wc.authority_id  " +
      " where i.id =:inoteId ", nativeQuery = true)
	public List<Object[]> getWarrantDetailsByInoteId(Integer inoteId);

    @Query(value = "SELECT P.PART_NO, P.MATERIAL_DESCRIPTION, P.DRAWING_NO, SLI.ORDER_TYPE_NO, I.ID AS INOTE_ID, " +
            " I.INOTE_NO, I.INOTE_DATE, ILI.TENDERED, STRING_AGG(LISN.SERIAL_NO, ', ') AS SERIAL_NUMBERS, WC.START_DATE, " +
            " WC.END_DATE, WC.APPROVAL_DATE, WT.TYPE, EMP.ENAME AS AUTHORITY_NAME,  WC.IS_DOA_APPROVED, " +
            " WC.WARRANT_TYPE, WC.ID, WC.DOA_APPROVER_ID, DOA_EMP.ENAME AS DOA_APPROVER_NAME, SLI.ORDER_TYPE_DATE, WT.ID AS WARRANT_TYPE_ID, "+
            " SLI.WARR_PERIOD_QTY, SLI.REGISTRATION_NO " +
            " FROM MDMS.PART_NUMBER P " +
            " JOIN TDMS.SO_LINE_ITEM SLI ON SLI.PART_NUMBER_ID = P.ID " +
            " JOIN TDMS.CATEGORY C ON C.SO_LINE_ITEM_ID = SLI.ID " +
            " JOIN TDMS.SUBMISSION_SLIP OL ON OL.WARRANT_ID = C.ID " +
            " JOIN TDMS.INTERNAL_INOTE I ON I.SUB_SLIP_ID = OL.ID " +
            " JOIN TDMS.LINE_ITEM_SERIAL_NO LISN ON LISN.SUB_SLIP_ID = OL.ID " +
            " JOIN TDMS.INTERNAL_INOTE_LINE_ITEM ILI ON ILI.INTERNAL_INOTE_ID = I.ID " +
            " LEFT JOIN TDMS.WARRANT_CERTIFICATE WC ON WC.INOTE_ID = I.ID " +
            " LEFT JOIN MDMS.WARRANT_TYPE WT ON WT.ID = SLI.WARR_PERIOD_TYPE " +
            " LEFT JOIN MDMS.EMPLOYEE EMP ON EMP.ID = WC.AUTHORITY_ID  " +
            " LEFT JOIN MDMS.EMPLOYEE DOA_EMP ON DOA_EMP.ID = WC.DOA_APPROVER_ID  " +
            " WHERE I.ID = :internalInoteId " +
            " GROUP BY " +
            "  P.PART_NO, P.MATERIAL_DESCRIPTION, P.DRAWING_NO, SLI.ORDER_TYPE_NO, I.ID, I.INOTE_NO, " +
            "  I.INOTE_DATE, ILI.TENDERED, WC.START_DATE, WC.END_DATE, WC.APPROVAL_DATE, WT.TYPE, EMP.ENAME, " +
            "  WC.IS_DOA_APPROVED, WC.WARRANT_TYPE, WC.ID, WC.DOA_APPROVER_ID, DOA_EMP.ENAME, SLI.ORDER_TYPE_DATE, WT.ID, SLI.WARR_PERIOD_QTY, SLI.REGISTRATION_NO ", nativeQuery = true)
	public List<Object[]> getWarrantDetailsByInternalInoteId(Integer internalInoteId);

    @Query(value = "Select * from tdms.warrant_certificate where inote_id = :iNoteId", nativeQuery = true)
    Optional<WarrantCertificate> findByINoteId(Integer iNoteId);
}
