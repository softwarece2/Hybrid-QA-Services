package in.co.bel.olms.iol.reports.util;

public enum ProductTypeEnum {
	
	TPDK_1 (1), TVNE_4B_M1 (2), TVNE_4B_M2 (3), TVNE_1PA (4);
	
	public final int type;

	private ProductTypeEnum(int type) {
		this.type = type;
	}
}
