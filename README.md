Swagger UI 
http://3.37.22.98:8080/swagger-ui/index.html


*현재 Swagger 내부에서 물품 등록 , 물품 수정 기능이 작동이 안됩니다 . (value값 형태 문제) postman에서는 정상작동 되니,  postman에서 물품 등록 , 수정 해주시고 다른 기능은 swagger에서 부탁드립니다 !*

<User>

- 회원가입 

/api/signup [post]
http://3.37.22.98:8080/api/signup


이메일 유효성 검사 (이메일 형식 확인 --> user@test.com) + 회원가입된 이메일 있으면 예외처리 
비밀번호 유효성 검사 (8~20자리, 영문자와 숫자 조합 --> 123a5678 or ABCD1234)
핸드폰 번호 유효성 검사 (010-1234-5678)

입력값 예시
{
    "email": "user3@example.com",
    "user_password": "Password123456",
    "user_nickname": "nickname",
    "user_phone": "010-1234-5678",
    "user_addr": "사용자 주소",
    "user_img": "사용자 이미지 URL"
}

- 로그인

/api/login [post]
http://3.37.22.98:8080/api/login

입력 값 예시
{
    "email": "user3@example.com",
    "user_password": "Password123456"
}

로그인 성공 시 , 로그인 성공 메시지 + 토큰 발급

- 로그아웃 

/api/logout [post]
http://3.37.22.98:8080/api/logout
입력 값 예시
{
    "email": "user3@example.com"
}

- 회원탈퇴 

/api/unregister/{email} [delete]
http://3.37.22.98:8080/api/unregister/user3@example.com

입력 값 예시 
Authorization Beaber Token에 
해당 이메일에 해당하는 토큰 값 입력 후 요청


- 유저 정보 조회 

/api/users/{userId} [GET]
http://3.37.22.98:8080/api/users/2

userId에 따른 유저 정보 조회 
Authorization Beaber Token에 
해당 이메일에 해당하는 토큰 값 입력 후 요청


<Product>  -- 모든 요청 토큰 있어야 가능 

- 쇼핑몰 전체 물건 조회 

/api/product [get]   
http://3.37.22.98:8080/api/product?page=2&sort=asc 

페이지당 8개의 상품 출력, 페이지는 0부터 시작 즉 page = 2는 3번째 페이지를 의미
sort = asc -> 오름차순 sort = desc -> 내림차순
productStatus가 1인 물건만 조회가능 
여러 사용자가 등록한 물건 조회 가능 

- 쇼핑몰 상세 물건 조회 

/api/product/{product_id} [get]
http://3.37.22.98:8080/api/product/18

재고 0 이상인 물건만 조회 가능 

- 쇼핑몰 판매 물품 등록 

/api/products/register [POST]
http://3.37.22.98:8080/api/products/register

입력 값 예시 

productName, price, startDate, endDate, productOption , description , stock , price 입력 필요
files 통해 사진 업로드 필요 . 

사진 경로 리스트 중 , 리스트의 첫번째 이미지 경로를 imageUrl 에 설정하고 , 이를 대표 이미지로 사용 (쇼핑몰 물건 조회시)
product_Status 속성에 EndDate가 현재 시간과 같거나 , 현재시간보다 늦다면 1을 현재 시간보다 빠르다면 (판매 기한이 끝났다면) 0을 반환  
추가로 config 패키지의 ProductStatusScheduler을 통해 매일 00시에 해당 product_Status 속성을 체크하여 , 판매 기한이 끝나면 자동으로 0으로 설정 
Beaber Token 에 입력한 토큰에 따른 user_id 를 조회해서 자동으로 user_id에 값 할당 
추가로 , FileController 에yaml 파일에 올려둔 
upload:
  dir: D:\ImageTest 
경로에 파일이 업로드 되므로 , 테스트 시에 , 따로 설정하시면 됩니다 . 


