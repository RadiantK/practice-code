# 책임 할당하기

데이터 중심 설계로 인해 발생하는 문제점을 해결할 수 있는 가장 기본적인 방법은 책임에 초점을 맞추는 것이다.  

<br/>
<br/>

## 책임 주도 설계를 향해
책임 중심 설계로 전환하기 위한 원칙
- 데이터보다 행동을 먼저 결정하라
- 협력이라는 문맥 안에서 책임을 결정하라  
<br/>

### 데이터보다 행동을 먼저 결정하라
객체에게 중요한 것은 데이터가 아니라 외부에 제공하는 행동이다. 클라이언트의 관점에서 객체가 수행하는 행동이란 곧 객체의 책임을 의미한다.  
객체는 협력에 참여하기 위해 존재하며 협력 안에서 수행하는 책임이 객체의 존재가치를 증명한다.  
<br/>

책임 중심 설계에서는 "이 객체가 수행해야 하는 책임은 무엇인가"를 결정한 후에 "이 책임을 수행하는 데 필요한 데이터는 무엇인가"를 결정한다.  
즉, 책임을 먼저 결정한 후에 객체의 상태를 결정한다.  
<br/>

### 협력이라는 문맥 안에서 책임을 결정하라
책임은 객체의 입장이 아니라 객체가 참여하는 협력에 적합해야 한다.  
협력에 적합한 책임이란 메시지 수신자가 아니라 메시지 전송자에게 적합한 책임을 의미한다. 다시 말해서 메시지를 전송하는 클라이언트의 의도에 적합한 책임을 할당해야 한다.  
메시지가 클라이언트의 의도를 표현한다.  

처음부터 데이터에 집중하는 데이터 중심의 설계가 캡슐화에 취약한 반면 협력이라는 문맥 안에서 메시지에 집중하는 책임 중심의 설계는 캡슐화의 원리를 지키기가 훨씬 쉬워진다.  

객체에게 적절한 책임을 할당하기 위해서는 협력이라는 문맥을 고려해야 한다.  
협력이라는 문맥에서 적절한 책임이란 곧 클라이언트의 관점에서 적절한 책임을 의미한다.  
올바른 객체지향 설계는 클라이언트가 전송할 메시지를 결장한 후에야 비로소 객체의 상태를 저장하는 데 필요한 내부 데이터에 관해 고민하기 시작한다.  
<br/>

### 책임 주도 설계
책임 주도 설계의 흐름
- 시스템이 사용자에게 제공해야 하는 기능인 시스템 책임을 파악한다.  
- 시스템 책임을 더 작은 책임으로 분할한다.
- 분할된 책임을 수행할 수 있는 적절한 객체 또는 역할을 찾아 책임을 할당한다. 
- 객체가 책임을 수행하는 도중 다른 객체의 도움이 피룡한 경우 이를 책임질 적절한 객체 또는 역할을 찾는다.  
- 해당 객체 또는 역할에게 책임을 할당함으로써 두 객체가 협력하게 한다.  
<br/>

책임 주도 설계의 핵심은 책임을 결정한 후에 책임을 수행할 객체를 결정하는 것이다.  
그리고 협력에 참여하는 객체들의 책임이 어느정도 정리될 때 까지는 객체의 내부 상태에 대해 관심을 가지지 않는 것이다.  
<br/>
<br/>

## 책임 할당을 위한 GRASP 패턴
"General Responsibility Assignment Software Pattern(일반적인 책임 할당을 위한 소프트웨어 패턴)"의 약자로 객체에게 책임을 할당할 때 지침으로 삼을 수 있는 원칙들의 집합을 패턴 형식으로 정리한 것이다.  
 
### 도메인 개념에서 출발
설계를 시작하기 전에 도메인에 대한 개략적인 모습을 그려보는 것이 유용하다.  
도메인 안에는 무수히 많은 개념들이 존재하며 이 도메인 개념들을 책임 할당의 대상으로 사용하면 코드에 도메인의 모습을 투영하기가 좀 더 수월해진다.  

중요한 것은 설계를 시작하는 것이기 때문에 도메인 개념을 완벽하게 정리하는 것 보다는 빠르게 설계와 구현을 진행하라.  

이번 장에서는 할인 정책이 독립적인 개념이 아닌 영화의 종류로 표현되어 있지만, 두 모델 모두 올바른 구현으로 이끌어낼 수 있다면 두 모델 모두 정답이 된다.   
<br/>

