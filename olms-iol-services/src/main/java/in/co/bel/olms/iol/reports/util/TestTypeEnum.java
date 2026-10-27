package in.co.bel.olms.iol.reports.util;

public enum TestTypeEnum {

	COLD_RESISTANCE (1), HEAT_STABILITY (2), IMPACT (3), VIBRATION (4), LEAK_TEST (5) ;
	
	public final int type;

	private TestTypeEnum(int type) {
		this.type = type;
	}
	
}
