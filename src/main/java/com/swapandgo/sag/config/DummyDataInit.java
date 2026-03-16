package com.swapandgo.sag.config;

import com.swapandgo.sag.domain.Image;
import com.swapandgo.sag.domain.WishList;
import com.swapandgo.sag.domain.item.Category;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.item.ItemType;
import com.swapandgo.sag.domain.item.TradeType;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.domain.user.Address;
import com.swapandgo.sag.domain.user.User;
import com.swapandgo.sag.repository.ItemRepository;
import com.swapandgo.sag.repository.TransactionRepository;
import com.swapandgo.sag.repository.UserRepository;
import com.swapandgo.sag.repository.WishListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component

public class DummyDataInit implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final PasswordEncoder passwordEncoder;
    private final WishListRepository wishListRepository;
    private final TransactionRepository transactionRepository;


    public DummyDataInit(UserRepository userRepository, PasswordEncoder passwordEncoder,
                         ItemRepository itemRepository, WishListRepository wishListRepository, TransactionRepository transactionRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.itemRepository = itemRepository;
        this.wishListRepository = wishListRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
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
            item17.completed();
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
            item26.completed();
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

            // --- 추가 데이터 (item 31 ~ 50) ---

            // 1. 대여 상품 (ItemType.RENTAL) - 10개
            Item item31 = Item.create(user, "캠핑용 텐트 4인용", "주말 캠핑용으로 제격",
                    new BigDecimal("30000"), new BigDecimal("200000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Seoul", headphoneImage); // 이미지 적절히 변경 가능
            itemRepository.save(item31);

            Item item32 = Item.create(user, "DSLR 카메라 대여", "여행 시 고화질 촬영",
                    new BigDecimal("50000"), new BigDecimal("1000000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item32);

            Item item33 = Item.create(user, "전동 드릴 세트", "이사/가구 조립용",
                    new BigDecimal("15000"), new BigDecimal("150000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", headphoneImage);
            itemRepository.save(item33);

            Item item34 = Item.create(user, "고급 슈트(남성용)", "결혼식/면접용 1회 대여",
                    new BigDecimal("40000"), new BigDecimal("400000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Seoul", sofaImage);
            itemRepository.save(item34);

            Item item35 = Item.create(user, "빔프로젝터 풀HD", "영화 감상용 홈시어터",
                    new BigDecimal("25000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item35);

            Item item36 = Item.create(user, "여행용 캐리어 대형", "한달 유럽 여행용",
                    new BigDecimal("10000"), new BigDecimal("200000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", sofaImage);
            itemRepository.save(item36);

            Item item37 = Item.create(user, "전동 킥보드", "동네 마실용 대여",
                    new BigDecimal("12000"), new BigDecimal("600000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item37);

            Item item38 = Item.create(user, "파티용 블루투스 스피커", "출력이 어마어마합니다",
                    new BigDecimal("35000"), new BigDecimal("450000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item38);

            Item item39 = Item.create(user, "보드게임 5종 세트", "친구들과 홈파티용",
                    new BigDecimal("8000"), new BigDecimal("100000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", sofaImage);
            itemRepository.save(item39);

            Item item40 = Item.create(user, "미니 제습기", "장마철 한시적 대여",
                    new BigDecimal("15000"), new BigDecimal("120000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item40);


            // 2. 중고거래 상품 (ItemType.RESALE) - 10개
            Item item41 = Item.create(user, "기계식 키보드 청축", "타건감 좋습니다",
                    new BigDecimal("55000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            item41.completed();
            itemRepository.save(item41);

            Item item42 = Item.create(user, "커피머신 에스프레소", "홈카페 입문용",
                    new BigDecimal("120000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item42);

            Item item43 = Item.create(user, "식탁 의자 2세트", "원목 스타일",
                    new BigDecimal("60000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            item43.completed();
            itemRepository.save(item43);

            Item item44 = Item.create(user, "태블릿 PC 10인치", "인강 시청용",
                    new BigDecimal("210000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item44);

            Item item45 = Item.create(user, "모니터 받침대", "깔끔한 책상 정리",
                    new BigDecimal("10000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item45);

            Item item46 = Item.create(user, "무선 마우스 게이밍", "반응속도 빠름",
                    new BigDecimal("35000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item46);

            Item item47 = Item.create(user, "전신 거울", "방이 넓어보여요",
                    new BigDecimal("25000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item47);

            Item item48 = Item.create(user, "북케이스 책장", "3단 책장입니다",
                    new BigDecimal("40000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item48);

            Item item49 = Item.create(user, "에어프라이어 5L", "대용량입니다",
                    new BigDecimal("45000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item49);

            Item item50 = Item.create(user, "스탠드 조명", "무드등으로 사용 가능",
                    new BigDecimal("18000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            item50.completed();
            itemRepository.save(item50);

            WishList wish1 = WishList.create(user, item30);
            wishListRepository.save(wish1);
            WishList wish2 = WishList.create(user, item5);
            wishListRepository.save(wish2);
            WishList wish3 = WishList.create(user, item20);
            wishListRepository.save(wish3);
            WishList wish4 = WishList.create(user, item41);
            wishListRepository.save(wish4);


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

            // --- 추가 데이터 (item 31 ~ 50) ---

            // 1. 대여 상품 (ItemType.RENTAL) - 10개
            Item item31 = Item.create(user, "캠핑용 텐트 4인용", "주말 캠핑용으로 제격",
                    new BigDecimal("30000"), new BigDecimal("200000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Seoul", headphoneImage); // 이미지 적절히 변경 가능
            itemRepository.save(item31);

            Item item32 = Item.create(user, "DSLR 카메라 대여", "여행 시 고화질 촬영",
                    new BigDecimal("50000"), new BigDecimal("1000000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item32);

            Item item33 = Item.create(user, "전동 드릴 세트", "이사/가구 조립용",
                    new BigDecimal("15000"), new BigDecimal("150000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", headphoneImage);
            itemRepository.save(item33);

            Item item34 = Item.create(user, "고급 슈트(남성용)", "결혼식/면접용 1회 대여",
                    new BigDecimal("40000"), new BigDecimal("400000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Seoul", sofaImage);
            itemRepository.save(item34);

            Item item35 = Item.create(user, "빔프로젝터 풀HD", "영화 감상용 홈시어터",
                    new BigDecimal("25000"), new BigDecimal("500000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item35);

            Item item36 = Item.create(user, "여행용 캐리어 대형", "한달 유럽 여행용",
                    new BigDecimal("10000"), new BigDecimal("200000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", sofaImage);
            itemRepository.save(item36);

            Item item37 = Item.create(user, "전동 킥보드", "동네 마실용 대여",
                    new BigDecimal("12000"), new BigDecimal("600000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item37);

            Item item38 = Item.create(user, "파티용 블루투스 스피커", "출력이 어마어마합니다",
                    new BigDecimal("35000"), new BigDecimal("450000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item38);

            Item item39 = Item.create(user, "보드게임 5종 세트", "친구들과 홈파티용",
                    new BigDecimal("8000"), new BigDecimal("100000"), ItemType.RENTAL,
                    TradeType.SELL, Category.기타, "Berlin", sofaImage);
            itemRepository.save(item39);

            Item item40 = Item.create(user, "미니 제습기", "장마철 한시적 대여",
                    new BigDecimal("15000"), new BigDecimal("120000"), ItemType.RENTAL,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item40);


            // 2. 중고거래 상품 (ItemType.RESALE) - 10개
            Item item41 = Item.create(user, "기계식 키보드 청축", "타건감 좋습니다",
                    new BigDecimal("55000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item41);

            Item item42 = Item.create(user, "커피머신 에스프레소", "홈카페 입문용",
                    new BigDecimal("120000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Berlin", headphoneImage);
            itemRepository.save(item42);

            Item item43 = Item.create(user, "식탁 의자 2세트", "원목 스타일",
                    new BigDecimal("60000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Seoul", sofaImage);
            itemRepository.save(item43);

            Item item44 = Item.create(user, "태블릿 PC 10인치", "인강 시청용",
                    new BigDecimal("210000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Warsaw", headphoneImage);
            itemRepository.save(item44);

            Item item45 = Item.create(user, "모니터 받침대", "깔끔한 책상 정리",
                    new BigDecimal("10000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item45);

            Item item46 = Item.create(user, "무선 마우스 게이밍", "반응속도 빠름",
                    new BigDecimal("35000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item46);

            Item item47 = Item.create(user, "전신 거울", "방이 넓어보여요",
                    new BigDecimal("25000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item47);

            Item item48 = Item.create(user, "북케이스 책장", "3단 책장입니다",
                    new BigDecimal("40000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Berlin", sofaImage);
            itemRepository.save(item48);

            Item item49 = Item.create(user, "에어프라이어 5L", "대용량입니다",
                    new BigDecimal("45000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.전자기기, "Seoul", headphoneImage);
            itemRepository.save(item49);

            Item item50 = Item.create(user, "스탠드 조명", "무드등으로 사용 가능",
                    new BigDecimal("18000"), null, ItemType.RESALE,
                    TradeType.SELL, Category.가구, "Warsaw", sofaImage);
            itemRepository.save(item50);

            WishList wish1 = WishList.create(user, item29);
            wishListRepository.save(wish1);
            WishList wish2 = WishList.create(user, item27);
            wishListRepository.save(wish2);
            WishList wish3 = WishList.create(user, item1);
            wishListRepository.save(wish3);
            WishList wish4 = WishList.create(user, item41);
            wishListRepository.save(wish4);

            User user1 = userRepository.findById(1L).get();
            LocalDateTime now = LocalDateTime.now();
            // 1. 과거 거래 (1주일 전 ~ 현재 사이) - 이미 완료되었거나 진행 중인 대여
            // item31: 캠핑용 텐트 (대여)
            Transaction trans1 = Transaction.create(
                    user1, item31, item31.getType(),
                    now.minusDays(7), now.plusDays(4) // 7일 전 시작, 4일 전 종료 (완료)
            );
            transactionRepository.save(trans1);
            item31.completed();

            // item32: DSLR 카메라 (대여)
            Transaction trans2 = Transaction.create(
                    user1, item32, item32.getType(),
                    now.minusDays(3), now.plusDays(2) // 3일 전 시작, 2일 후 종료 (진행 중)
            );
            transactionRepository.save(trans2);
            item32.completed();

            // 2. 현재 시점 거래 (오늘)
            Transaction trans3 = Transaction.create(
                    user1, item36, item36.getType(),
                    now.minusDays(7), now.plusDays(7)
            );
            transactionRepository.save(trans3);
            item36.completed();

            Transaction trans4 = Transaction.create(
                    user1, item33, item33.getType(),
                    now.minusDays(7), now.plusDays(7) // 중고거래는 시작/종료를 당일로 설정
            );
            transactionRepository.save(trans4);
            item33.completed();


        }
    }
}
