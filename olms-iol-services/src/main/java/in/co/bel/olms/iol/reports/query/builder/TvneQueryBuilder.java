package in.co.bel.olms.iol.reports.query.builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import in.co.bel.olms.iol.constants.ApplicationConstants;
import in.co.bel.olms.iol.dto.TvneDto;
import in.co.bel.olms.iol.dto.TvneSearchDto;
import in.co.bel.olms.iol.reports.util.ProductTypeEnum;
import in.co.bel.olms.iol.reports.util.TestTypeEnum;
import in.co.bel.olms.iol.service.DateUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TvneQueryBuilder {

	private final JdbcTemplate jdbcTemplate;

	Gson gson = new Gson();
	
	private static String TEST_TYPE_NOT_EXIST_MESSAGE = "The test type id does not exist.";
	private static String PRODUCT_TYPE_NOT_EXIST_MESSAGE = "The product type id does not exist.";
	private static String DEV_NAME = "dev_name";
	private static String SUBMITTED_DATE = "submitted_date";
	
	public ResponseEntity<String> populateQuery(int productTypeId, int testTypeId, Date fromDate, Date toDate){
		String res = null;
		if (productTypeId == ProductTypeEnum.TVNE_4B_M1.type) {
			if (testTypeId == TestTypeEnum.COLD_RESISTANCE.type) {
				List<TvneSearchDto> crDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(crDto);
			} else if (testTypeId == TestTypeEnum.LEAK_TEST.type) {
				List<TvneSearchDto> leakDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(leakDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else if (productTypeId == ProductTypeEnum.TVNE_4B_M2.type) {
			if (testTypeId == TestTypeEnum.COLD_RESISTANCE.type) {
				List<TvneSearchDto> crDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(crDto);
			} else if (testTypeId == TestTypeEnum.LEAK_TEST.type) {
				List<TvneSearchDto> leakDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(leakDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else if (productTypeId == ProductTypeEnum.TVNE_1PA.type) {
			if (testTypeId == TestTypeEnum.COLD_RESISTANCE.type) {
				List<TvneSearchDto> crDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(crDto);
			} else if (testTypeId == TestTypeEnum.LEAK_TEST.type) {
				List<TvneSearchDto> leakDto = getData(productTypeId, testTypeId, fromDate, toDate);
				res = gson.toJson(leakDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else {
			return ResponseEntity.badRequest().body(PRODUCT_TYPE_NOT_EXIST_MESSAGE);
		}

		return ResponseEntity.ok(res);
	}

	public ResponseEntity<String> populateQuarterQuery(int productTypeId, int testTypeId, int year){
		String res = null;
		if (productTypeId == ProductTypeEnum.TVNE_4B_M1.type) {
			if (testTypeId == TestTypeEnum.HEAT_STABILITY.type) {
				List<TvneSearchDto> hsDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(hsDto);
			} else if (testTypeId == TestTypeEnum.IMPACT.type) {
				List<TvneSearchDto> imDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(imDto);
			} else if (testTypeId == TestTypeEnum.VIBRATION.type) {
				List<TvneSearchDto> vibDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(vibDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else if (productTypeId == ProductTypeEnum.TVNE_4B_M2.type) {
			if (testTypeId == TestTypeEnum.HEAT_STABILITY.type) {
				List<TvneSearchDto> hsDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(hsDto);
			} else if (testTypeId == TestTypeEnum.IMPACT.type) {
				List<TvneSearchDto> imDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(imDto);
			} else if (testTypeId == TestTypeEnum.VIBRATION.type) {
				List<TvneSearchDto> vibDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(vibDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else if (productTypeId == ProductTypeEnum.TVNE_1PA.type) {
			if (testTypeId == TestTypeEnum.HEAT_STABILITY.type) {
				List<TvneSearchDto> hsDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(hsDto);
			} else if (testTypeId == TestTypeEnum.IMPACT.type) {
				List<TvneSearchDto> imDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(imDto);
			} else if (testTypeId == TestTypeEnum.VIBRATION.type) {
				List<TvneSearchDto> vibDto = getQuarterData(productTypeId, testTypeId, year);
				res = gson.toJson(vibDto);
			} else {
				return ResponseEntity.badRequest().body(TEST_TYPE_NOT_EXIST_MESSAGE);
			}
		} else {
			return ResponseEntity.badRequest().body(PRODUCT_TYPE_NOT_EXIST_MESSAGE);
		}
		return ResponseEntity.ok(res);
	}
	@SuppressWarnings("deprecation")
	private List<TvneDto> executeTvneQuery(Integer productTypeId) {
		RowMapper<TvneDto> rowMapper = new RowMapper<TvneDto>() {
			@Override
			public TvneDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneDto tvneDto = new TvneDto();
				tvneDto.setId(rs.getInt("id"));
				tvneDto.setDevName(rs.getString(DEV_NAME));
				tvneDto.setProductTypeId(rs.getInt("product_type_id"));
				return tvneDto;
			}
		};
		String tvneQuery = "SELECT * FROM iol.tvne AS tv LEFT JOIN iol.product_type AS pt ON tv.product_type_id = pt.id WHERE tv.product_type_id = ?";
		return jdbcTemplate.query(tvneQuery, new Object[] { productTypeId }, rowMapper);
	}
	
	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchColdResistanceByTvneId(Integer tvneId, Date fromDate, Date toDate) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}

				if (rs.getInt(ApplicationConstants.STATUS) == 1 || rs.getInt(ApplicationConstants.STATUS) == 0) {
					tvneSearchDto.setStatus(rs.getInt(ApplicationConstants.STATUS));
				} else {
					tvneSearchDto.setStatus(null);
				}
				if (rs.getString(ApplicationConstants.REMARKS) != null) {
					tvneSearchDto.setRemarks(rs.getString(ApplicationConstants.REMARKS));
				} else {
					tvneSearchDto.setRemarks(null);
				}
				return tvneSearchDto;
			}
		};
		String coldResistanceQuery = "SELECT cr.id as id, cr.submitted_date as submitted_date, tv.dev_name as dev_name, cr.status as status, cr.remarks as remarks FROM iol.cold_resistance cr LEFT JOIN iol.tvne tv ON cr.tvne_id = tv.id WHERE cr.tvne_id = ? AND cr.submitted_date BETWEEN ? AND ?";
		return jdbcTemplate.query(coldResistanceQuery, new Object[] { tvneId, fromDate, toDate }, rowMapper);
	}

	private List<TvneSearchDto> getData(int productTypeId, int testTypeId, Date fromDate, Date toDate) {
		List<TvneSearchDto> tvneSearchDataList = new ArrayList<>();
		List<TvneDto> tvneRecords = executeTvneQuery(productTypeId);

		for (TvneDto tvneRecord : tvneRecords) {
			if (tvneRecord != null && tvneRecord.getId() != null) {
				Integer tvneId = tvneRecord.getId();
				if (testTypeId == TestTypeEnum.COLD_RESISTANCE.type) {
					List<TvneSearchDto> coldResistanceData = fetchColdResistanceByTvneId(tvneId, fromDate, toDate);
					tvneSearchDataList.addAll(coldResistanceData);
				} else if (testTypeId == TestTypeEnum.LEAK_TEST.type) {
					List<TvneSearchDto> leakData = fetchLeakTestByTvneId(tvneId, fromDate, toDate);
					tvneSearchDataList.addAll(leakData);
				}
			}
		}

		return tvneSearchDataList;
	}

	
	private List<TvneSearchDto> getQuarterData(int productTypeId, int testTypeId, int year) {
		List<TvneSearchDto> tvneSearchDataList = new ArrayList<>();
		List<TvneDto> tvneRecords = executeTvneQuery(productTypeId);

		for (TvneDto tvneRecord : tvneRecords) {
			if (tvneRecord != null && tvneRecord.getId() != null) {
				Integer tvneId = tvneRecord.getId();
				if (testTypeId == TestTypeEnum.HEAT_STABILITY.type) {
					List<TvneSearchDto> heatStabilityData = fetchHeatStabilityQuarterByTvneId(tvneId, year);
					tvneSearchDataList.addAll(heatStabilityData);
				} else if (testTypeId == TestTypeEnum.IMPACT.type) {
					List<TvneSearchDto> impactData = fetchImpactQuarterByTvneId(tvneId, year);
					tvneSearchDataList.addAll(impactData);
				} else if (testTypeId == TestTypeEnum.VIBRATION.type) {
					List<TvneSearchDto> vibData = fetchVibrationQuarterByTvneId(tvneId, year);
					tvneSearchDataList.addAll(vibData);
				}
			}
		}

		return tvneSearchDataList;
	}
	
	
	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchHeatStabilityByTvneId(Integer tvneId, Date fromDate, Date toDate) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}
				return tvneSearchDto;
			}
		};
		String heatStabilityQuery = "SELECT hs.id as id, hs.submitted_date as submitted_date, tv.dev_name as dev_name FROM iol.heat_stability hs LEFT JOIN iol.tvne tv ON hs.tvne_id = tv.id WHERE hs.tvne_id = ? AND hs.submitted_date BETWEEN ? AND ?";
		return jdbcTemplate.query(heatStabilityQuery, new Object[] { tvneId, fromDate, toDate }, rowMapper);
	}
	
	
	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchHeatStabilityQuarterByTvneId(Integer tvneId, int year) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}
				return tvneSearchDto;
			}
		};
		String heatStabilityQuery = "SELECT hs.id AS id, hs.submitted_date AS submitted_date, tv.dev_name AS dev_name FROM iol.heat_stability hs LEFT JOIN iol.tvne tv ON hs.tvne_id = tv.id WHERE hs.tvne_id = ? AND EXTRACT(YEAR FROM hs.submitted_date) = ?";
		return jdbcTemplate.query(heatStabilityQuery, new Object[] { tvneId, year }, rowMapper);
	}
	
	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchImpactQuarterByTvneId(Integer tvneId, int year) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}
				return tvneSearchDto;
			}
		};
		String impactQuery = "SELECT im.id AS id, im.submitted_date AS submitted_date, tv.dev_name AS dev_name FROM iol.impact im LEFT JOIN iol.tvne tv ON im.tvne_id = tv.id WHERE im.tvne_id = ? AND EXTRACT(YEAR FROM im.submitted_date) = ?";
		return jdbcTemplate.query(impactQuery, new Object[] { tvneId, year }, rowMapper);
	}

	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchVibrationQuarterByTvneId(Integer tvneId, int year) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}
				return tvneSearchDto;
			}
		};
		String vibQuery = "SELECT vib.id AS id, vib.submitted_date AS submitted_date, tv.dev_name AS dev_name FROM iol.vibration vib LEFT JOIN iol.tvne tv ON vib.tvne_id = tv.id WHERE vib.tvne_id = ? AND EXTRACT(YEAR FROM vib.submitted_date) = ?";
		return jdbcTemplate.query(vibQuery, new Object[] { tvneId, year }, rowMapper);
	}

	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> fetchLeakTestByTvneId(Integer tvneId, Date fromDate, Date toDate) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString(DEV_NAME) != null) {
					tvneSearchDto.setDevName(rs.getString(DEV_NAME));
				} else {
					tvneSearchDto.setDevName(null);
				}

				if (rs.getInt(ApplicationConstants.STATUS) == 1 || rs.getInt(ApplicationConstants.STATUS) == 0) {
					tvneSearchDto.setStatus(rs.getInt(ApplicationConstants.STATUS));
				} else {
					tvneSearchDto.setStatus(null);
				}
				if (rs.getString(ApplicationConstants.REMARKS) != null) {
					tvneSearchDto.setRemarks(rs.getString(ApplicationConstants.REMARKS));
				} else {
					tvneSearchDto.setRemarks(null);
				}
				return tvneSearchDto;
			}
		};
		String leakQuery = "SELECT lt.id as id, lt.submitted_date as submitted_date, tv.dev_name as dev_name, lt.status as status, lt.remarks as remarks FROM iol.leak_test lt LEFT JOIN iol.tvne tv ON lt.tvne_id = tv.id WHERE lt.tvne_id = ? AND lt.submitted_date BETWEEN ? AND ?";
		return jdbcTemplate.query(leakQuery, new Object[] { tvneId, fromDate, toDate }, rowMapper);
	}

	@SuppressWarnings("deprecation")
	private List<TvneSearchDto> executehotColdTestQuery(Integer testTypeId, Date fromDate, Date toDate) {
		RowMapper<TvneSearchDto> rowMapper = new RowMapper<TvneSearchDto>() {
			@Override
			public TvneSearchDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				TvneSearchDto tvneSearchDto = new TvneSearchDto();
				tvneSearchDto.setId(rs.getInt("id"));
				if (rs.getDate(SUBMITTED_DATE) != null) {
					tvneSearchDto.setSubmittedDate(DateUtils.formatDate(rs.getDate(SUBMITTED_DATE)));
				} else {
					tvneSearchDto.setSubmittedDate(null);
				}

				if (rs.getString("device_no") != null) {
					tvneSearchDto.setDevName(rs.getString("device_no"));
				} else {
					tvneSearchDto.setDevName(null);
				}

				if (rs.getInt(ApplicationConstants.STATUS) == 1 || rs.getInt(ApplicationConstants.STATUS) == 0) {
					tvneSearchDto.setStatus(rs.getInt(ApplicationConstants.STATUS));
				} else {
					tvneSearchDto.setStatus(null);
				}
				if (rs.getString(ApplicationConstants.REMARKS) != null) {
					tvneSearchDto.setRemarks(rs.getString(ApplicationConstants.REMARKS));
				} else {
					tvneSearchDto.setRemarks(null);
				}
				return tvneSearchDto;
			}
		};
		String hotColdTestQuery = "SELECT * FROM iol.cold_test AS ct LEFT JOIN iol.hot_cold_test_type AS ht ON ct.hot_cold = ht.id WHERE ct.hot_cold = ? AND ct.submitted_date BETWEEN ? AND ? ORDER BY ct.id ASC";
		return jdbcTemplate.query(hotColdTestQuery, new Object[] { testTypeId, fromDate, toDate }, rowMapper);
	}
}
