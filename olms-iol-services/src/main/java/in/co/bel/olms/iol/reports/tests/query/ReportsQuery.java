package in.co.bel.olms.iol.reports.tests.query;

public class ReportsQuery {

	private ReportsQuery() {
		throw new IllegalStateException("Utility class");
	}

	public static final String COLD_RESISTANCE_QUERY = "SELECT cr.id AS cr_id, ROW_NUMBER() OVER (ORDER BY cr.id) AS se_no, TO_CHAR(cr.created_datetime, 'DD-MM-YYYY') AS created_date, tv.dev_name AS dev_name, TO_CHAR(cr.submitted_date, 'DD-MM-YYYY') AS tested_dt, CASE WHEN cr.status = 0 THEN 'Rejected' WHEN cr.status = 1 THEN 'Accepted' END AS status, cr.remarks AS remarks FROM iol.cold_resistance AS cr LEFT JOIN iol.tvne AS tv ON cr.tvne_id = tv.id where ";

	public static final String COLD_RESISTANCE_SUF_QUERY = "GROUP BY cr.id, TO_CHAR(cr.created_datetime, 'DD-MM-YYYY'), tv.dev_name, TO_CHAR(cr.submitted_date, 'DD-MM-YYYY'), cr.status, cr.remarks ORDER BY se_no ASC";

	public static final String HEAT_STABILITY_QUERY = "SELECT hs.id AS hs_id, ROW_NUMBER() OVER (ORDER BY hs.id) AS se_no, TO_CHAR(hs.created_datetime, 'DD-MM-YYYY') AS created_date, tv.dev_name AS dev_name, TO_CHAR(hs.submitted_date, 'DD-MM-YYYY') AS tested_dt FROM iol.heat_stability AS hs LEFT JOIN iol.tvne AS tv ON hs.tvne_id = tv.id WHERE ";

	public static final String HEAT_STABILITY_SUF_QUERY = "GROUP BY hs.id, TO_CHAR(hs.created_datetime, 'DD-MM-YYYY'), tv.dev_name, TO_CHAR(hs.submitted_date, 'DD-MM-YYYY') ORDER BY created_date ASC ";

	public static final String IMPACT_QUERY = "SELECT im.id AS im_id, ROW_NUMBER() OVER (ORDER BY im.id) AS se_no, TO_CHAR(im.created_datetime, 'DD-MM-YYYY') AS created_date, tv.dev_name AS dev_name, TO_CHAR(im.submitted_date, 'DD-MM-YYYY') AS tested_dt FROM iol.impact AS im LEFT JOIN iol.tvne AS tv ON im.tvne_id = tv.id WHERE ";

	public static final String IMPACT_SUF_QUERY = "GROUP BY im.id, TO_CHAR(im.created_datetime, 'DD-MM-YYYY'), tv.dev_name, TO_CHAR(im.submitted_date, 'DD-MM-YYYY') ORDER BY created_date ASC";
	
	public static final String VIBRATION_QUERY = "SELECT vib.id AS im_id, ROW_NUMBER() OVER (ORDER BY vib.id) AS se_no, TO_CHAR(vib.created_datetime, 'DD-MM-YYYY') AS created_date, tv.dev_name AS dev_name, TO_CHAR(vib.submitted_date, 'DD-MM-YYYY') AS tested_dt FROM iol.vibration AS vib LEFT JOIN iol.tvne AS tv ON vib.tvne_id = tv.id WHERE ";

	public static final String VIBRATION_SUF_QUERY = "GROUP BY vib.id, TO_CHAR(vib.created_datetime, 'DD-MM-YYYY'), tv.dev_name, TO_CHAR(vib.submitted_date, 'DD-MM-YYYY') ORDER BY se_no ASC";
	
	public static final String LEAK_TEST_QUERY = "SELECT lt.id AS lt_id, ROW_NUMBER() OVER (ORDER BY lt.id) AS se_no, TO_CHAR(lt.created_datetime, 'DD-MM-YYYY') AS created_date, tv.dev_name AS dev_name, TO_CHAR(lt.submitted_date, 'DD-MM-YYYY') AS tested_dt, CASE WHEN lt.status = 0 THEN 'Rejected' WHEN lt.status = 1 THEN 'Accepted' END AS status, lt.remarks AS remarks FROM iol.leak_test AS lt LEFT JOIN iol.tvne AS tv ON lt.tvne_id = tv.id WHERE ";

	public static final String LEAK_TEST_SUF_QUERY = "GROUP BY lt.id, TO_CHAR(lt.created_datetime, 'DD-MM-YYYY'), tv.dev_name, TO_CHAR(lt.submitted_date, 'DD-MM-YYYY'), lt.status, lt.remarks ORDER BY se_no ASC";

	public static final String HOT_COLD_QUERY = "SELECT ct.id AS ct_id, ROW_NUMBER() OVER (ORDER BY ct.id) AS se_no, TO_CHAR(ct.created_datetime, 'DD-MM-YYYY') AS created_date, ct.device_no AS dev_name, TO_CHAR(ct.submitted_date, 'DD-MM-YYYY') AS tested_dt, CASE WHEN ct.status = 0 THEN 'Rejected' WHEN ct.status = 1 THEN 'Accepted' END AS status, ct.remarks AS remarks FROM iol.cold_test AS ct WHERE ";
	
	public static final String HOT_COLD_SUF_QUERY = "GROUP BY ct.id, TO_CHAR(ct.created_datetime, 'DD-MM-YYYY'), ct.device_no, TO_CHAR(ct.submitted_date, 'DD-MM-YYYY'), ct.status, ct.remarks ORDER BY se_no ASC";
}
