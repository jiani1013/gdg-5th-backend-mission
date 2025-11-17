# Mission Course Week3
지난 주차에는 `Controller` 에서 `Service` 계층을 분리하는 연습을 해봤습니다.  
`Service` 계층은 비지니스 로직을 분리하기 위한 목적으로, 책임과 역할을 분명하게 하고자 했습니다.  

이번 주차에서는 2주차까지의 내용을 바탕으로, "영속성" 을 부여해보고자 합니다.  
즉, 서비스가 종료되어도 저희의 데이터가 데이터베이스에 남도록 만들어볼 예정입니다.

### 이번 주차 학습 목차
* `ORM` 이해하기
* `Persistence Context` 이해하기
* `Repository` 계층을 추가하여 2주차 과제 리팩토링하기

## ORM(Object Relational Mapping)
### 개요
데이터베이스와 Java 는 근본적으로 다른 존재입니다.  
저희가 주로 사용할 데이터베이스인 RDBMS(Relational Database Management System) 은  
관계(Relation)를 지향하며, Java 는 객체지향을 지향합니다.  

두 패러다임의 차이를 극복하고자하는 중간 역할이 **"ORM"** 입니다.  
이름대로, 객체를 관계에 맵핑해주는 역할을 합니다.

이 ORM 의 개념은 Java 에 한정되어있진 않습니다.  
Prisma, sequelize, TypeORM 등등 각 언어마다 다양하게 존재합니다.  

ORM 은 개발자가 하나의 패러다임으로 연속적으로 사고할 수 있도록 도움을 줍니다.  
하지만, 그 변환에 대한 반대급부로 성능 최적화에 문제를 일으키기도 합니다.  
범용적으로 모든 명령어를 처리해야하는 ORM 은 자칫 과도한 내부 SQL 문으로 성능이슈를 일으키곤 합니다.  

이런 경우에 대비해서 일부 데이터베이스 관련 로직은 QueryDSL, JPQL 로 작성되곤 합니다.

### 미션 내용
ORM 에 대한 내용을 찾아보고 정리해봅니다.  
[영문 위키피디아](https://en.wikipedia.org/wiki/Object%E2%80%93relational_mapping)를 추천드립니다.  

#### TODO
- [ ] ORM 에 대해 학습하고 정리하기

심화 내용으로는 다음 내용도 도전해보세요.
* [Object–relational impedance mismatch](https://en.wikipedia.org/wiki/Object%E2%80%93relational_impedance_mismatch)
* ORM vs JPA vs Hibernate vs Spring Data JPA

## 영속성 컨텍스트(Persistence Context)
### 개요
ORM 중에서 JPA 를 활용하시면, 언젠가는 마주치는 "영속성 컨텍스트" 입니다.  
JPA 는 Java 와 데이터베이스를 직접적으로 연결하지 않습니다.  
그 중간매체로서 영속성 컨텍스트를 사용하게 됩니다.  

이 내부구조를 이해해야하는 이유는,  
"저장을 했는데 저장이 되지 않는 경우" 가 발생할 수 있고, 이를 위해 영속성 컨텍스트를 따져야봐야 합니다.  
즉, JPA 를 이용해 데이터베이스를 조작하다보면, 의도치 않는 동작을 일으키고 이를 위해 영속성 컨텐스트를 직접 조작해야 합니다.  

### 미션 내용
영속성 컨텍스트에 대한 내용을 찾아보고 정리합니다.

* [Persistence context and EntityManager
  ](https://colevelup.tistory.com/21)
* [10분 테크톡](https://www.youtube.com/watch?v=c4rDrirE7Bc)
* [Hibernate Persistence Context](https://www.baeldung.com/jpa-hibernate-persistence-context)
* [Persistence Context Deep Dive](https://msolo021015.medium.com/jpa-persistence-context-deep-dive-2f36f9bd6214)

다음의 키워드에 대해서도 공부해봅시다.
* Entity Manager
* persist
* flush
* dirty checking
* 비영속 / 영속 / 반영속

#### TODO
- [ ] Persistent Context 에 대해 키워드를 바탕을 학습하고 정리하기

## `Repository` 계층
### 개요
위의 내용을 바탕으로 본격적으로 데이터베이스를 서비스에 추가하고자 합니다.  
기존에 Controller - Service 에 이어서, Controller - Service - Repository 를 통해,  
3-계층 아키텍쳐를 완성시킵니다.  

Repository 는 데이터 처리(데이터베이스 연결, 쿼리 작성을 통한 데이터 질의) 를 처리함으로서,  
네트워크 통신 - 비지니스 로직 - 데이터베이스 라는 역할을 분리하게 됩니다.  

이 데이터베이스를 추가함으로서, 저희의 데이터는 데이터베이스에 저장되어 서비스 서버가 잠시 종료되어도,  
데이터베이스에 저장되어있는 영속성을 구현하게 됩니다.  

### 미션 내용
Repository 계층을 추가하기 위해서 검색한다면 다음의 3가지 어노테이션이 나올겁니다.
* `@Repository`
* `@Transactional`
* `@Entity`

각각의 어노테이션이 어떤 의미인지, 어떤 기능을 하는지 정리해봅시다.

#### 심화내용
`@Transactional` 을 항상 꼭 써야할까요?  
다음 글을 통해 자신의 의견, 혹은 기준을 정리해보세요.
* https://swmobenz.tistory.com/34
* https://www.youtube.com/watch?v=XJo5O6GrFC0
* https://tech.kakaopay.com/post/jpa-transactional-bri/#transactional%EC%9D%B4%EB%9E%80

#### TODO
- [ ] `@Repository`, `@Transactional`, `@Entity` 키워드를 바탕으로 Repository 계층에 대해 학습하고 정리하기
- [ ] 2주차 과제에 Repository 계층을 추가함으로서 영속성을 부여하기

## TODO 정리
- [ ] **ORM** 에 대해 학습하기
- [ ] **Persistent Context** 에 대해 학습하기
- [ ] **Repository 계층**에 대해 학습하기
- [ ] 2주차 과제에 영속성 부여하기