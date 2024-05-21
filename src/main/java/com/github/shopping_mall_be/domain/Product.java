package com.github.shopping_mall_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    //User 엔티티와의 관계: user_id는 User 엔티티와의 외래 키 관계를 나타내므로, User 엔티티 클래스도 정의되어 있어야 합니다. 여기서는 ManyToOne 관계를 사용하여 Product 엔티티가 여러 User 엔티티와 연결될 수 있도록 합니다.
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(columnDefinition = "TEXT") // db에서 text타입으로 저장.
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    // JPA에서 날짜를 매핑할 때 사용. 필드를 db의 DATE유형에 매핑. 연도,월,일 정보만 저장(시간 정보는 무시)
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Temporal(TemporalType.DATE)
    private Date saleDate;

    private int quantity;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
