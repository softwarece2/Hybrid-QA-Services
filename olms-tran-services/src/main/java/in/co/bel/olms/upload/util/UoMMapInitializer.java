package in.co.bel.olms.upload.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import in.co.bel.olms.master.entity.Uom;
import in.co.bel.olms.master.repository.UomRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UoMMapInitializer {

	private final UomRepository uomRepository;

	@Bean
	public Map<String, Integer> uomMap() {

		Map<String, Integer> uomMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		List<Uom> uoms = uomRepository.findAll();
		for (Uom uom : uoms) {
			uomMap.put(uom.getUnit(), uom.getId());
		}
		return uomMap;
	}

}
