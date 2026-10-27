package in.co.bel.olms.iol.reports.tests.query;

public class TvneQuery {

	private TvneQuery() {
	    throw new IllegalStateException("Utility class");
	}
	
	public static final String TVNE_QUERY = "SELECT * FROM iol.tvne AS tv LEFT JOIN iol.product_type AS pt ON tv.product_type_id = pt.id WHERE tv.product_type_id = :productTypeId";
	
	public static final String TVNE_SUF_QUERY = " GROUP BY tv.id, pt.id ORDER BY tv.id ASC";
}
