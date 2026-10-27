package in.co.bel.olms.upload.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FYMonthSorter {

	private FYMonthSorter() {
		super();
	}

	public static void sortList(List<Map<String, Object>> objectsToOrder, List<String> orderedObjects) {

		HashMap<String, Integer> indexMap = new HashMap<>();
		int index = 0;
		for (String month : orderedObjects) {
			indexMap.put(month, index);
			index++;
		}

		Collections.sort(objectsToOrder, (o1, o2) -> {

			Integer leftIndex = indexMap.get(o1.get("month"));
			Integer rightIndex = indexMap.get(o2.get("month"));
			if (leftIndex == null) {
				return -1;
			}
			if (rightIndex == null) {
				return 1;
			}

			return Integer.compare(leftIndex, rightIndex);
		});
	}

}
