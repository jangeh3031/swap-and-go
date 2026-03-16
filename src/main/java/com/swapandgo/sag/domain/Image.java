package com.swapandgo.sag.domain;


import com.swapandgo.sag.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Image {
    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(name = "is_main", nullable = false)
    private boolean isMain = false;

    //생성 메서드
    public static Image create(String url){
        Image image = new Image();
        image.url = url;
        return image;
    }

    //비즈니스 로직

    //메인 사진 여부를 바꾸기
    public void markAsMain(){
        this.isMain = true;
    }
    public void unmarkAsMain(){
        this.isMain = false;
    }

    //url 변경(이미지 교체)
    //todo

}
