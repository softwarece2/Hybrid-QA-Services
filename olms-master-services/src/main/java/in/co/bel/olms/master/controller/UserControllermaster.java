package in.co.bel.olms.master.controller;

import java.util.List;

import javax.validation.Valid;

import org.dozer.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import in.co.bel.olms.master.dto.UserDtomaster;
import in.co.bel.olms.master.entity.User;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.master.service.UserService;
import in.co.bel.olms.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Slf4j
public class UserControllermaster {

	private final UserService userService;

	private final Mapper mapper;

	Gson gson = new Gson();

	/**
	 * @param id
	 * @return
	 * @throws DataNotFoundException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDtomaster> getUserById(@PathVariable int id) throws DataNotFoundException {
		User user = userService.getUserById(id);
		UserDtomaster userDto = mapper.map(user, UserDtomaster.class);
		return ResponseEntity.ok(userDto);
	}

	/**
	 * @param userDto
	 * @return
	 */
	@PostMapping("/saveUser")
	public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto userDto) {
		User savedUser = userService.saveUser(userDto);
		UserDto savedUserDto = mapper.map(savedUser, UserDto.class);
		return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);

	}

	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto) {
	    try {
	        Boolean savedUser = userService.createUser(userDto);
	        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	        
	    } catch (Exception e) {
	   
	        if ("CONFLICT_USER_EXISTS".equals(e.getMessage())) {
	            return new ResponseEntity<>(" Employee already exists with this  User name", HttpStatus.CONFLICT);
	        }
	        
	        if (e instanceof DataNotValidException) {
	            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
	        }

	        return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    }
	@GetMapping("/getAllUsers/{orgId}")
	public ResponseEntity<List<UserDto>> getAllUsersByOrgId(@PathVariable int orgId) {
		List<UserDto> userDto = userService.getUsersByOrgId(orgId);
		return ResponseEntity.ok(userDto);
	}
	
	//getUsersByOrgTypeId
	@GetMapping("/getUsers/{orgTypeId}")
	public ResponseEntity<List<UserDto>> getAllUsersByOrgTypeId(@PathVariable int orgTypeId) {
		List<UserDto> userDto = userService.getUsersByOrgTypeId(orgTypeId);
		return ResponseEntity.ok(userDto);
	}

	@GetMapping("/fetchAllUsers")
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> userDto = userService.getAllUsers();
		return ResponseEntity.ok(userDto);
	}

	@PutMapping("/editUser")
	public ResponseEntity<String> editUser(@RequestBody @Valid UserDto userDto) {
		UserDto savedUserDto = null;
		try {
			if (userDto != null) {

				User savedUser = userService.editUser(userDto);
				savedUserDto = mapper.map(savedUser, UserDto.class);
				return new ResponseEntity<String>(gson.toJson(savedUserDto), HttpStatus.CREATED);

			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDto userDtos) {
        try {
            User savedUser = userService.updateUser(userDtos);
            
            UserDto savedUserDto = mapper.map(savedUser, UserDto.class);
            
       return new ResponseEntity<>(gson.toJson(savedUserDto), HttpStatus.OK);

        } catch (RuntimeException e) {
       
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);

        } catch (Exception e) {
           
            e.printStackTrace();
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), 
                                        HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@GetMapping("/fetchAllActiveLoginUsers")
 	    public ResponseEntity<List<UserDto>> getAllActiveLoginsUsers() {
 	        List<UserDto> userDto = userService.getAllActiveLoginUsers();
 	        return ResponseEntity.ok(userDto);
 	    }

	
	@GetMapping("/getOrgCqaeById")
	 public ResponseEntity<List<UserDto>>  getOrgCqaeById() {
		
		try {
			
			return ResponseEntity.ok(userService.getOrgCqae());
			
		}
		
		catch (Exception e) {
          log.error("Error fetching Inspection Agency by id: {} {}",   e.getMessage(), e);
          throw e;
      }
	}
}
