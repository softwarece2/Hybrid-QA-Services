package in.co.bel.olms.transaction.service;

import java.util.Optional;

import org.dozer.Mapper;
import org.springframework.stereotype.Service;

import in.co.bel.olms.master.exception.DataNotFoundException;

import in.co.bel.olms.transaction.dto.VendorRatingDto;

import in.co.bel.olms.transaction.entity.VendorRating;
import in.co.bel.olms.transaction.repository.VendorRatingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VendorRatingService {

	private final VendorRatingRepository vendorRatingRepo;

	private final Mapper mapper;

	public VendorRating getVendorRatingById(int id) {
		Optional<VendorRating> vendorRatingOptional = vendorRatingRepo.findById(id);
		if (vendorRatingOptional.isPresent()) {
			return vendorRatingOptional.get();
		} else {
			throw new DataNotFoundException("VendorRating is not found by Id: " + id);
		}
	}

	public VendorRating saveVendorRating(VendorRatingDto vendorRatingDto) {

		VendorRating vendorRating = mapper.map(vendorRatingDto, VendorRating.class);
		return vendorRatingRepo.save(vendorRating);

	}
}
