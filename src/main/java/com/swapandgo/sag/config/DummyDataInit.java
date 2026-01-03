package com.swapandgo.sag.config;

import com.swapandgo.sag.domain.Image;
import com.swapandgo.sag.domain.WishList;
import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.repository.WishListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component

public class DummyDataInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final WishListRepository wishListRepository;


    public DummyDataInit(UserRepository userRepository, PasswordEncoder passwordEncoder,
                         ItemRepository itemRepository, WishListRepository wishListRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public void run(String... args) {
        //사용자1 추가 및 사용자1의 작성한 게시물들 추가
        if (userRepository.findByEmail("jangeh3031@naver.com").isEmpty()) {
            String encodedPassword = passwordEncoder.encode("password");
            Address address = new Address("Germany", "Ulm", "Syrlinstrasse 8");
            User user = User.createUser("장은호", "jangeh3031@naver.com", encodedPassword, address);
            userRepository.save(user);

            //헤드폰 이미지
            List<String> headphoneImage = new ArrayList<>();
            headphoneImage.add("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop");

            //소파 이미지
            List<String> sofaImage = new ArrayList<>();
            sofaImage.add("https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400&h=400&fit=crop");

            Item item1 = Item.create(user, "헤드폰 ver10", "산지 1년된 미개봉 중고",
                    new BigDecimal("100000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item1);
            Item item2 = Item.create(user, "헤드폰 ver11", "산지 1년된 미개봉 중고",
                    new BigDecimal("200000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item2);
            Item item3 = Item.create(user, "8인용 소파", "5년 사용중",
                    new BigDecimal("100000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", sofaImage);
            itemRepository.save(item3);
            Item item4 = Item.create(user, "4인용 소파", "2년 사용중",
                    new BigDecimal("100000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item4);
            Item item5 = Item.create(user, "헤드폰 ver12", "산지 1년된 미개봉 중고",
                    new BigDecimal("100000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item5);
            Item item6 = Item.create(user, "헤드폰 ver13", "산지 1년된 미개봉 중고",
                    new BigDecimal("20000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item6);
            Item item7 = Item.create(user, "헤드폰 ver14", "산지 1년된 미개봉 중고",
                    new BigDecimal("1000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item7);
            Item item8 = Item.create(user, "헤드폰 ver15", "산지 1년된 미개봉 중고",
                    new BigDecimal("20000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item8);

            Item item9 = Item.create(user, "노이즈캔슬링 헤드폰", "거의 새것 같은 중고",
                    new BigDecimal("150000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item9);

            Item item10 = Item.create(user, "무선 헤드폰 프리미엄", "1개월 사용",
                    new BigDecimal("180000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item10);

            Item item11 = Item.create(user, "3인용 패브릭 소파", "깨끗한 상태",
                    new BigDecimal("250000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item11);

            Item item12 = Item.create(user, "블루투스 헤드폰", "저렴하게 판매",
                    new BigDecimal("45000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item12);

            Item item13 = Item.create(user, "고급 가죽 소파", "거의 사용 안함",
                    new BigDecimal("800000"), null, ItemType.RESALE,
                    TradeType.SELL, Category. 가구, "Warsaw", sofaImage);
            itemRepository.save(item13);

            Item item14 = Item.create(user, "게이밍 헤드폰 RGB", "박스 포함",
                    new BigDecimal("95000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item14);

            Item item15 = Item.create(user, "L자형 코너 소파", "3년 사용",
                    new BigDecimal("450000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item15);

            Item item16 = Item.create(user, "스튜디오 헤드폰 모니터링용", "전문가용",
                    new BigDecimal("320000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item16);

            Item item17 = Item.create(user, "2인용 소파베드", "변형 가능",
                    new BigDecimal("180000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item17);

            Item item18 = Item.create(user, "초저가 헤드폰 정리", "급매",
                    new BigDecimal("15000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item18);

            Item item19 = Item.create(user, "빈티지 체스터필드 소파", "앤틱 느낌",
                    new BigDecimal("950000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item19);

            Item item20 = Item. create(user, "무선 헤드폰 ver20", "배터리 좋음",
                    new BigDecimal("88000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item20);

            Item item21 = Item.create(user, "미니 2인 소파", "원룸용 최적",
                    new BigDecimal("120000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item21);

            Item item22 = Item.create(user, "프리미엄 헤드폰 하이엔드", "최상급",
                    new BigDecimal("650000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item22);

            Item item23 = Item.create(user, "패브릭 소파 5인용", "대가족용",
                    new BigDecimal("550000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item23);

            Item item24 = Item.create(user, "학생용 헤드폰 저렴", "공부용",
                    new BigDecimal("25000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item24);

            Item item25 = Item.create(user, "리클라이너 소파", "전동 조절",
                    new BigDecimal("720000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            item25.completed();
            itemRepository.save(item25);

            Item item26 = Item.create(user, "DJ 헤드폰 프로페셔널", "클럽용",
                    new BigDecimal("280000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item26);

            Item item27 = Item.create(user, "모듈러 소파 조합형", "자유로운 배치",
                    new BigDecimal("380000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item27);

            Item item28 = Item.create(user, "접이식 헤드폰 휴대용", "여행용",
                    new BigDecimal("52000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item28);

            Item item29 = Item.create(user, "벨벳 소파 럭셔리", "호텔 스타일",
                    new BigDecimal("890000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item29);

            Item item30 = Item.create(user, "유선 헤드폰 고음질", "오디오파일용",
                    new BigDecimal("420000"), null, ItemType.RESALE,
                    TradeType.SELL, Category. 전자기기, "Berlin", headphoneImage);
            itemRepository.save(item30);

            WishList wish1 = WishList.create(user, item30);
            wishListRepository.save(wish1);
            WishList wish2 = WishList.create(user, item5);
            wishListRepository.save(wish2);
            WishList wish3 = WishList.create(user, item20);
            wishListRepository.save(wish3);



        }

        //사용자2 추가 및 사용자2의 작성한 게시물들 추가
        if (userRepository.findByEmail("jang3031@naver.com").isEmpty()) {
            String encodedPassword = passwordEncoder.encode("password");
            Address address = new Address("Germany", "Berlin", "strasse 10");
            User user = User.createUser("은호", "jang3031@naver.com", encodedPassword, address);
            userRepository.save(user);

            //헤드폰 이미지
            List<String> headphoneImage = new ArrayList<>();
            headphoneImage.add("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop");

            //소파 이미지
            List<String> sofaImage = new ArrayList<>();
            sofaImage.add("https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=400&h=400&fit=crop");

            Item item1 = Item.create(user, "헤드폰 ver10", "아무거나 연락주세요",
                    new BigDecimal("100000"), null, ItemType.RESALE,
                    TradeType.BUY, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item1);
            Item item2 = Item.create(user, "헤드폰 ver11", "산지 1년된 미개봉 중고",
                    new BigDecimal("200000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item2);
            Item item3 = Item.create(user, "8인용 소파", "5년 사용중",
                    new BigDecimal("100000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", sofaImage);
            itemRepository.save(item3);
            Item item4 = Item.create(user, "4인용 소파", "2년 사용중",
                    new BigDecimal("100000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item4);
            Item item5 = Item.create(user, "헤드폰 ver12", "산지 1년된 미개봉 중고",
                    new BigDecimal("100000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item5);
            Item item6 = Item.create(user, "헤드폰 ver13", "1년 안으로 된 제품 삽니다",
                    new BigDecimal("20000"), null, ItemType.RESALE,
                    TradeType.BUY, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item6);
            Item item7 = Item.create(user, "헤드폰 ver14", "산지 1년된 미개봉 중고",
                    new BigDecimal("1000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item7);
            Item item8 = Item.create(user, "헤드폰 ver15", "산지 1년된 미개봉 중고",
                    new BigDecimal("20000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item8);

            Item item9 = Item.create(user, "노이즈캔슬링 헤드폰", "거의 새것 같은 중고",
                    new BigDecimal("150000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item9);

            Item item10 = Item.create(user, "무선 헤드폰 프리미엄", "1개월 사용",
                    new BigDecimal("180000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item10);

            Item item11 = Item.create(user, "3인용 패브릭 소파", "깨끗한 상태",
                    new BigDecimal("250000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item11);

            Item item12 = Item.create(user, "블루투스 헤드폰", "저렴하게 판매",
                    new BigDecimal("45000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item12);

            Item item13 = Item.create(user, "고급 가죽 소파", "거의 사용 안함",
                    new BigDecimal("800000"), null, ItemType.RESALE,
                    TradeType.SELL, Category. 가구, "Warsaw", sofaImage);
            itemRepository.save(item13);

            Item item14 = Item.create(user, "게이밍 헤드폰 RGB", "박스 포함",
                    new BigDecimal("95000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item14);

            Item item15 = Item.create(user, "L자형 코너 소파", "3년 사용",
                    new BigDecimal("450000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item15);

            Item item16 = Item.create(user, "스튜디오 헤드폰 모니터링용", "전문가용",
                    new BigDecimal("320000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item16);

            Item item17 = Item.create(user, "2인용 소파베드", "변형 가능",
                    new BigDecimal("180000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item17);

            Item item18 = Item.create(user, "초저가 헤드폰 정리", "급매",
                    new BigDecimal("15000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            item18.completed();
            itemRepository.save(item18);

            Item item19 = Item.create(user, "빈티지 체스터필드 소파", "앤틱 느낌",
                    new BigDecimal("950000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item19);

            Item item20 = Item. create(user, "무선 헤드폰 ver20", "배터리 좋음",
                    new BigDecimal("88000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item20);

            Item item21 = Item.create(user, "미니 2인 소파", "원룸용 최적",
                    new BigDecimal("120000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item21);

            Item item22 = Item.create(user, "프리미엄 헤드폰 하이엔드", "최상급",
                    new BigDecimal("650000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item22);

            Item item23 = Item.create(user, "패브릭 소파 5인용", "대가족용",
                    new BigDecimal("550000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item23);

            Item item24 = Item.create(user, "학생용 헤드폰 저렴", "공부용",
                    new BigDecimal("25000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item24);

            Item item25 = Item.create(user, "리클라이너 소파", "전동 조절",
                    new BigDecimal("720000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item25);

            Item item26 = Item.create(user, "DJ 헤드폰 프로페셔널", "클럽용",
                    new BigDecimal("280000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item26);

            Item item27 = Item.create(user, "모듈러 소파 조합형", "자유로운 배치",
                    new BigDecimal("380000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item27);

            Item item28 = Item.create(user, "접이식 헤드폰 휴대용", "여행용",
                    new BigDecimal("52000"), null, ItemType.RESALE,
                    TradeType.BUY, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item28);

            Item item29 = Item.create(user, "벨벳 소파 럭셔리", "호텔 스타일",
                    new BigDecimal("890000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item29);

            Item item30 = Item.create(user, "유선 헤드폰 고음질", "오디오파일용",
                    new BigDecimal("420000"), null, ItemType.RESALE,
                    TradeType.SELL, Category. 전자기기, "Berlin", headphoneImage);
            itemRepository.save(item30);

            WishList wish1 = WishList.create(user, item29);
            wishListRepository.save(wish1);
            WishList wish2 = WishList.create(user, item27);
            wishListRepository.save(wish2);
            WishList wish3 = WishList.create(user, item1);
            wishListRepository.save(wish3);
        }
    }
}