- 쇼핑몰 판매 물품 수정

/api/products/{productId} [PUT]
http://3.37.22.98:8080/api/products/24

물품 등록시 필요한 입력값들 + email , password 값 입력 
물품 수정 위해서는 , 등록된 물품의 user_Id 에 해당하는 email , password 값 입력해야 수정가능 

입력 값 예시
{
  "productName": "새로운 상품",
  "price": 40000000,
  "stock": 30,
  "startDate": "2024-05-03",
  "endDate": "2024-05-31",
  "productOption" : "abc",
  "description": "새로운 상품 설명입니다"
}


- 판매중인 물건 조회 
/api/products/user/{userId} [GET] 
http://3.37.22.98:8080/api/products/user/1

사용자 ID에 따라 , 등록한 물건 조회 가능 


- 등록중인 물건 삭제
/api/products/{productId} [DELETE]
http://3.37.22.98:8080/api/products/22 

productId에 따라 , 등록된 물건 삭제 가능. productId에 따라 물품을 등록한 사용자의 userId에 해당하는  email과 password를 입력해야 삭제 가능 


<Cart>


- 장바구니 담기


/api/cart [POST] 
http://3.37.22.98:8080/api/cart
물건 중에서도 product_status 가 1인 물건 (판매중인 물건)만 장바구니 담기 가능

입력 값 : 

{
    "productId": 19,
    "quantity": 2
}

구매원하는 productId 와 수량 입력 후 post
이미 장바구니에 productId 값이 존재한다면 , 추가하지 않고 예외 던짐 


- 장바구니 결제 총액 구하기


/api/cart/total-price/{userId} [get]
http://3.37.22.98:8080/api/cart/6 

마찬가지로 productStatus가 1인 제품만을 고려해서 계산 . 


- 장바구니 리스트 조회 

/api/cart/{userId}[GET]
http://3.37.22.98:8080/api/cart/6

userId에 해당하는 장바구니 리스트 조회 , 만약 장바구니에 담긴 상태에서 다른 사람이 해당 물건을 구매한다해도 , stock 0 상태로 남아있음 



- 장바구니 내역 수정 


/api/cart/{cart_id} [put]
http://3.37.22.98:8080/api/cart/5

해당 cart_id가 존재하는경우 처리 , productStatus가 1인 제품만 업데이트 가능 , 
{
    "productId": 19,
    "quantity": 1
}

원하는 cart_id 를 다른 물건이나 , 수량으로 변경 할 수 있음 



- 장바구니 내역 삭제
  
/api/cart/{cartItemId} [delete]
http://3.37.22.98:8080/api/cart/6

원하는 cart_id 의 장바구니 내역을 삭제 가능 


<Order>

- 장바구니에서 선택 주문

/api/order/{orderedItemId} 
http://3.37.22.98:8080/api/order/7

원하는 cartItemId 를 통해 선택적 주문 가능 , 구매하려는 상품 productStatus = 1이어야 구매가능 ,구매 후 재고가 0이상이어야 구매 가능 , 구매 후 상품의 수량 줄어듬  


- 장바구니 전체 주문 

/api/ordertotal/{userId}
http://3.37.22.98:8080/api/ordertotal/6

구매하려는 상품 productStatus = 1이어야 구매가능 , 구매 후 재고가 0이상이어야 구매 가능 , 구매 후 상품의 수량 줄어듬  



- 주문내역 삭제 

/api/order/{orderedItemId} [delete]
http://3.37.22.98:8080/api/order/11

원하는 구매 물건 삭제 가능 ,  삭제 시에 구매했던 물건 재고 구매 전으로 업데이트 .
주문한 사용자의 userId에 해당하는  email과 password를 입력해야 삭제 가능 


- 주문내역 조회 

/api/order/{userId} [get]
http://3.37.22.98:8080/api/order/1

userId에 따라 해당하는 유저의 주문내역 조회 






