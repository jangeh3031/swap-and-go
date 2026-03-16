package com.swapandgo.sag.domain.request;

import com.swapandgo.sag.domain.item.Item;
import com.swapandgo.sag.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "request")
public class Request {
    @Id @GeneratedValue
    @Column(name = "request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime startAt;
    LocalDateTime endAt;

    //생성 메서드
    public static Request create(
            User requester,
            Item item,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {
        Request r = new Request();
        r.requester = requester;
        r.item = item;
        r.startAt = startAt;
        r.endAt = endAt;
        r.status = RequestStatus.PENDING;
        r.createdAt = LocalDateTime.now();
        r.updatedAt = LocalDateTime.now();
        return r;
    }

    public void accept(){
        ensurePending();
        this.status = RequestStatus.ACCEPTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(){
        ensurePending();
        this.status = RequestStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelByRequester(User requester){
        ensurePending();
        if(!isRequester(requester))
            throw new SecurityException("본인 요청만 취소 가능합니다.");
        this.status = RequestStatus.CANCELED;
        this.updatedAt = LocalDateTime.now();

    }

    public boolean isRequester(User user){
        return this.requester != null && this.requester.equals(user);
    }

    public void ensurePending(){
        if(this.status != RequestStatus.PENDING)
            throw new IllegalStateException("대기중 상태에서만 처리 가능합니다.");
    }

    //public void isFor(){}



}
