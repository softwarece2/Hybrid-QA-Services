package in.co.bel.olms.iol.reports.file;

public class ReportFile {
	
	private ReportFile() {
		throw new IllegalStateException("Utility class");
	}

	public static final String REPORT = "Report.xlsx";
	
	public static final String HOT_TEST_TPDK_REPORT = "HotTestTpdk.xlsx";
	
	public static final String COLD_TEST_TPDK_REPORT = "ColdTestTpdk.xlsx";
	
	public static final String COLD_RESISTANCE_TVNE_REPORT = "ColdResistanceTvne.xlsx";
	
	public static final String HEAT_STABILITY_TVNE_REPORT = "HeatStabilityTvne.xlsx";
	
	public static final String IMPACT_TVNE_REPORT = "ImpactTvne.xlsx";
	
	public static final String VIBRATION_TVNE_REPORT = "VibrationTvne.xlsx";
	
	public static final String LEAK_TEST_TVNE_REPORT = "LeakTestTvne.xlsx";
}
