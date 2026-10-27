package in.co.bel.olms.master.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.co.bel.olms.master.entity.User;



/**
 * @author Manasa B
 * @since 20-10-2022
 */
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query(value = "select count(*) from User u where u.username=:userName")
	int findUsersCountByUsername(@Param("userName") String userName);
	
	@Query(value="SELECT * FROM mdms.user WHERE username = :username and active=true",nativeQuery=true)
	User getUserByUsername(@Param("username") String username);
	
	@Query(value="SELECT * FROM mdms.user WHERE username = :username and active=true and token_value = :token",nativeQuery=true)
	User getValidUserByToken(@Param("username") String username, @Param("token") String token );

	@Query(value="select emp.ename as employee_name ,emp.gender,emp.seal_no,usr.username,usr.active,org.type_name as organization_name,er.emp_role ,er.id as role_id,emp.reporting_officer_id as reporting_officer_id , " +
			"(select em.ename as reporting_officer from mdms.employee em where em.id=emp.reporting_officer_id), usr.id, emp.id as empId, dept.id as dept_id, dept.department_name  " +
			"from mdms.employee emp " +
			"left join mdms.user usr on usr.emp_id=emp.id  " +
			"left join mdms.emp_role er on er.id=emp.emp_role_id  " +
			"left join mdms.organization_type org on org.id=er.organization_type  " +
			"left join mdms.department dept on emp.department_id = dept.id " +
			"where emp.organization_id =:orgId ",nativeQuery=true)
	List<Object[]> getAllUsersByOrgId(@Param("orgId") int orgId);
		
	@Query(value="select emp.ename as employee_name ,emp.gender,emp.seal_no,usr.username,usr.active,org.type_name as organization_name,er.emp_role ,er.id as role_id,emp.reporting_officer_id as reporting_officer_id , " +
			"(select em.ename as reporting_officer from mdms.employee em where em.id=emp.reporting_officer_id), usr.id, emp.id as empId, " +
			"STRING_AGG(cast(dept.id as text), ',') AS dept_ids, STRING_AGG(dept.department_name, ',') AS dept_names" +
			"from mdms.employee emp " +
			"left join mdms.user usr on usr.emp_id=emp.id  " +
			"left join mdms.emp_role er on er.id=emp.emp_role_id  " +
			"left join mdms.organization_type org on org.id=er.organization_type  " +
			"left join mdms.emp_dept_mapping edm on edm.id = edm.emp_id " +
			"left join mdms.department dept on edm.dept_id = dept.id " +
			"where emp.organization_id =:orgId "+
			"GROUP BY emp.ename, emp.gender, emp.seal_no, usr.username, " +
			"usr.active, org.type_name, er.emp_role, er.id, emp.reporting_officer_id, usr.id, emp.id",nativeQuery=true)
	List<Object[]> getAllUsersByOrgIdV2(@Param("orgId") int orgId);
	

	@Query(value="SELECT emp.ename AS employee_name, emp.gender, emp.seal_no, usr.username, usr.active, \r\n"
			+ "    org.type_name AS organization_name, er.emp_role, er.id AS role_id, emp.reporting_officer_id AS reporting_officer_id, \r\n"
			+ "    (SELECT em.ename FROM mdms.employee em WHERE em.id = emp.reporting_officer_id) AS reporting_officer, \r\n"
			+ "    usr.id, emp.id AS empId, \r\n"
			+ "    STRING_AGG(DISTINCT CAST(dept.id AS text), ',') AS dept_ids,  \r\n"
			+ "    STRING_AGG(DISTINCT dept.department_name, ', ') AS dept_names,  \r\n"
			+ "    emp.organization_id, emp.sbu_id AS sales_group_id, emp.dept_type_id, emp.staff_no \r\n"
			+ "FROM mdms.employee emp \r\n"
			+ "LEFT JOIN mdms.user usr ON usr.emp_id = emp.id \r\n"
			+ "LEFT JOIN mdms.emp_role er ON er.id = emp.emp_role_id \r\n"
			+ "LEFT JOIN mdms.organization_type org ON org.id = er.organization_type \r\n"
			+ "LEFT JOIN mdms.organization o ON o.id = emp.organization_id \r\n"
			+ "LEFT JOIN mdms.emp_dept_mapping edm ON edm.emp_id = emp.id \r\n"
			+ "LEFT JOIN mdms.department dept ON edm.dept_id = dept.id \r\n"
			+ "WHERE er.organization_type = :orgTypeId \r\n"
			+ "GROUP BY emp.ename, emp.gender, emp.seal_no, usr.username, usr.active, \r\n"
			+ "    org.type_name, er.emp_role, er.id, emp.reporting_officer_id, usr.id, emp.id, \r\n"
			+ "    emp.organization_id, emp.sbu_id, emp.dept_type_id, emp.staff_no \r\n"
			+ "ORDER BY usr.id DESC ", nativeQuery=true)
	List<Object[]> getAllUserByOrgTypeId(@Param("orgTypeId") int orgTypeId);
	
	@Query(value = "select usr.id as user_id,usr.username,usr.active, emp.id  as emp_id ,emp.ename as employee_name ,emp.gender,emp.seal_no,org.organization_name as organization_name,org.id as org_id,er.emp_role ,"
			+ "er.id as role_id,emp.sbu_id,sg.name, org.organization_type_id as orgTypeId"
			+ " from mdms.user usr " + " left join mdms.employee emp  on usr.emp_id=emp.id "
			+ " left join mdms.sales_group sg on emp.sbu_id=sg.id "
			+ " left join mdms.emp_role er on emp.emp_role_id =er.id "
			+ " left join mdms.organization org on emp.organization_id =org.id where er.is_admin = true", nativeQuery = true)
	List<Object[]> getAllUsers();
	
	@Query(value = "SELECT * from mdms.user WHERE active = true", nativeQuery = true)
	public List<User> findAllActiveUser();
	
	boolean existsByUsername(String username);

	@Query(value=" select usr.id,emp.ename as employee_name ,emp.gender,emp.seal_no,usr.username,usr.active,org.organization_name as organization_name,er.emp_role "
 			+" from mdms.employee emp "
 			+"  left join mdms.user usr on usr.emp_id=emp.id  "
 			+"  left join mdms.emp_role er on er.id=emp.emp_role_id  "
 			+" left join mdms.organization org on org.id=emp.organization_id where usr.login_status=true",nativeQuery=true)
 	List<Object[]> getAllActiveLoginUsers();

	boolean existsByUsernameAndIdNot(String newUsername, Integer integer);
	 
	@Query(value="select "
			+ "	STRING_AGG(cast(dept.id as text), ',') AS dept_ids, STRING_AGG(dept.department_name, ',') AS dept_names "
			+ "	from mdms.department dept "
			+ "	left join mdms.emp_dept_mapping edm on edm.dept_id = dept.id "
			+ "	left join mdms.user usr on usr.emp_id = edm.emp_id "
			+ " where edm.emp_id = :empId",nativeQuery=true)
	Map<String, Object> getDeptIdsAndNames(@Param("empId") int empId);
	
}
