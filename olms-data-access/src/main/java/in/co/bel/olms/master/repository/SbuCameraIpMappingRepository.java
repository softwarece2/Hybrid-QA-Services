package in.co.bel.olms.master.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.co.bel.olms.master.entity.SbuCameraIpMapping;


public interface SbuCameraIpMappingRepository extends JpaRepository<SbuCameraIpMapping, Integer> {
	
	@Query(value="select camera_ip, sbu_name from mdms.sbu_camera_ip_mapping where camera_ip in :ipList", nativeQuery = true)
	public List<Object[]> getByIPList(List<String> ipList);
	
	@Query(value = "select DISTINCT camera_ip, sbu_name from mdms.sbu_camera_ip_mapping m " +
            "JOIN ate.sbu_camera_test_mapping t ON m.id = t.cam_id " +
            "WHERE camera_ip in :ipList AND t.test_id = :testId", nativeQuery = true)
    public List<Object[]> getByIPListandTest(Set<String> ipList,  Integer testId);

 
    @Query(value = "select DISTINCT camera_ip, sbu_name from mdms.sbu_camera_ip_mapping m " +
            "JOIN ate.sbu_camera_test_mapping t ON m.id = t.cam_id " +
            "WHERE camera_ip in :ipList AND t.test_id in :testList", nativeQuery = true)
    public List<Object[]> getByIPListandTestList(List<String> ipList, List<Integer> testList);

	
	List<SbuCameraIpMapping> findBySalesGrpId(Integer salesGrpId);
	
	List<SbuCameraIpMapping> findByCameraIp(String CameraIp);

	public Boolean existsByCameraIp(String cameraIp);

	public Boolean existsBySbuName(String sbuName);
	
	public Boolean existsByCameraIpAndIdNot(String cameraIp, Integer id);

    public Boolean existsBySbuNameAndIdNot(String sbuName, Integer id);
}
