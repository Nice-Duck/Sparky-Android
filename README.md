
# [Sparky - 탭 세 번으로 끝나는 스크랩](https://apps.apple.com/us/app/id6444295657)
<img src="https://github.com/Minny27/Sparky-iOS/assets/68800789/61b3d89a-cde8-4477-a870-494a6a58668c" width=18%> <img src="https://github.com/Minny27/Sparky-iOS/assets/68800789/953dd799-5b0d-49c5-9357-5bcee7640d9a" width=18%> <img src="https://github.com/Minny27/Sparky-iOS/assets/68800789/84314559-2da0-40bd-8ca1-b42f3a29d6d0" width=18%> <img src="https://github.com/Minny27/Sparky-iOS/assets/68800789/5595ac0d-8915-41f5-bbe7-c81158a43b84" width=18%> <img src="https://github.com/Minny27/Sparky-iOS/assets/68800789/a580b13a-d124-4e80-9386-e56d0b6591eb" width=18%>


## 🧩 Overview
근무 시간, 이동중, 잠 들기 전 언제나. 데스크톱, 모바일 기기 어디서든. 지금 당장 보고 정리할 수는 없지만 일단 기록해 두고 싶은 **웹 페이지를 쉽고 빠르게 저장할 수 있는 서비스**가 있으면 좋겠다고 생각했습니다.

**웹 페이지에서 공유하기 기능으로 자체 제작한 템플릿에 썸네일, 제목, 소제목, 태그, 메모를 포함해서 저장할 수 있는 서비스**를 고안했습니다. 다양한 도메인의 웹 페이지를 단 3번의 클릭으로 스크랩할 수 있습니다.


## 🎲 Key Features
- 불특정 도메인의 웹 페이지 공유하기
- 공유 템플릿에 제목, 소제목, 태그, 메모 등을 포함해서 저장하기
- 태그를 통한 스크랩 분류 및 검색
- 타인의 스크랩 열람 및 재스크랩


## 🧑🏻‍💻 Team
- 기획자 1명
- 디자이너 1명
- 백엔드 개발자 1명
- iOS 개발자 1명
- AOS 개발자 1명

## 🔗 Link

(현재 서비스 종료)

[https://play.google.com/store/apps/details?id=com.softsquared.niceduck.android.sparky](https://play.google.com/store/apps/details?id=com.softsquared.niceduck.android.sparky) 

## 📖 상세 내용

### 로그인
![](https://github.com/GNUting/GNUting-Android/assets/86148926/3de22d3b-8254-4cf0-85fd-53b7e41e9549)
    

### 회원가입
![](https://github.com/GNUting/GNUting-Android/assets/86148926/c8a84836-5c57-4b6c-a812-57eb6ea076c9)

![](https://github.com/GNUting/GNUting-Android/assets/86148926/7624a34c-504a-41a5-8ab9-23448fdc8ac6)

![](https://github.com/GNUting/GNUting-Android/assets/86148926/f8543733-30ac-42c8-aa14-0a4b3f9710bd)


### 링크 스크랩 기능
링크 스크랩 기능은 사용자가 저장하고 싶은 웹페이지의 HTML 구조를 파싱((Jsoup 사용)하여 원하는 정보를 Sparky만의 저장 템플릿에 맞춰서 저장하고 표시하는 기능입니다.
    
스크랩 기능은 크게 두 가지입니다. 
    

### 저장하고 싶은 링크 uri를 입력하여 저장 
![](https://github.com/GNUting/GNUting-Android/assets/86148926/162737e5-b5fb-4bf2-9ff3-657ed0d99cf2)

### 다른 앱에서 앱 사용 도중에 저장하고 싶은 웹페이지를 우리 앱에 공유하여 저장 (Android Sharesheet)
![](https://github.com/GNUting/GNUting-Android/assets/86148926/bda05c91-7b63-411f-95a1-7faa6693fdd5)

### 스크랩 템플릿
![](https://github.com/GNUting/GNUting-Android/assets/86148926/565d0aab-b258-49f2-82a7-ff834424fa5e)

### 사용자들이 저장한 스크랩 리스트 조회
![](https://github.com/GNUting/GNUting-Android/assets/86148926/8ce2f2b3-e390-4495-9ab6-da2456bfe36a)

### Lottie 애니메이션 로딩
![](https://github.com/GNUting/GNUting-Android/assets/86148926/6f3603f7-a19a-4327-8e43-280008450787)

### shimmer library을 사용한 스켈레톤 UI
<img width="330" src="https://github.com/GNUting/GNUting-Android/assets/86148926/a141adbf-f7ee-487a-912a-6ceb3b3886bf">
    

## ℹ️ 시연 영상

[https://blog.naver.com/pck4949/222947888217](https://blog.naver.com/pck4949/222947888217)


## 🛠️ 사용 기술 및 라이브러리

- Kotlin
- Coroutines
- **MVVM 패턴**
- Retrofit2
- OkHttp3
- ViewModel, LiveData, Room, Navigation Component
- Glide
- **Jsoup (HTML 파서 라이브러리)**
- **Android Sharesheet**
- **JWT - Access Token & Refresh Token**
- **Lottie**
- **shimmer library**

