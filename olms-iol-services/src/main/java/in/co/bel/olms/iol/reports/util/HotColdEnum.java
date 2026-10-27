package in.co.bel.olms.iol.reports.util;

public enum HotColdEnum {

	HOT_TEST (0), COLD_TEST (1);
	
	public final int type;

	private HotColdEnum(int type) {
		this.type = type;
	}
}
