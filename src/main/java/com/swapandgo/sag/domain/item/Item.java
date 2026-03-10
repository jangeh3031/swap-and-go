package com.swapandgo.sag.domain.item;

import com.swapandgo.sag.domain.Image;
import com.swapandgo.sag.domain.request.Request;
import com.swapandgo.sag.domain.transaction.Transaction;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "item")
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Column(precision = 10, scale = 2)
    private BigDecimal deposit;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    @Enumerated(EnumType.STRING)
    private TradeType tradeType;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String location;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성 메서드
    public static Item create(
            User user, String title, String content, BigDecimal price,
            BigDecimal deposit, ItemType type, TradeType tradeType, Category category,
            String location, List<String> imageUrls
    ){
        Item item = new Item();
        item.setUser(user);
        item.title = title;
        item.content = content;
        item.price = price;
        item.deposit = deposit;
        item.type = type;
        item.tradeType = tradeType;
        item.category = category;
        item.status = ItemStatus.ACTIVE;
        item.location = location;
        item.createdAt = LocalDateTime.now();
        item.updatedAt = LocalDateTime.now();

        //첫번째 이미지를 썸네일로 설정
        if(imageUrls != null && !imageUrls.isEmpty()){
            for (int i = 0; i < imageUrls.size(); i++){
                Image image = Image.create(imageUrls.get(i));
                if(i == 0){
                    image.markAsMain();
                }

                item.addImage(image);
            }
        }
        //item.validate();
        return item;

    }



    //연관관계 메서드
    public void addImage(Image image){
        images.add(image);
        image.setItem(this);
    }

    public void setUser(User user){
        this.user = user;
        user.getItems().add(this);
    }

    //유효성 검사
    public void validate(){
        if(this.title == null || this.title.isBlank())
            throw new IllegalArgumentException("제목을 입력해야 합니다.");
        if(this.content == null || this.content.isBlank())
            throw new IllegalArgumentException("본문 내용을 입력해야 합니다.");
        if(this.price == null || this.price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        if(this.type == ItemType.RENTAL && this.deposit == null || this.deposit.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("대여 상품은 보증금이 필요합니다.");
    }

    //거래 완료 상태로 바꾸기 (중고 거래 요청 수락 했을 때, 대여 수락 했을 때)
    public void completed(){
        if(this.status == ItemStatus.COMPLETED || this.status == ItemStatus.RENTED)
            throw new IllegalStateException("이미 완료된 게시글입니다.");
        if (this.deposit == null){
            this.status = ItemStatus.COMPLETED;
        }else {
            this.status = ItemStatus.RENTED;
        }
    }

    //거래 상태 활성화로 바꾸기 (대여 기간 끝났을때?)
    public void activate(){
        if(this.status == ItemStatus.ACTIVE)
            throw new IllegalStateException("이미 활성화된 게시글입니다.");
        this.status = ItemStatus.ACTIVE;
    }

    //게시글 수정
    public void update(
            String title, String content, BigDecimal price, BigDecimal deposit,
            Category category, TradeType tradeType){
        this.title = title;
        this.content = content;
        this.price = price;
        this.deposit = deposit;
        this.category = category;
        this.tradeType = tradeType;
        this.updatedAt = LocalDateTime.now();

        //validate();
    }

    //대표 이미지 조회
    public String getThumbnailUrl(){
        // 1. isMain이 true인 이미지 찾기
        for (Image image : images){
            if(image.isMain()){
                return image.getUrl();
            }
        }

        // 2. isMain이 없으면 첫 번째 이미지 반환
        if(! images.isEmpty()){
            return images.get(0).getUrl();
        }

        // 3. 이미지가 아예 없으면 null
        return null;
    }

    //게시글 작성자 확인
    public boolean isOwner(User user){
        return this.user != null && this.user.equals(user);
    }


    //요청 메서드 -> 요청자와 대여 시작, 끝 기간을 파라미터로 받는다(중고거래일 경우 값 x)
    public Request addRequestForm(User requester, LocalDateTime startAt, LocalDateTime endAt){
        if(requester.equals(this.user)){
            throw new IllegalStateException("본인 글에는 요청을 보낼 수 없습니다.");
        }

        if (this.type == ItemType.RESALE){
            startAt = null;
            endAt = null;
        } else if (this.type == ItemType.RENTAL) {
            if (startAt == null || endAt == null) {
                throw new IllegalArgumentException("대여 요청에는 대여 기간이 필요합니다.");
            }
        } else {
                throw new IllegalStateException("지원하지 않는 거래 타입입니다.");
        }


        Request request = Request.create(requester, this, startAt, endAt);

        // 양방향 연관관계 동기화
        requester.getSentRequests().add(request);

        return request;
    }




//    public WishList toggleWish(User user){
//    } item 에서 찜 리스트를 가지고 있지 않을 거라서 찜 추가 기능은 유저에서 진행

}
