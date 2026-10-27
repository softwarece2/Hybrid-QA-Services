package in.co.bel.olms.iol.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import in.co.bel.olms.iol.dto.TvneDto;
public class TvneRowMapper implements RowMapper<TvneDto>{

	 @Override
	    public TvneDto mapRow(ResultSet rs, int rowNum) throws SQLException {
	        TvneDto tvne = new TvneDto();
	        tvne.setId(rs.getInt("id"));
	        tvne.setProductTypeId(rs.getInt("product_type_id"));
	        tvne.setDevName(rs.getString("dev_name"));
	        // Set other properties as needed
	        return tvne;
	    }

	
}
