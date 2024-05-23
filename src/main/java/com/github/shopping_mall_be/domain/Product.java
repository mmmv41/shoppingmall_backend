package com.github.shopping_mall_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    //User 엔티티와의 관계: user_id는 User 엔티티와의 외래 키 관계를 나타내므로, User 엔티티 클래스도 정의되어 있어야 합니다. 여기서는 ManyToOne 관계를 사용하여 Product 엔티티가 여러 User 엔티티와 연결될 수 있도록 합니다.
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(columnDefinition = "TEXT") // db에서 text타입으로 저장.
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    // 옵션 추가
    @Column(name = "product_option")
    private String productOption;

    // JPA에서 날짜를 매핑할 때 사용. 필드를 db의 DATE유형에 매핑. 연도,월,일 정보만 저장(시간 정보는 무시)
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    // 이미지 파일 경로를 저장할 필드
    @ElementCollection // 여러 이미지 경로를 저장하기 위해 사용. 컬렉션 매핑
    @CollectionTable(name = "ProductImage", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_path")
    private List<String> imagePaths = new ArrayList<>(); // 이미지 경로들을 저장하기 위한 리스트

    // 이미지의 최대 개수를 지정하기 위한 로직
    public boolean canAddImage() { // 이미지를 추가할 수 있는지 여부 확인. 최대 이미지 개수 초과하지않는지 확인.
        return imagePaths.size() < 10;
    }

    public void addImage(String imagePath) { // 상품에 이미지 추가. 추가 전에 canAddImage()메서드로 최대 이미지 개수 확인.
        if (canAddImage()) {
            imagePaths.add(imagePath);
        } else {
            throw new IllegalStateException("사진은 10개까지 등록 가능합니다.");
        }
    }
}
