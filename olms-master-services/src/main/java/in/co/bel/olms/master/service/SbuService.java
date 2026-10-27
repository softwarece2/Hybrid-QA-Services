package in.co.bel.olms.master.service;

import java.util.Date;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.dao.OrganizationDao;
import in.co.bel.olms.master.dao.SbuDao;
import in.co.bel.olms.master.dto.SbuDto;
import in.co.bel.olms.master.entity.Organization;
import in.co.bel.olms.master.entity.Sbu;
import in.co.bel.olms.master.repository.SbuRepository;
import lombok.RequiredArgsConstructor;
/**
 * 
 * @author Manasa B
 * @since 08-12-2022
 */

@Service
@RequiredArgsConstructor
public class SbuService {

	private final SbuRepository sbuRepository;

	private final Mapper mapper;

	private final OrganizationDao organizationDao;

	private final SbuDao sbuDao;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public Sbu getSbuById(int id) {
		return sbuDao.getSbuById(id);
	}
	
	public List<Sbu> getAllSbu() {
		return sbuRepository.findAll();
	}
	
	/**
	 * 
	 * @param sbuDto
	 * @return
	 */
	public Sbu saveSbu(SbuDto sbuDto) {
		Sbu sbu = mapper.map(sbuDto, Sbu.class);
		Organization org=	organizationDao.getOrganizationByOrgId(sbuDto.getOrgId());
		sbu.setOrganization(org);		
		sbu.setActive(Boolean.TRUE);
		sbu.setCreatedDatetime(new Date());
		return sbuRepository.save(sbu);
	}

	
	/**
	 * 
	 * @param sbuDto
	 * @return
	 */
	public Sbu updateSbu(SbuDto sbuDto) {		
		Sbu sbu =  sbuDao.getSbuById(sbuDto.getId());
		sbu.setSbuCode(sbuDto.getSbuCode());
		sbu.setSbuName(sbu.getSbuName());
		sbu.setActive(sbuDto.isActive());
		sbu.setModifiedDatetime(new Date());
		sbu.setModifiedBy(sbuDto.getModifiedBy());
		return sbuRepository.save(sbu);
	}
}
