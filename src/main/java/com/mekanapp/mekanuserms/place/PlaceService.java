package com.mekanapp.mekanuserms.place;

import com.mekanapp.mekanuserms.exception.AppException;
import com.mekanapp.mekanuserms.exception.ErrorMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PlaceService {

    private final PlaceRepository repository;
    private final PlaceMapper placeMapper;

    public UUID createPlace(PlaceCreateDto placeCreateDto) {
        Place newPlace = new Place();
        BeanUtils.copyProperties(placeCreateDto, newPlace);
        newPlace.setPlaceStatus(PlaceStatuses.ACTIVE.toString());
        Place responsePlace = repository.save(newPlace);

        return responsePlace.getId();
    }

    public Page<PlaceDto> getPlaces(String placeName, Pageable page) {
        if(placeName.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.PLACE_NOT_FOUND);
        }
        else {

            Page<PlaceDto> existPlaces = repository.findByPlaceName(placeName, page);

            if(!existPlaces.getContent().isEmpty()){
                return existPlaces;
            }
            else if(existPlaces.getContent().isEmpty()) {
                String lowerPlaceName = placeName.toLowerCase();
                return repository.findByPlaceNameContaining(lowerPlaceName, page).map(placeMapper::toDto);
            }
            else {
                throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.PLACE_NOT_FOUND);
            }

        }
    }

    public PlaceDto updatePlace(UUID id, PlaceUpdateDto placeUpdateDto) {
        Optional<Place> existPlace = repository.findById(id);

        if(existPlace.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.PLACE_NOT_FOUND);
        }

        Place existingPlace = existPlace.get();
        BeanUtils.copyProperties(placeUpdateDto, existingPlace);
        Place newResponsePlace = repository.save(existingPlace);

        return placeMapper.toDto(newResponsePlace);

    }

    public Boolean deletePlace(UUID id) {
        Optional<Place> existPlace = repository.findById(id);

        if(existPlace.isEmpty()) {
            throw AppException.withStatusAndMessage(HttpStatus.NOT_FOUND, ErrorMessages.PLACE_NOT_FOUND);
        }
        else {
            repository.deleteById(id);
            return true;
        }

    }

}
