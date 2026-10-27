package in.co.bel.olms.transaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import in.co.bel.olms.constants.ApplicationConstants;
import in.co.bel.olms.transaction.dto.INoteDto;
import in.co.bel.olms.transaction.dto.INoteLineItemDto;
import in.co.bel.olms.transaction.entity.INote;
import in.co.bel.olms.transaction.entity.INoteLineItem;
import in.co.bel.olms.transaction.repository.INoteLineItemRepository;
import in.co.bel.olms.transaction.repository.INoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class INoteService {

    private final INoteRepository iNoteRepository;

    private final INoteLineItemRepository iNoteLineItemRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(INoteService.class);

    @Transactional(propagation = Propagation.NESTED)
    public String updateINote(INoteDto iNoteDto) throws Exception {
        String status = null;
        SimpleDateFormat prevINoteDateFormatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Optional<INote> iNoteOptional = iNoteRepository.findById(iNoteDto.getInoteId());
            if (iNoteOptional.isPresent()) {
                INote iNote = iNoteOptional.get();
                iNote.setConsignee(iNoteDto.getConsignee());
                iNote.setRemarks(iNoteDto.getRemarks());
                iNote.setAmendment(iNoteDto.getAmendment());
                iNote.setDescription(iNoteDto.getDescription());
                iNote.setPrevINoteNo(iNoteDto.getPrevINoteNo());
               // iNote.setSealNo(iNoteDto.getSealNo());
                iNote.setOrgName(iNoteDto.getOrgName());
                iNote.setIndentor(iNoteDto.getIndentor());
                iNote.setInoteDate(iNoteDto.getInoteDate());
                if (iNoteDto.getPrevINoteDate() != null) {
                    Date prevInoteDate = prevINoteDateFormatter.parse(iNoteDto.getPrevINoteDate());
                    iNote.setPrevINoteDate(prevInoteDate);
                }
              
                iNote.setCqaoRemarks(iNoteDto.getCqaoRemarks());
                iNote.setINoteNo(iNoteDto.getINoteNo());
                List<INoteLineItem> iNoteLineItems = new ArrayList<>();
                for (INoteLineItemDto iNoteLineItemDto : iNoteDto.getINoteLineItems()) {
                    Optional<INoteLineItem> iNoteLineItemOptional = iNoteLineItemRepository.findById(iNoteLineItemDto.getId());
                    if (iNoteLineItemOptional.isPresent()) {
                        INoteLineItem iNoteLineItem = iNoteLineItemOptional.get();
                        iNoteLineItem.setRemarks(iNoteLineItemDto.getRemarks());
                        iNoteLineItem.setItemRemarks(iNoteLineItemDto.getItemRemarks());
                        iNoteLineItem.setHologramRemarks(iNoteLineItemDto.getHologramRemarks());
                        iNoteLineItems.add(iNoteLineItem);
                    }
                }
                iNoteLineItemRepository.saveAll(iNoteLineItems);
                iNoteRepository.save(iNote);
                status = ApplicationConstants.SUCCESS;
            }
        } catch (Exception e) {
        	logger.error("error occured while updating INote ", e);
        	logger.error("Couldn't update the INote due to {}", e.getMessage());
            status = ApplicationConstants.FAILURE;
        }
        return status;
    }
}
