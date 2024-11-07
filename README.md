# java-convenience-store-precourse
## 기능 목록
### 입출력

#### 입력
- [ ] 상품 목록과 행사 목록을 파일 입출력을 통해 불러온다.
- [ ] 구매할 상품과 수량을 입력 받는다. 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분한다.
- [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼 추가 여부를 입력받는다.
- [ ] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부를 입력받는다.
- [ ] 멤버십 할인 적용 여부를 입력 받는다.
- [ ] 추가 구매 여부를 입력 받는다.

#### 출력
- [ ] 환영 인사와 함께 상품명, 가격, 프로모션 이름, 재고를 안내한다.
- [ ] 만약 재고가 0개라면 `재고 없음`을 출력한다.
- [ ] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량만큼 가져오지 않았을 경우, 혜택에 대한 안내 메시지를 출력한다.
- [ ] 프로모션 재고가 부족하여 일부 수량을 프로모션 혜택 없이 결제해야 하는 경우, 일부 수량에 대해 정가로 결제할지 여부에 대한 안내 메시지를 출력한다.
- [ ] 멤버십 할인 적용 여부를 확인하기 위해 안내 문구를 출력한다.
- [ ] 멤버십 할인 적용 여부를 입력 받는다.
- [ ] 추가 구매 여부를 입력 받는다.


### 상태와 동작

#### 재고 하나: Item
> 이름, 가격, 프로모션 소유

- [x] 프로모션의 항목에 따라 현재 재고가 얼마나 남았는지 반환한다.
- [x] 필요한 수량에 맞춰 부족한 수량을 반환한다.
- [x] 자신의 개수 상태를 변경한다.

#### 총 재고: Inventory
> 각 재고 아이템들 소유
- [x] 재고를 고려하여 결제가 가능한지 알려준다.
- [x] 재고를 차감할 수 있다.
- [x] 프로모션 재고가 다 떨어졌을 때에는 일반 재고를 차감한다.(애매하게 1개 남았을 때도)
- [x] 프로모션 재고 판매 가능 개수를 반환한다.
- [x] 한 제품의 모든 재고 개수를 반환한다.

- [ ] 현재 재고의 상태를 문자열로 반환한다.

#### 영수증: Receipt
> 영수증 정보 내역 소유
- [x] 영수증의 내역을 저장하고, 문자열 값으로 변환한다.


#### 프로모션: Promotion
> 이름, 적용구매 개수, 혜택 개수, 시작기간, 끝나는 기간 소유
- [x] 프로모션 기간이 맞는지 확인한다.
- [x] 해당 프로모션이 맞는지 확인한다.

#### 편의점: ConvenienceStore implements Store
> 총 재고, 영수증 소유

### 동작
### FileReader
- [x] 파일을 읽어온다.

#### Parser
- [x] 문자열을 기준으로 promotions에 맞는 데이터 타입으로 파싱한다.
- [x] 문자열을 기준으로 items에 맞는 데이터 타입으로 파싱한다.

#### InventoryFactory
- [x] Reader에서 읽어온 값을 Parser로 파싱 후 재고를 만들고, Inventory를 생성한다.
- [x] FileReader, ConvenienceStoreParser 주입을 받는다.

#### Discount(interface) extends PromotionDiscount, MembershipDiscount
- [ ] 각 할인을 총 정리한다.

#### PromotionDiscount(interface)
> 프로모션 할인
- [ ] 프로모션의 값만큼 할인한다.

#### MembershipDiscount(interface)
> 멤버십 할인
- [ ] 프로모션된 물품을 제외한 나머지 상품의 재고를 확인하고 할인한다.
- [ ] 할인 퍼센트는 주입받는다.

#### DiscountService implements Discount
- [ ] 각 할인을 상황에 맞게 할인한다.

#### ReceiptBuilder
- [ ] 영수증을 업데이트한다.



### 유틸