### 정보 전문가에게 책임을 할당하라 
책임 주도 설계 방식의 첫 단계는 애플리케이션이 제공해야 하는 기능을 애플리케이션의 책임으로 생각하는 것이다.  
이 책임을 애플리케이션에 대해 전송된 메시지로 간수하고 이 메시지를 책임질 첫 번째 객체를 선택하는 것으로 설계를 시작한다.  

메시지를 전송할 객체의 의도를 반영해야 한다.
- 메시지를 전송할 객체는 무엇을 원하는가?
- 메시지를 수신할 적합한 객체는 누구인가?  
<br/>

메시지를 수신할 객체는 상태와 행동을 통합한 캡슐화의 단위라는 사실에 집중해야 하며, 객체는 자신의 상태를 스스로 처리하는 자율적인 존재여야 한다.  

객체에게 책임을 할당하는 첫 번째 원칙은 **책임을 수행할 정보를 알고 있는 객체에게 책임을 할당하는 것**이다. 이를 정보 전문가(Information Export) 패턴이라고 한다.  

Information Export 패턴은 객체가 자신이 소유하고 있는 정보와 관련된 작업을 수행한다는 일반적인 직관을 표현한 것이다.  
여기서 정보는 데이터가 아니며, 객체는 해당 정보를 제공할 수 있는 다른 객체를 알고 있거나 필요한 정보를 계산해서 제공할 수도 있다.  

Information Export 패턴은  객체란 상태와 행동을 함께 가지는 단위라는 객체지향의 가장 기본적인 원리를 책임 할당의 관점에서 표현한다.  
<br/>

### 높은 응집도와 낮은 결합도
설계는 트레이드오프 활동이다.  

높은 응집도와 낮은 결합도는 객체에 책임을 할당할 때 항상 고려해야 하는 기본 원리이다.  
책임을 할당할 수 있는 다양한 대안들이 존재한다면 응집도와 결합도의 측면에서 더 나은 대안을 선택하는 것이 좋다. 
GRASP에서는 이를 낮은 결합도(Low Coupling)패턴과 높은 응집도(High Cohesion) 패턴이라고 부른다.  
<br/>

**Low Coupling 패턴**
의존성을 낮추고 변화의 영향을 줄이며 재사용성을 줄이기 위해 설계의 전체적인 결합도가 낮게 유지되도록 책임을 할당하라.  
<br/> 

**High Cohesion 패턴**
복잡성을 관리할 수 있도록 높은 응집도를 유지할 수 있게 책임을 할당하라.  
<br/>

Low Coupling 패턴과 High Cohesion 패턴은 설계를 진행하면서 책임과 협력의 품질을 검토하는 데 사용할 수 있는 중요한 평가 기준이다.  
<br/>

### 창조자에게 객체 생성 책임을 할당하라
영화 예매 협력의 최종 결과물은 Reservation을 생성하는 것으로, 협력에 참여하는 어떤 객체는 Reservation 인스턴스를 생성할 책임을 할당해야 한다는 것을 의미한다.  
GRASP의 Creator(창조자) 패턴은 책임 할당 패턴으로서 객체를 생성할 책임을 어떤 객체에게 할당할지에 대한 지침을 제공한다.  

**Creator 패턴**
객체 A를 생성해야 할 떄 어떤 객체에게 객체 생성 책임을 할당해야 하는가? 아래 조건을 최대한 많이 만족하는 B에게 객체 생성 책임을 할당하라.
- B가 A 객체를 포함하거나 참조한다. 
- B가 A 객체를 기록한다.
- B가 A 객체를 긴밀하게 사용한다.
- B가 A 객체를 초기화하는 데 필요한 데이터를 가지고 있다.(B가 A에 대한 정보 전문가다)  

Creator 패턴의 의도는 어떤 방식으로든 생성되는 개체와 연결되거나 관련될 필요가 있는 개체에 해당 객체를 생성할 책임을 맡기는 것이다.  
생성될 객체에 대해 잘 알고 있어야 하거나 그 객체를 사용해야 하는 객체는 어떤 방식으로든 생성될 객체와 연결될 것이다. 즉, 두 객체는 서로 결합된다.  

