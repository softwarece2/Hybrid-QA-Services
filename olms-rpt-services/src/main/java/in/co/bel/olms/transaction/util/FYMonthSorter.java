package in.co.bel.olms.transaction.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FYMonthSorter {

	private FYMonthSorter() {
		super();
	}

	public static void sortList(List<Map<String, String>> objectsToOrder, List<String> orderedObjects) {

		HashMap<String, Integer> indexMap = new HashMap<>();
		int index = 0;
		for (String month : orderedObjects) {
			indexMap.put(month, index);
			index++;
		}

		Collections.sort(objectsToOrder, (o1, o2) -> {

			Integer leftIndex = indexMap.get(o1.get("key"));
			Integer rightIndex = indexMap.get(o2.get("key"));
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
