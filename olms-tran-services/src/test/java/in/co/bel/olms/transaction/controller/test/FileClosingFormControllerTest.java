package in.co.bel.olms.transaction.controller.test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import in.co.bel.olms.OlmsTranServicesApplication;
import in.co.bel.olms.master.entity.ChecklistFileClose;
import in.co.bel.olms.master.exception.DataNotFoundException;
import in.co.bel.olms.master.exception.DataNotValidException;
import in.co.bel.olms.transaction.controller.FileClosingFormController;
import in.co.bel.olms.transaction.dto.FileClosingFormDto;
import in.co.bel.olms.transaction.entity.FileClosingForm;
import in.co.bel.olms.transaction.entity.FileClosingFormDet;
import in.co.bel.olms.transaction.service.so.FileClosingFormService;

@SpringBootTest
@ContextConfiguration(classes = OlmsTranServicesApplication.class)
class FileClosingFormControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(FileClosingFormControllerTest.class);
	
	@InjectMocks
	FileClosingFormController fileClosingFormController;
	
	@Mock
	FileClosingFormService fileClosingFormService;
	
	@Mock
	ObjectMapper mapper2;

	@Mock
	private Mapper mapper;
	
	Gson gson = new Gson();
	
	@Test
	void saveFileClosingFormValidTest() {
		logger.info("FileClosingFormControllerTest.saveFileClosingFormValidTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			FileClosingForm fileClosingForm = new FileClosingForm();
			List<FileClosingFormDet> lFileCloseFormDet = new ArrayList<>();
			FileClosingFormDet fileCloseFormDet = new FileClosingFormDet();
			ChecklistFileClose checkfileclose = new ChecklistFileClose();
			checkfileclose.setId(1);
			checkfileclose.setSlNo(1);
			checkfileclose.setReference("Separate out all indents and amendments as Vol 'A' or as a separate part of the same file");
			checkfileclose.setObsType("obs1");
			checkfileclose.setSplit(false);
			fileCloseFormDet.setChecklistFileClose(checkfileclose);
			fileCloseFormDet.setId(3);
			fileCloseFormDet.setRemarks("tested");
			fileCloseFormDet.setObsSts("ObsSts");
			lFileCloseFormDet.add(fileCloseFormDet);
			lFileCloseFormDet.add(fileCloseFormDet);
			fileClosingForm.setfileclosingFormDets(lFileCloseFormDet);
			when(fileClosingFormService.saveFileClosingForms(any(FileClosingFormDto.class))).thenReturn(fileClosingForm);
			when(fileClosingFormService.convertBoToentity(any(FileClosingForm.class))).thenReturn(fileClosingFormDto);
			when(mapper.map(any(FileClosingForm.class), eq(FileClosingFormDto.class))).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> res = fileClosingFormController.saveFileClosingForm(fileClosingFormDto);
			assertNotNull(res);
		}, "Test failed due to an exception: saveFileClosingFormValidTest ");
		logger.info("FileClosingFormControllerTest.saveFileClosingFormValidTest : Finished");
	}

	@Test
	void saveFileClosingFormDataNotValidExceptionTest() throws DataNotValidException {
		logger.info("FileClosingFormControllerTest.saveFileClosingFormDataNotValidExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			when(fileClosingFormService.saveFileClosingForms(any())).thenThrow(new DataNotValidException("Invalid Data"));
			when(fileClosingFormService.convertBoToentity(any(FileClosingForm.class))).thenReturn(fileClosingFormDto);
			when(mapper.map(any(FileClosingForm.class), eq(FileClosingFormDto.class))).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> res = fileClosingFormController.saveFileClosingForm(fileClosingFormDto);
			assertNotNull(res);
		}, "Test failed due to an exception: saveFileClosingFormDataNotValidExceptionTest ");
		logger.info("FileClosingFormControllerTest.saveFileClosingFormDataNotValidExceptionTest : Finished");
	}
	
	@Test
	void saveFileClosingFormExceptionTest() {
		logger.info("FileClosingFormControllerTest.saveFileClosingFormExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			when(fileClosingFormService.saveFileClosingForms(any())).thenThrow(new NullPointerException());
			when(fileClosingFormService.convertBoToentity(any(FileClosingForm.class))).thenReturn(fileClosingFormDto);
			when(mapper.map(any(FileClosingForm.class), eq(FileClosingFormDto.class))).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> res = fileClosingFormController.saveFileClosingForm(fileClosingFormDto);
			assertNotNull(res);
		}, "Test failed due to an exception: saveFileClosingFormExceptionTest ");
		logger.info("FileClosingFormControllerTest.saveFileClosingFormExceptionTest : Finished");
	}
	
	@Test
	void submitFileClosingFormValidTest() {
		logger.info("FileClosingFormControllerTest.submitFileClosingFormValidTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			FileClosingForm fileClosingForm = new FileClosingForm();
			List<FileClosingFormDet> lFileCloseFormDet = new ArrayList<>();
			FileClosingFormDet fileCloseFormDet = new FileClosingFormDet();
			ChecklistFileClose checkfileclose = new ChecklistFileClose();
			checkfileclose.setId(1);
			checkfileclose.setSlNo(1);
			checkfileclose.setReference("Separate out all indents and amendments as Vol 'A' or as a separate part of the same file");
			checkfileclose.setObsType("obs1");
			checkfileclose.setSplit(false);
			fileCloseFormDet.setChecklistFileClose(checkfileclose);
			fileCloseFormDet.setId(3);
			fileCloseFormDet.setRemarks("tested");
			fileCloseFormDet.setObsSts("ObsSts");
			lFileCloseFormDet.add(fileCloseFormDet);
			lFileCloseFormDet.add(fileCloseFormDet);
			fileClosingForm.setfileclosingFormDets(lFileCloseFormDet);
			when(fileClosingFormService.submitFile(any(FileClosingFormDto.class))).thenReturn(fileClosingForm);
			when(mapper.map(any(FileClosingForm.class), eq(FileClosingFormDto.class))).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> result = fileClosingFormController.submitFileClosingForm(fileClosingFormDto);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFileClosingFormValidTest ");
		logger.info("FileClosingFormControllerTest.submitFileClosingFormValidTest : Finished");
	}

	@Test
	void submitFileClosingFormDataNotFoundExceptionTest() throws DataNotFoundException {
		logger.info("FileClosingFormControllerTest.submitFileClosingFormDataNotFoundExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			when(fileClosingFormService.submitFile(any())).thenThrow(new DataNotFoundException("Invalid Data"));
			when(mapper.map(any(), any())).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> result = fileClosingFormController.submitFileClosingForm(fileClosingFormDto);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFileClosingFormDataNotFoundExceptionTest ");
		logger.info("FileClosingFormControllerTest.submitFileClosingFormDataNotFoundExceptionTest : Finished");
	}
	
	@Test
	void submitFileClosingFormExceptionTest() {
		logger.info("FileClosingFormControllerTest.submitFileClosingFormExceptionTest : Initiated");
		assertDoesNotThrow(() -> {
			FileClosingFormDto fileClosingFormDto = new FileClosingFormDto();
			when(fileClosingFormService.submitFile(any())).thenThrow(new NullPointerException());
			when(mapper.map(any(FileClosingForm.class), eq(FileClosingFormDto.class))).thenReturn(fileClosingFormDto);
			ResponseEntity<FileClosingFormDto> result = fileClosingFormController.submitFileClosingForm(fileClosingFormDto);
			assertNotNull(result);
		}, "Test failed due to an exception: submitFileClosingFormExceptionTest ");
		logger.info("FileClosingFormControllerTest.submitFileClosingFormExceptionTest : Finished");
	}
	
	@Test
	void getByClosingFormControllerTest() {
		logger.info("FileClosingFormControllerTest.getByClosingFormControllerTest : Initiated");
		assertDoesNotThrow(() -> {
			int soId = 84;
			when(fileClosingFormService.getFileCloseFormById(anyInt())).thenReturn(new FileClosingFormDto());
			ResponseEntity<FileClosingFormDto> res = fileClosingFormController.getByClosingForm(soId);
			assertNotNull(res);
		}, "Test failed due to an exception: getByClosingFormControllerTest ");
		logger.info("FileClosingFormControllerTest.getByClosingFormControllerTest : Finished");
	}
	
}
