package in.co.bel.olms.transaction.enums;

/**
 * Enum class for DeliveryType 
 * @author Awadhesh Verma
 * @since 13-07-2022
 */
public enum DeliveryTypeEnum {
 
	FULL("Full Supply", "Full"),
    PART("Part Supply", "Part"),
    FURTHER_PART("Further Part Supply", "Further Part"),
    FINAL("Final Supply", "Completion");

	private final String key;
	private final String value;

	DeliveryTypeEnum(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public static String getValueByKey(String key) {
		for (DeliveryTypeEnum enumValue : DeliveryTypeEnum.values()) {
			if (enumValue.getKey().equals(key)) {
				return enumValue.getValue();
			}
		}
		return null; // Key not found
	}
}
