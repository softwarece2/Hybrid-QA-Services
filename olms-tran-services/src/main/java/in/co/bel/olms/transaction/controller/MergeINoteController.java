//package in.co.bel.olms.transaction.controller;
//
//import java.util.Arrays;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import in.co.bel.olms.user.jwt.JwtUtils;
//import lombok.RequiredArgsConstructor;
//
///**
// * @author Shridhar R
// * Merge INote service	
// */
//
//@RestController
//@RequestMapping("/internal")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RequiredArgsConstructor
//public class MergeINoteController {
//
//	private final MergeINoteService mergeINoteService;
//	
//	private final JwtUtils jwtUtils;
//	
////	 only DO user allowed to save 
//	@PreAuthorize("hasAnyAuthority('DO')")  
//	@PostMapping("/mergeINote/save")
//	private ResponseEntity<String> mergeINote(@RequestBody MergeINoteDto requestDto, @RequestHeader(name = "Authorization") String token) throws Exception {
//		if(token != null && token.startsWith("Bearer ")) {
//			token = token.substring(7);
//		}
//		try {
//			Map<String, Object> map = jwtUtils.parseToken(token);
//			requestDto.setCreatedBy(jwtUtils.getEmpIdFromJwtToken(map));
//			ResponseEntity<String> res = mergeINoteService.saveMergeINote(requestDto);
//			return res;
//		}catch (IllegalArgumentException e) {
//
//	        System.err.println("Invalid argument: " + e.getMessage());
//	        return ResponseEntity.badRequest().body(e.getMessage());
//	    } catch (Exception e) {
//	
//	        System.err.println("Failed to save " + e.getMessage());
//	        return ResponseEntity.internalServerError().body("Failed to save ");
//	    }
//	}
//	
////	for now, get access given to all 
//    @GetMapping("/getAllMergedINotes")
//    public ResponseEntity<List<MergeINoteDetDto>> getAllMergedINotes() {
//        try {
//        	List<MergeINoteDetDto> iNoteDto = mergeINoteService.getAllMergedINotes();
//            return ResponseEntity.ok(iNoteDto);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(null);
//        }
//    }
//    
//// not added preAuthorize for now for get
//    @GetMapping("/getMergedINoteDetById/{mergedINoteId}")
//    public ResponseEntity<List<MergeINoteDetDto>> getMergedINoteDetById(@PathVariable("mergedINoteId") Integer mergedINoteId) {
//        try {
//        	List<MergeINoteDetDto> iNoteDto = mergeINoteService.getMergedINoteDetById(mergedINoteId);
//            return ResponseEntity.ok(iNoteDto);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(null);
//        }
//    }
//    
//    @GetMapping("/getAllINoteDetails/{inoteIdString}")
//    public ResponseEntity<List<INoteDetailDto>> getAllINoteDetails(@PathVariable("inoteIdString") String inoteIdString) {
//        if(inoteIdString == null || inoteIdString.isEmpty()) {
//        	return ResponseEntity.badRequest().body(Collections.emptyList());
//        }    
//    	try {
//    		List<Integer> inoteIds = Arrays.stream(inoteIdString.split(","))
//    				                .map(Integer::parseInt)
//    				                .collect(Collectors.toList());
//        	List<INoteDetailDto> iNoteDto = mergeINoteService.getAllINoteDetails(inoteIds);
//            return ResponseEntity.ok(iNoteDto);
//        }catch(Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body(null);
//        }
//    }
//}