이미 결합돼 있는 개체에게 생성 책임을 할당하는 것은 설계의 전체적인 결합도에 영향을 미치지 않는다.  
결과적으로 Creator 패턴은 이미 존재하는 개체 사이의 관걔를 이용하기 때문에 설계가 낮은 결합도를 유지할 수 있게 한다.  
<br/>
<br/>

## 구현을 통한 검증
낮은 응집도가 초래하는 문제를 해결하기 위해서는 변경의 이유에 따라 클래스르 분리해야 한다.  

일반적으로 설계를 개선하는 작업은 변경의 이유가 하나 이상인 클래스를 찾는 것으로부터 시작하는 것이 좋다.  


#### 코드를 통해 변경의 이유를 파악할 수 있는 방법
첫 번째 방법은 인스턴스 변수가 초기화 되는 시점을 살펴보는 것이다.  
응집도가 높은 클래스는 인스턴스를 생성할 때 모든 속성을 함께 초기화한다.  
반면 응집도가 낮은 클래스는 객체의 속성 중 일부만 초기화하고 일부는 초기화 되지 않은 상태로 남겨진다.  
따라서 함께 초기화되는 속성을 기준으로 코드를 분리해야 한다.  
<br/>

두 번째 방법은 메서드들이 인스턴스 변수를 사용하는 방식을 살펴보는 것이다.  
모든 메서드가 객체의 모든 속성을 사용하다면 클래스의 응집도는 높다고 볼 수 있다.  
반면 메서드들이 사용하는 속성에 따라 그룹이 나뉜다면 클래스의 응집도가 낮다고 볼수 있다.  
이 경우 클래스의 응집도를 높이기 위해서는 속성 그룹과 해당하는 그룹에 접근하는 메서드 그룹을 기준으로 코드를 분리해야 한다.  

#### 클래스의 응집도 판단하는 법
- 클래스가 하나 이상의 이유로 변경되야 한다면 응집도가 낮은 것이다, 변경의 이유를 기준으로 클래스르 분리하라.  
- 클래스의 인스턴스를 초기화하는 시접에 경우에 따라 서로 다른 속성들을 초기화하고 있다면 응집도가 낮은 것이다.  초기화되는 속성의 그룹을 기준으로 클래스를 분리하라.  
- 메서드 그룹이 속성 그룹을 사용하는지 여부로 나뉜다면 응집도가 낮은 것이다, 이들 그룹을 기준으로 클래스를 분리하라.  
<br/>

### 다형성을 통해 분리하기
역할을 사용하면 객체의 구체적인 타입을 추상화할 수 있다.  
역할을 대체할 클래스들 사이에서 구현을 공유해야 할 필요가 있다면 추상 클래스를 사용하면 된다.  
구현을 공유할 필요 없이 역할을 대체하는 객체들의 책임만 정의하고 싶다면 인터페이스를 사용하면 된다.  

객체의 타입에 따라 변하는 행동이 있다면 타입을 분리하고 변화하는 행동을 각 타입의 책임으로 할당하는 것을 GRASP 다형성(Polymorphism) 패턴이라 한다.  
<br/>

### 변경으로부터 보호하기
DiscountCondition 인터페이스를 사용해 새로운 DiscountCondition 타입을 추가해도 Movie는 영향을 받지 않는다.  
변경을 캡슐화해서 책임을 할당하는 것을 GRASP에서는 변경 보호(Protected Variation) 패턴이라고 한다.  
이 패턴은 책임 할당의 관점에서 캡슐화를 설명한 것이다.  
<br/>

### 변경의 유연성
개발자로서 변경에 대비할 수 있는 두 가지 방법
- 코드를 이해하고 수정하기 쉽도록 최대한 단순하게 설계하는 것
- 코드를 수정하지 않고도 변경을 수용할 수 있도록 코드를 더 유연하게 만드는 것  
<br/>

첫 번째 방법이 더 좋은 방법이지만 유사한 변경이 반복적으로 발생하고 있다면 복잡성이 상승하더라도 유연성을 추가하는 두 번째 방법이 더 좋다.  
<br/>

## 책임 주도 설계의 대안
이해하기 쉽고 수정하기 쉬운 소프트웨어로 개선하기 위해 겉으로 보이는 동작은 바꾸지 않은 채 내부 구조를 변경하는 것을 리팩터링(Refactoring)이라고 부른다.  

