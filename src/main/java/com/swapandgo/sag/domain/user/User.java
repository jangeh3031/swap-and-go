package com.swapandgo.sag.domain.user;

import com.swapandgo.sag.domain.tradeoffer.TradeOffer;
import com.swapandgo.sag.domain.WishList;
import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.transaction.Transaction;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
public class User {
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String email;

    private String password;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();

    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<TradeOffer> sentTradeOffers = new ArrayList<>();

    @OneToMany(mappedBy = "buyer")
    private List<Transaction> transactions = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //생성 메서드
    public static User createUser(String username, String email, String password,
                                  Address address){
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.address = address;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }

    //비즈니스 로직

    //회원 정보 수정
    public void updateProfile(String username, String password, Address address){
        if(username != null && !username.isBlank()) this.username = username;
        if(password != null && !password.isBlank()) this.password = password;
        this.address = address;
        this.updatedAt = LocalDateTime.now();
    }

    //회원 탈퇴 요청 시, 대여중인 물품이 있는지 체크하는 로직
    //todo
    public boolean UserDelete(){
        return true;
    }

    //찜 추가
    public void addWish(Item item){
        WishList wishList = WishList.create(this, item);
        this.wishLists.add(wishList);
    }

    //요청을 보낸 user의 클래스에서 확정 메서드를 호출하는 구조
    public Transaction confirmSentTransaction(TradeOffer tradeOffer){

        //게시물이 중고거래 일때는 기간 null
        Transaction transaction = Transaction.create(tradeOffer.getRequester(), tradeOffer.getItem(), tradeOffer.getItem().getType(),
                tradeOffer.getStartAt(), tradeOffer.getEndAt());

        transactions.add(transaction);
//        request.getItem().getUser().transactions.add(transaction);
        return transaction;

    }

}
