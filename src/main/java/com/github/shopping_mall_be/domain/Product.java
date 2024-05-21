package com.github.shopping_mall_be.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "user_id" , nullable = false)
    private Integer userId;

    //User 엔티티와의 관계: user_id는 User 엔티티와의 외래 키 관계를 나타내므로, User 엔티티 클래스도 정의되어 있어야 합니다. 여기서는 ManyToOne 관계를 사용하여 Product 엔티티가 여러 User 엔티티와 연결될 수 있도록 합니다.
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private String userId;

    @Column(name = "product_name", nullable = false, length = 255)
    private String productName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer stock;


    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Temporal(TemporalType.DATE)
    private Date saleDate;

    private Integer quantity;

    @Column(name = "total_price")
    private Integer totalPrice;

//    @Column(name = "image_url")
//    private String imageUrl;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}