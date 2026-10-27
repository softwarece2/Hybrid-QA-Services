package in.co.bel.olms.iol.reports.query.builder;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

import in.co.bel.olms.iol.reports.dto.ReportRequest;
import in.co.bel.olms.iol.reports.tests.query.ReportsQuery;
import in.co.bel.olms.iol.reports.util.HotColdEnum;
import in.co.bel.olms.iol.reports.util.ProductTypeEnum;
import in.co.bel.olms.iol.reports.util.TestTypeEnum;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportQueryBuilder {

	public String populateQuery(ReportRequest reportRequest) throws IOException, ParseException {
		int productId = reportRequest.getProductTypeId();
		int testId = reportRequest.getTestTypeId();
		String suffixQuery = null;
		StringBuilder finalQuery = null;
		if (productId == ProductTypeEnum.TPDK_1.type) {
			if (testId == HotColdEnum.HOT_TEST.type) {
				finalQuery = appendDateTimeQueryTpdk(reportRequest);
			} else if  (testId == HotColdEnum.COLD_TEST.type) {
				finalQuery = appendDateTimeQueryTpdk(reportRequest);
			}
		} else if (productId == ProductTypeEnum.TVNE_4B_M1.type) {
			if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.IMPACT.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.VIBRATION.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.LEAK_TEST.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			}
		} else if (productId == ProductTypeEnum.TVNE_4B_M2.type) {
			if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.IMPACT.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.VIBRATION.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.LEAK_TEST.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			}
		} else if (productId == ProductTypeEnum.TVNE_1PA.type) {
			if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.IMPACT.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.VIBRATION.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			} else if (testId == TestTypeEnum.LEAK_TEST.type) {
				finalQuery = appendDateTimeQueryTvne(reportRequest);
			}
		}

		if (productId == ProductTypeEnum.TVNE_4B_M1.type || productId == ProductTypeEnum.TVNE_4B_M2.type || productId == ProductTypeEnum.TVNE_1PA.type) {
		   suffixQuery = getSuffixQueryTvne(reportRequest);
		} else if (productId == ProductTypeEnum.TPDK_1.type) {
			suffixQuery = getSuffixQueryTpdk(reportRequest);
		}
		finalQuery.append(suffixQuery);

		return finalQuery.toString();
	}

	private String getSuffixQueryTpdk(ReportRequest reportRequest) {
		long testId = reportRequest.getTestTypeId();
		String retVal = null;
		if (testId == HotColdEnum.HOT_TEST.type) {
			retVal = ReportsQuery.HOT_COLD_SUF_QUERY;
		} else if (testId == HotColdEnum.COLD_TEST.type) {
			retVal = ReportsQuery.HOT_COLD_SUF_QUERY;
		}
		return retVal;
	}

	private String getSuffixQueryTvne(ReportRequest reportRequest) {
		long testId = reportRequest.getTestTypeId();
		String retVal = null;
		if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			retVal = ReportsQuery.COLD_RESISTANCE_SUF_QUERY;
		} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
			retVal = ReportsQuery.HEAT_STABILITY_SUF_QUERY;
		} else if (testId == TestTypeEnum.IMPACT.type) {
			retVal = ReportsQuery.IMPACT_SUF_QUERY;
		} else if (testId == TestTypeEnum.VIBRATION.type) {
			retVal = ReportsQuery.VIBRATION_SUF_QUERY;
		} else if (testId == TestTypeEnum.LEAK_TEST.type) {
			retVal = ReportsQuery.LEAK_TEST_SUF_QUERY;
		}
		return retVal;
	}

	private StringBuilder appendDateTimeQueryTvne(ReportRequest reportRequest) {
		StringBuilder finalQuery = new StringBuilder();
		String dateString = null;
		int testId = reportRequest.getTestTypeId();
		String dateSubString1 = "tv.product_type_id = " + reportRequest.getProductTypeId();
		String dateSubString2 = reportRequest.getFromDate() + "' AND '" + reportRequest.getToDate() + "'";
		if (testId == TestTypeEnum.COLD_RESISTANCE.type) {
			finalQuery.append(ReportsQuery.COLD_RESISTANCE_QUERY);
			dateString = dateSubString1 + " AND cr.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		} else if (testId == TestTypeEnum.HEAT_STABILITY.type) {
			finalQuery.append(ReportsQuery.HEAT_STABILITY_QUERY);
			dateString = dateSubString1 + " AND hs.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		} else if (testId == TestTypeEnum.IMPACT.type) {
			finalQuery.append(ReportsQuery.IMPACT_QUERY);
			dateString = dateSubString1 + " AND im.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		} else if (testId == TestTypeEnum.VIBRATION.type) {
			finalQuery.append(ReportsQuery.VIBRATION_QUERY);
			dateString = dateSubString1 + " AND vib.created_datetime BETWEEN '"	+ dateSubString2;
			finalQuery.append(dateString);
		} else if (testId == TestTypeEnum.LEAK_TEST.type) {
			finalQuery.append(ReportsQuery.LEAK_TEST_QUERY);
			dateString = dateSubString1 + " AND lt.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		}
		return finalQuery;
	}
	
	
	private StringBuilder appendDateTimeQueryTpdk(ReportRequest reportRequest) {
		StringBuilder finalQuery = new StringBuilder();
		String dateString = null;
		int testId = reportRequest.getTestTypeId();
		String dateSubString2 = reportRequest.getFromDate() + "' AND '" + reportRequest.getToDate() + "'";
		if (testId == HotColdEnum.HOT_TEST.type) {
			finalQuery.append(ReportsQuery.HOT_COLD_QUERY);
			dateString = "ct.hot_cold = " + testId + " AND ct.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		} else if (testId == HotColdEnum.COLD_TEST.type) {
			finalQuery.append(ReportsQuery.HOT_COLD_QUERY);
			dateString = "ct.hot_cold = " + testId + " AND ct.created_datetime BETWEEN '" + dateSubString2;
			finalQuery.append(dateString);
		}
		return finalQuery;
	}

}
