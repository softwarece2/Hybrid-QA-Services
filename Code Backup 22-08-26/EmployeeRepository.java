package in.co.bel.olms.master.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.Employee;


/**
 * 
 * @author Manasa B, Awadhesh Verma
 * @since 22-02-2023
 *
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	@Query(value = "SELECT * FROM mdms.employee where emp_role_id =:roleId ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getEmpByRole(@Param("roleId") int roleId);

	
	@Query(value = "SELECT * FROM mdms.employee where emp_role_id in :roleIds ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getEmpByRole(@Param("roleIds") List<Integer> roleIds);
	
	@Query(value = "SELECT * FROM mdms.employee where emp_role_id =:roleId and organization_id=:orgId ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getEmpByOrgId(@Param("roleId") int roleId, @Param("orgId") int orgId);
	
	
//	@Query(value = "SELECT * FROM mdms.employee where emp_role_id in :roleIds and organization_id=:orgId ORDER BY id ASC ", nativeQuery = true)
//	List<Employee> getEmpByOrgId(@Param("roleIds") List<Integer> roleIds, @Param("orgId") int orgId);
	
	@Query(value = "SELECT * FROM mdms.employee where organization_id=:orgId ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getEmpByOrgId(@Param("orgId") int orgId);
	
	@Query(value = "SELECT id FROM mdms.employee where organization_id IN :orgIds ORDER BY id ASC ", nativeQuery = true)
	List<Integer> getEmpIDByOrgIds( @Param("orgIds") List<Integer> orgIds);
	
	
	
	@Query(value = "SELECT SG.ID FROM MDMS.SALES_GROUP SG LEFT JOIN MDMS.EMPLOYEE E ON E.SBU_ID = SG.ID WHERE E.ID = :empId LIMIT 1", nativeQuery = true)
	Integer getSalesGrpByEmpId(@Param("empId") int empId);
	
	
	/**	 
	 * Get immediate subordinate employees
	 * @author Manasa B	 
	 * @param empId
	 * @return
	 */
	@Query(value = "SELECT * FROM mdms.employee where reporting_officer_id=:empId ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getSubByEmp( @Param("empId") int empId);
	
	/**
	 * Gets available subordinates for an employee, excluding previously allotted staff.
	 * 
	 * @param empId ID of the supervisor.
	 * @param allotedToIds List of employee IDs to exclude from the results.
	 * @return List of subordinate employees.
	 */
	@Query(value = "SELECT * FROM mdms.employee where reporting_officer_id=:empId and id not in (:allotedToIds) ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getAvailableSubordinates( @Param("empId") Integer empId, @Param("allotedToIds") List<Integer> allotedToIds);
	
	/**	 
	 * Get immediate subordinate employees
	 * @author Manasa B	 
	 * @param empId
	 * @return
	 */
	@Query(value = "SELECT * FROM mdms.employee where reporting_officer_id=:empId and emp_role_id= :roleId  ORDER BY id ASC ", nativeQuery = true)
	List<Employee> getSubByEmpBySubRole( @Param("empId") int empId, @Param("roleId") int roleId );
	
	
	@Query(value = "SELECT ER.ID FROM MDMS.EMPLOYEE E LEFT JOIN MDMS.EMP_ROLE ER ON E.EMP_ROLE_ID = ER.ID WHERE E.ID= :empId", nativeQuery = true)
	Integer getEmpRoleByEmpId(@Param("empId") int empId);
	
	@Query(value = "WITH RECURSIVE ORG_QUERY AS (SELECT ID,1 AS EMP_LEVEL, array[ID] AS ORG_PATH,EMP_ROLE_ID AS ROLE_ID,"
			+ " ENAME AS ENAME FROM MDMS.EMPLOYEE WHERE REPORTING_OFFICER_ID = :empId AND ORGANIZATION_ID = :orgId UNION ALL SELECT E.ID,"
			+ " Q.EMP_LEVEL + 1,Q.ORG_PATH || E.ID,E.EMP_ROLE_ID AS ROLE_ID, E.ENAME AS ENAME FROM MDMS.EMPLOYEE E JOIN ORG_QUERY Q "
			+ "ON Q.ID = E.REPORTING_OFFICER_ID) SELECT O.ID, O.EMP_LEVEL,O.ROLE_ID, O.ENAME, R.EMP_ROLE FROM ORG_QUERY O INNER JOIN"
			+ " MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID ORDER BY ORG_PATH", nativeQuery = true)
	List<Object[]> getAllSubordinates(@Param("orgId") int orgId, @Param("empId") int empId);
	
	@Query(value = "SELECT ID FROM MDMS.EMPLOYEE WHERE ORGANIZATION_ID= :orgId AND EMP_ROLE_ID IN (2,4)", nativeQuery = true)
	List<Integer> getJCnAdminIdsByOrgId(@Param("orgId") int orgId);
	
	@Query(value="WITH RECURSIVE ORG_QUERY AS "
			+ "                                (SELECT ID, "
			+ "                                                1 AS EMP_LEVEL, array[ID] AS ORG_PATH, "
			+ "                                                EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                lpad(ENAME, 50, '') AS ENAME "
			+ "                                        FROM MDMS.EMPLOYEE "
			+ "                                        WHERE REPORTING_OFFICER_ID = :empId "
			+ "                                        UNION ALL SELECT E.ID, "
			+ "                                                Q.EMP_LEVEL + 1, "
			+ "                                                Q.ORG_PATH || E.ID, "
			+ "                                                E.EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                E.ENAME AS ENAME "
			+ "                                        FROM MDMS.EMPLOYEE E "
			+ "                                        JOIN ORG_QUERY Q ON Q.ID = E.REPORTING_OFFICER_ID) "
			+ "SELECT O.ID, "
			+ "        O.EMP_LEVEL, "
			+ "        O.ROLE_ID, "
			+ "        O.ENAME, "
			+ "        R.EMP_ROLE "
			+ "FROM ORG_QUERY O "
			+ "INNER JOIN MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID "
			+ "WHERE (:roleId = -1 OR O.ROLE_ID = :roleId) "
			+ "ORDER BY ORG_PATH "
			+ "", nativeQuery=true)
	List<Object[]> getAllSubOfEmpByRoleId(int empId, int roleId);
	
	@Query(value="WITH RECURSIVE ORG_QUERY AS "
			+ "                                (SELECT ID, "
			+ "                                                1 AS EMP_LEVEL, array[ID] AS ORG_PATH, "
			+ "                                                EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                 ENAME, "
			+ "								                REPORTING_OFFICER_ID "
			+ "                                        FROM MDMS.EMPLOYEE "
			+ "                                        WHERE ID = :empId "
			+ "                                        UNION ALL SELECT E.ID, "
			+ "                                                Q.EMP_LEVEL + 1, "
			+ "                                                Q.ORG_PATH || E.ID, "
			+ "                                                E.EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                E.ENAME AS ENAME, "
			+ "								                E.REPORTING_OFFICER_ID "
			+ "                                        FROM MDMS.EMPLOYEE E "
			+ "                                        JOIN ORG_QUERY Q ON Q.REPORTING_OFFICER_ID = E.ID) "
			+ "SELECT O.ID, "
			+ "        O.EMP_LEVEL, "
			+ "        O.ROLE_ID, "
			+ "        O.ENAME, "
			+ "        R.EMP_ROLE "
			+ "FROM ORG_QUERY O "
			+ "INNER JOIN MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID "
			+ "WHERE (:roleId = -1 OR O.ROLE_ID = :roleId) "
			+ "AND O.ID != :empId "
			+ "ORDER BY ORG_PATH", nativeQuery=true)
	List<Object[]> getAllParentOfEmpByRoleId(int empId, int roleId);
	
	@Query(value="WITH RECURSIVE ORG_QUERY AS "
			+ "                                (SELECT ID, "
			+ "                                                1 AS EMP_LEVEL, array[ID] AS ORG_PATH, "
			+ "                                                EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                 ENAME, "
			+ "								                REPORTING_OFFICER_ID "
			+ "                                        FROM MDMS.EMPLOYEE "
			+ "                                        WHERE ID = :empId "
			+ "                                        UNION ALL SELECT E.ID, "
			+ "                                                Q.EMP_LEVEL + 1, "
			+ "                                                Q.ORG_PATH || E.ID, "
			+ "                                                E.EMP_ROLE_ID AS ROLE_ID, "
			+ "                                                E.ENAME AS ENAME, "
			+ "								                E.REPORTING_OFFICER_ID "
			+ "                                        FROM MDMS.EMPLOYEE E "
			+ "                                        JOIN ORG_QUERY Q ON Q.REPORTING_OFFICER_ID = E.ID) "
			+ "SELECT O.ID, "
			+ "        O.EMP_LEVEL, "
			+ "        O.ROLE_ID, "
			+ "        O.ENAME, "
			+ "        R.EMP_ROLE "
			+ "FROM ORG_QUERY O "
			+ "INNER JOIN MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID "
			+ "WHERE (:roleId = -1 OR O.ROLE_ID = :roleId) "
			+ "ORDER BY ORG_PATH", nativeQuery=true)
	List<Object[]> getAllParentIncCurrentEmpOfEmpByRoleId(int empId, int roleId);
	
	@Query(value="WITH RECURSIVE ORG_QUERY AS "
			+ "                                (SELECT ID, "
			+ "                                        1 AS EMP_LEVEL, array[ID] AS ORG_PATH, "
			+ "                                        EMP_ROLE_ID AS ROLE_ID, "
			+ "                                        ENAME, "
			+ "                                        REPORTING_OFFICER_ID "
			+ "                                 FROM MDMS.EMPLOYEE "
			+ "                                 WHERE ID = :empId "
			+ "                              UNION ALL SELECT E.ID, "
			+ "                                        Q.EMP_LEVEL + 1, "
			+ "                                        Q.ORG_PATH || E.ID, "
			+ "                                        E.EMP_ROLE_ID AS ROLE_ID, "
			+ "                                        E.ENAME AS ENAME, "
			+ "                                        E.REPORTING_OFFICER_ID "
			+ "                                   FROM MDMS.EMPLOYEE E "
			+ "                                   JOIN ORG_QUERY Q ON Q.REPORTING_OFFICER_ID = E.ID) "
			+ "SELECT O.ID, "
			+ "        O.EMP_LEVEL, "
			+ "        O.ROLE_ID, "
			+ "        O.ENAME, "
			+ "        R.EMP_ROLE "
			+ "FROM ORG_QUERY O "
			+ "INNER JOIN MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID "
			+ "WHERE O.ROLE_ID NOT IN(2, 3) "
			+ "AND O.ID != :empId "
			+ "ORDER BY ORG_PATH", nativeQuery=true)
	List<Object[]> getParentTillJcByEmpAndRole(int empId);

	@Query(value="SELECT * FROM MDMS.EMPLOYEE E WHERE E.ORGANIZATION_ID = :orgId AND E.EMP_ROLE_ID IN (3,4,5,6)", nativeQuery=true)
	List<Employee> getEmpForInote(int orgId);
	
	@Query(value="SELECT * FROM MDMS.EMPLOYEE E WHERE E.ORGANIZATION_ID = :orgId AND E.EMP_ROLE_ID IN (2,3,4) and e.active = true", nativeQuery=true)
	List<Employee> getActiveEmpForInote(int orgId);
	
	@Query(value="select seal_no from mdms.employee e where id = :empId limit 1", nativeQuery=true)
	String getInspSealNoByEmpId(int empId);
	
	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
			+ " left join mdms.emp_role er on er.id =em.emp_role_id "
			+ " where  er.seniority < :seniorityId", nativeQuery=true)
	List<Object[]> getEmpBySeniority(int seniorityId);
	
	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
			+ " left join mdms.emp_role er on er.id =em.emp_role_id "
			+ " where  er.seniority < :seniorityId and em.active = true", nativeQuery=true)
	List<Object[]> getActiveEmpBySeniority(int seniorityId);
	
	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
			+ " left join mdms.emp_role er on er.id =em.emp_role_id "
			+ " where  er.seniority < :seniorityId and em.department_id = :deptId and em.active = true", nativeQuery=true)
	List<Object[]> getActiveEmpBySeniorityofDeptId(int seniorityId, int deptId);

	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em \r\n"
			+ "			  left join mdms.emp_role er on er.id =em.emp_role_id \r\n"
			+ "			  left join mdms.department dept on dept.id= em.department_id\r\n"
			+ "			  where em.emp_role_id=14 and  dept.department_type =2 and em.active = true", nativeQuery=true)
	List<Object[]>getHosaList();
	
	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
			+ " left join mdms.emp_role er on er.id =em.emp_role_id "
			+ " where  er.seniority > :seniorityId and em.active = true", nativeQuery=true)
	List<Object[]> getActiveEmpBySubOrdinate(int seniorityId);
	
	@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
			+ " left join mdms.emp_role er on er.id =em.emp_role_id "
			+ " where  er.seniority > :seniorityId and em.department_id = :deptId and em.active = true", nativeQuery=true)
	List<Object[]> getActiveEmpBySubOrdinateofDeptId(int seniorityId, int deptId);
	
	@Query(value="WITH RECURSIVE ORG_QUERY AS "
			+ "                                (SELECT ID, "
			+ "                                        1 AS EMP_LEVEL, array[ID] AS ORG_PATH, "
			+ "                                        EMP_ROLE_ID AS ROLE_ID, "
			+ "                                        ENAME, "
			+ "                                        REPORTING_OFFICER_ID "
			+ "                                 FROM MDMS.EMPLOYEE "
			+ "                                 WHERE ID = :empId "
			+ "                              UNION ALL SELECT E.ID, "
			+ "                                        Q.EMP_LEVEL + 1, "
			+ "                                        Q.ORG_PATH || E.ID, "
			+ "                                        E.EMP_ROLE_ID AS ROLE_ID, "
			+ "                                        E.ENAME AS ENAME, "
			+ "                                        E.REPORTING_OFFICER_ID "
			+ "                                   FROM MDMS.EMPLOYEE E "
			+ "                                   JOIN ORG_QUERY Q ON Q.REPORTING_OFFICER_ID = E.ID) "
			+ "SELECT O.ID  "
			+ "FROM ORG_QUERY O "
			+ "INNER JOIN MDMS.EMP_ROLE R ON O.ROLE_ID = R.ID "
			+ "WHERE O.ROLE_ID NOT IN( 3) "
			+ "AND O.ID != :empId "
			+ "ORDER BY ORG_PATH", nativeQuery=true)
	List<Integer> getParentByEmpId(int empId);
	
	@Query(value = "select id from mdms.employee e where sbu_id=:sbuId", nativeQuery = true)
	List<Integer> getEmpIdsBySalesGrpId(@Param("sbuId") int sbuId);
	

	@Query(value ="	select e.id,e.ename  from tdms.so_allotment sa "
			+ "	left join mdms.employee e on sa.alloted_to = e.id "
			+ "	where sa.supply_order_id = :soId and e.emp_role_id =7 " , nativeQuery = true)
	Integer getEmpIdBysoId(@Param("soId") int soId);
	
	@Query(value =	"select e.id,e.ename  from tdms.ol_allotment oa "
			+ " left join mdms.employee e on oa.alloted_to = e.id "
      + " where oa.offer_letter_id = :olId and e.emp_role_id =7", nativeQuery = true)
	Integer getEmpIdByolId(@Param("olId") int olId);
	//getAllEmpsByOrgId
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN', 'CQAE_ADMIN', 'CQAO')) and e.id != :empId", nativeQuery=true)
	List<Object[]> getAllEmpsByOrgId(int empId, int orgId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN', 'CQAE_ADMIN', 'CQAO')) and e.active = true and e.id != :empId", nativeQuery=true)
	List<Object[]> getAllActiveEmpsByOrgId(int empId, int orgId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN', 'CQAE_ADMIN', 'CQAO')) "
			+ "and e.active = true and e.id != :empId and e.id NOT IN (:allotedToIds)", nativeQuery=true)
	List<Object[]> getAllAvailableActiveEmpsByOrgId(int empId, int orgId, @Param("allotedToIds") List<Integer> allotedToIds);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN', 'CQAE_ADMIN')) and e.id != :empId", nativeQuery=true)
	List<Object[]> getAllEmpsOfOrgn(int empId, int orgId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN', 'CQAE_ADMIN')) ", nativeQuery=true)
	List<Object[]> getAllEmpsOfOrg( int orgId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "			 left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "			 where organization_id = :orgId and "
			+ "			 emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN')) and e.active = true and e.id != :empId", nativeQuery = true)
	List<Object[]> getAllActiveEmpsOfOrgn(int empId, int orgId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN')) and e.active = true ", nativeQuery=true)
	List<Object[]> getAllActiveEmpsOfOrg( int orgId);
	
	@Query(value = "SELECT id, ename FROM mdms.employee where id IN :empIds ORDER BY id ASC ", nativeQuery = true)
	List<Object[]> getEmpIDNameByEmpIds(Set<Integer> empIds);
			
	@Query(value = "SELECT * FROM mdms.employee as emp where emp.id =:roleId ORDER BY id ASC", nativeQuery = true)
	Employee getEmpByRoleId(@Param("roleId") int roleId);
	
	@Query(value="select e.id,ename,emp_role_id,er.emp_role from mdms.employee e "
			+ "left join  mdms.emp_role er on er.id = e.emp_role_id "
			+ "where organization_id = :orgId and "
			+ "emp_role_id not in (SELECT id FROM mdms.emp_role WHERE sec_emp_role_id IN ('SUPER_ADMIN'))", nativeQuery=true)
	List<Object[]> getAllEmpsOfOrgn(int orgId);

	@Query(value="select id,organization_name, active from mdms.organization o where o.organization_type_id =:id", nativeQuery = true)
	List<Object[]> findByorgId(@Param("id") int id);

	@Query(value = "SELECT u.username, u.id AS userID, u.emp_id, e.ename, e.sbu_id " +
            "FROM mdms.user u " +
            "JOIN mdms.employee e ON u.emp_id = e.id " +
            "WHERE e.sbu_id = :salesGrpId " 
    ,nativeQuery = true)
List<Object[]> getAdminsBySalesgrpId(@Param("salesGrpId") int salesGrpId);

@Query(value="select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
		+ "left join mdms.emp_role er on er.id = em.emp_role_id "
		+ "where er.id = 14 and em.active = true", nativeQuery=true)
	List<Object[]> getAllActiveHOD();
	
@Query(value = " select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em "
		     + "	left join mdms.emp_role er on er.id =em.emp_role_id "
		     + "	where  er.seniority < :seniorityId and er.organization_type = :orgTypeId and em.active = true", nativeQuery = true)

List<Object[]> getAllActiveEmpsForOrgType(int seniorityId, int orgTypeId);

@Query(value = "select emp.id, emp.ename from mdms.employee emp "
			+ "left join mdms.department dept on emp.department_id = dept.id "
			+ "left join mdms.department_type dtype on dept.department_type = dtype.id "
			+ "where emp.emp_role_id = 14 and dtype.id = :deptTypeId ", nativeQuery = true)
List<Object[]> getAllHoS(int deptTypeId);

/**
 * Retrieves available HoS excluding those already assigned to allotments.
 *
 * @param deptTypeId The ID of the department type.
 * @param allotedToIds List of employee IDs to be excluded.
 * @return A list of available employees.
 */
@Query(value = "SELECT emp.id as id, emp.ename as ename FROM mdms.employee emp " 
          + "LEFT JOIN mdms.department dept ON emp.department_id = dept.id " 
          + "LEFT JOIN mdms.department_type dtype ON dept.department_type = dtype.id " 
          + "WHERE emp.emp_role_id = 14 AND dtype.id = :deptTypeId " 
          + "AND emp.id NOT IN (:allotedToIds)", nativeQuery = true)
List<Object[]> findAvailableHoSByDeptType(@Param("deptTypeId") int deptTypeId, @Param("allotedToIds") List<Integer> allotedToIds);

@Query(value = "select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em"
		+ "			 left join mdms.emp_role er on er.id =em.emp_role_id "
		+ "			where  er.seniority > :seniorityId and em.department_id = :deptId and em.active = true AND em.id NOT IN (:allotedToIds)", nativeQuery = true)
List<Object[]> findAvailableHoSByDeptType1(int seniorityId,@Param("deptId") int deptId, @Param("allotedToIds") List<Integer> allotedToIds);


  @Query(value = " SELECT e.id, e.ename FROM mdms.employee e "
      + " JOIN mdms.organization o ON e.organization_id = o.id  "
      + "	left join mdms.emp_role er on e.emp_role_id = er.id  "
      + " WHERE o.organization_type_id = 1 and er.seniority < 6  ", nativeQuery = true)
List<Object[]> getEmpsForQA();

  @Query(value = " SELECT e.id, e.ename FROM mdms.employee e "
      + " JOIN mdms.organization o ON e.organization_id = o.id  "
      + "	left join mdms.emp_role er on e.emp_role_id = er.id  "
      + " WHERE o.organization_type_id = 1 and er.seniority < 6 and e.id not in (:allotedToIds) ORDER BY e.id ASC ", nativeQuery = true)
  List<Object[]> getAvailableQaEmps(@Param("allotedToIds") List<Integer> allotedToIds);

//    @Query("SELECT e FROM Employee e WHERE e.id IN (:qaRecievers)")
//    List<Employee> findByQaRecievers(@Param("qaRecievers")List<Integer> qaRecievers);
    
	@Query(value = "select em.id,em.ename,em.ename_hindi,er.emp_role from mdms.employee em " +
			"left join mdms.emp_role er on er.id =em.emp_role_id " +
			"left join mdms.organization org on em.organization_id = org.id " +
			"where org.organization_type_id = 1 and em.emp_role_id in (2,3,4)", nativeQuery = true)
	List<Object[]> getDcAcDacForQA();

    @Query("SELECT e FROM Employee e WHERE e.id IN (:allottedToIdList)")
    List<Employee> findByQaRecievers(@Param("allottedToIdList")List<Integer> allottedToIdList);
 
    @Query(value =" select e.id,e.ename,e.ename_hindi,er.emp_role from ate.department_product_mapping dpm "
       		+ "left join mdms.department d on dpm.department_id=d.id "
       		+ "left join mdms.employee e on e.department_id=d.id "
       		+ "LEFT JOIN  mdms.emp_role er on er.id =e.emp_role_id where product_id in (:prodIds) and er.id=14", nativeQuery = true)
     List<Object[]> getAllHosByProdId(@Param("prodIds") List<Integer> prodIds);
     
     @Query(value ="select distinct e.id,e.ename,e.ename_hindi,er.emp_role from tdms.supply_order so"
     		+ " join tdms.so_line_item sl on sl.so_id=so.id "
     		+ "join ate.partno_product_mapping ppm on ppm.part_number_id=sl.part_number_id "
     		+ "join ate.department_product_mapping dpm on dpm.product_id=ppm.product_id "
     		+ " left join mdms.department d on dpm.department_id=d.id "
     		+ "left join mdms.employee e on e.department_id=d.id "
     		+ "LEFT JOIN  mdms.emp_role er on er.id =e.emp_role_id where so.id=:soId and er.id=14 ;", nativeQuery = true)
      List<Object[]> getAllHosBySoId(@Param("soId") int  soId);
      
      
      @Query(value = "select emp.id, emp.ename from mdms.employee emp "
  			+ "left join mdms.department dept on emp.department_id = dept.id "
  			+ "left join mdms.department_type dtype on dept.department_type = dtype.id "
  			+ "where emp.emp_role_id in :roleIds and dtype.id = :deptTypeId ", nativeQuery = true)
  List<Object[]> getAllNotifyToEmpByDeptId(@Param("roleIds") List<Integer> roleIds, int deptTypeId );
  
  
  @Query(value = "select emp.id, emp.ename, usr.username from mdms.employee emp "
			+ "left join mdms.department dept on emp.department_id = dept.id "
			+ "left join mdms.department_type dtype on dept.department_type = dtype.id "
			+ "left join mdms.user usr on emp.id = usr.emp_id " //14-08-26 - added to fetch usr.username as staff no
			+ "where dtype.id = :deptTypeId and emp.active = true ", nativeQuery = true)
List<Object[]> getAllActiveEmpBydepId(int deptTypeId);

@Query(value = "select emp.id, emp.ename from mdms.employee emp "
		+ "left join mdms.department dept on emp.department_id = dept.id "
		+ "left join mdms.department_type dtype on dept.department_type = dtype.id "
		+ "where dtype.id = :deptTypeId and emp.active = true and  emp_role_id =18 ", nativeQuery = true)
List<Object[]> getAllActiveDoBydepTypeId(int deptTypeId);

	@Query(value = "select e.id, e.ename from mdms.employee e "
			+ "left join mdms.department d on d.id = e.department_id "
			+ "left join ate.department_product_mapping dpm on dpm.department_id = d.id "
			+ "left join ate.partno_product_mapping ppm on ppm.product_id = dpm.product_id "
			+ "where ppm.part_number_id = :partNumberId and d.id = :deptId and e.emp_role_id = :roleId and e.active = true ", nativeQuery = true)
	List<Object[]> getAllEmpsByDeptIdAndPartNoId(Integer deptId, Integer partNumberId, Integer roleId);
	

	@Query(value = "SELECT e.id,e.ename FROM mdms.employee e "
			+ "JOIN mdms.department d ON e.department_id = d.id WHERE e.emp_role_id IN (14,18) "
			+ "  AND d.department_type = 3 and e.active = true ", nativeQuery = true)
	List<Object[]> getAllDoAndHosQc();

    @Query(value =
        "WITH RECURSIVE hierarchy AS (" +
            "    SELECT id, ename, reporting_officer_id, emp_role_id " +
            "    FROM mdms.employee " +
            "    WHERE id = :empId " +
            "    UNION ALL " +
            "    SELECT e.id, e.ename, e.reporting_officer_id, e.emp_role_id " +
            "    FROM mdms.employee e " +
            "    INNER JOIN hierarchy h ON e.reporting_officer_id = h.id" +
            ") " +
            "SELECT DISTINCT id, ename  FROM hierarchy", nativeQuery = true)
    List<Object[]> findAllSubordinatesRecursive(@Param("empId") int empId);

    @Query(value =
        "WITH RECURSIVE hierarchy AS (" +
            "    SELECT id, ename, reporting_officer_id, emp_role_id " +
            "    FROM mdms.employee " +
            "    WHERE id = :empId " +
            "    UNION ALL " +
            "    SELECT e.id, e.ename, e.reporting_officer_id, e.emp_role_id " +
            "    FROM mdms.employee e " +
            "    INNER JOIN hierarchy h ON e.reporting_officer_id = h.id" +
            ") " +
            "SELECT DISTINCT id, ename " +
            "FROM hierarchy " +
            "WHERE id NOT IN (:allotedToIds)", nativeQuery = true)
    List<Object[]> findAvailableSubordinatesRecursive(@Param("empId") int empId, @Param("allotedToIds") List<Integer> allotedToIds);

    @Query(value = "WITH RECURSIVE employee_hierarchy AS (" +
        "    SELECT id, ename, reporting_officer_id, emp_role_id " +
        "    FROM mdms.employee " +
        "    WHERE emp_role_id = :roleId " +
        "    UNION ALL " +
        "    SELECT e.id, e.ename, e.reporting_officer_id, e.emp_role_id " +
        "    FROM mdms.employee e " +
        "    INNER JOIN employee_hierarchy eh ON e.reporting_officer_id = eh.id" +
        ") " +
        "SELECT DISTINCT id, ename FROM employee_hierarchy", nativeQuery = true)
    List<Object[]> getAllEmpAndReportingOfficersByRoleId(@Param("roleId") int roleId);

    @Query(value = "WITH RECURSIVE employee_hierarchy AS (" +
        "    SELECT id, ename, reporting_officer_id, emp_role_id " +
        "    FROM mdms.employee " +
        "    WHERE emp_role_id = :roleId " +
        "    UNION ALL " +
        "    SELECT e.id, e.ename, e.reporting_officer_id, e.emp_role_id " +
        "    FROM mdms.employee e " +
        "    INNER JOIN employee_hierarchy eh ON e.reporting_officer_id = eh.id" +
        ") " +
        "SELECT DISTINCT id, ename FROM employee_hierarchy " +
        "WHERE id NOT IN (:allotedToIds)", nativeQuery = true)
    List<Object[]> getAvailableEmpAndReportingOfficersByRoleId(@Param("roleId") int roleId, @Param("allotedToIds") List<Integer> allotedToIds);

    
    @Query(value = "SELECT * FROM mdms.employee emp "
    		+ " WHERE emp.department_id = :deptId AND emp.emp_role_id = :doRoleId", nativeQuery = true)
	Employee getDoByDeptId(Integer deptId, int doRoleId);


    @Query(value = "SELECT * FROM mdms.employee emp"
    		+ " LEFT JOIN mdms.department d ON emp.department_id = d.id "
    		+ " LEFT JOIN mdms.department_type dt ON d.department_type = dt.id"
    		+ " WHERE dt.id = :deptTypeId AND emp.emp_role_id = :doRoleId", nativeQuery = true)
	List<Employee> getAllEmpsByRoleIdAndDeptId(int doRoleId, int deptTypeId);
	
}