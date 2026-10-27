package in.co.bel.olms.transaction.service;

import in.co.bel.olms.transaction.entity.SupplyOrder;
import in.co.bel.olms.transaction.repository.INoteRepository;
import in.co.bel.olms.transaction.repository.OfferLetterRepository;
import in.co.bel.olms.transaction.repository.SupplyOrderRepository;
import in.co.bel.olms.transaction.repository.SurpriseCheckRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for registers
 *
 * @author  Awadhesh Verma
 * @since   26-08-2023
 */

@Service
@RequiredArgsConstructor
public class RegistersService {

    private final SupplyOrderRepository supplyOrderRepository;

    private final OfferLetterRepository offerLetterRepository;

    private final SurpriseCheckRepository surpriseCheckRepository;

    private final INoteRepository iNoteRepository;

    private static final Logger logger = LoggerFactory.getLogger(RegistersService.class);

    public List<SupplyOrder> getSORegister(int orgId, String fromDate, String toDate) {

        return supplyOrderRepository.getSORegister(orgId, fromDate, toDate);

    }

    public List<Object[]> getOLRegister(int orgId, String fromDate, String toDate) {

        return offerLetterRepository.getOLRegister(orgId, fromDate, toDate);

    }

    public List<Object[]> getSurpriseCheckRegister(int orgId, String fromDate, String toDate) {

        return surpriseCheckRepository.getSurpriseCheckRegister(orgId, fromDate, toDate);

    }

    public List<Object[]> getINoteRegister(int orgId, String fromDate, String toDate) {

        return iNoteRepository.getINoteRegister(orgId, fromDate, toDate);

    }

}
