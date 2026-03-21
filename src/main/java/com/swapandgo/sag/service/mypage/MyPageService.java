package com.swapandgo.sag.service.mypage;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.dto.mypage.MyAccountDeleteRequest;
import com.swapandgo.sag.dto.mypage.MyItemResponse;
import com.swapandgo.sag.dto.mypage.MyProfileResponse;
import com.swapandgo.sag.dto.mypage.MyProfileUpdateRequest;
import com.swapandgo.sag.exception.ActiveTransactionException;
import com.swapandgo.sag.exception.DuplicateUsernameException;
import com.swapandgo.sag.exception.InvalidPasswordException;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.TransactionRepository;
import com.swapandgo.sag.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MyProfileResponse getMyProfile(Long userId) {
        User user = getUser(userId);
        return new MyProfileResponse(user);
    }

    public MyProfileResponse updateMyProfile(Long userId, MyProfileUpdateRequest request) {
        User user = getUser(userId);
        if (request.getUsername() != null && !request.getUsername().isBlank()
                && !request.getUsername().equals(user.getUsername())
                && userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException("이미 사용 중인 닉네임입니다.");
        }
        Address newAddress = mergeAddress(user.getAddress(), request);
        user.updateProfile(request.getUsername(), request.getPassword(), newAddress);
        return new MyProfileResponse(user);
    }

    public void deleteMyAccount(Long userId, MyAccountDeleteRequest request) {
        if (transactionRepository.existsActiveRentalForUser(userId, LocalDateTime.now())) {
            throw new ActiveTransactionException("현재 진행 중인 대여 거래가 있어 탈퇴할 수 없습니다.");
        }
        User user = getUser(userId);
        if (request == null || request.getPassword() == null || request.getPassword().isBlank()) {
            throw new InvalidPasswordException("비밀번호를 입력해주세요.");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public List<MyItemResponse> listMyItems(Long userId) {
        List<Item> items = itemRepository.findAllByUserIdOrderByIdDesc(userId);
        return items.stream().map(MyItemResponse::new).collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    private Address mergeAddress(Address current, MyProfileUpdateRequest request) {
        boolean hasAny = request.getFullAddress() != null
                || request.getLatitude() != null
                || request.getLongitude() != null
                || request.getCountry() != null
                || request.getRegion() != null
                || request.getDistrict() != null
                || request.getStreet() != null
                || request.getZipcode() != null;

        if (!hasAny && current == null) {
            return null;
        }

        String fullAddress = request.getFullAddress() != null ? request.getFullAddress()
                : current != null ? current.getFullAddress() : null;
        Double latitude = request.getLatitude() != null ? request.getLatitude()
                : current != null ? current.getLatitude() : null;
        Double longitude = request.getLongitude() != null ? request.getLongitude()
                : current != null ? current.getLongitude() : null;
        String country = request.getCountry() != null ? request.getCountry()
                : current != null ? current.getCountry() : null;
        String region = request.getRegion() != null ? request.getRegion()
                : current != null ? current.getRegion() : null;
        String district = request.getDistrict() != null ? request.getDistrict()
                : current != null ? current.getDistrict() : null;
        String street = request.getStreet() != null ? request.getStreet()
                : current != null ? current.getStreet() : null;
        String zipcode = request.getZipcode() != null ? request.getZipcode()
                : current != null ? current.getZipcode() : null;

        return new Address(fullAddress, latitude, longitude, country, region, district, street, zipcode);
    }
}
