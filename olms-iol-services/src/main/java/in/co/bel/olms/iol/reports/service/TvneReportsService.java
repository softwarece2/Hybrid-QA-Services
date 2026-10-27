package in.co.bel.olms.iol.reports.service;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.reports.dto.ReportsDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TvneReportsService {

	private final JdbcTemplate jdbcTemplate;

	public List<ReportsDto> executeColdResistanceReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}

	public List<ReportsDto> executeHeatStabilityReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}

	public List<ReportsDto> executeImpactReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}

	public List<ReportsDto> executeVibrationReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}

	public List<ReportsDto> executeLeakTestReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}

	public List<ReportsDto> executeColdTestTpdkReportQuery(String queryString) {
		return jdbcTemplate.query(queryString, new BeanPropertyRowMapper<>(ReportsDto.class));
	}
}
