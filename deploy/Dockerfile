# 빌드 단계: Gradle로 스프링 부트 프로젝트를 빌드하기 위한 환경 설정
FROM gradle:8.2-jdk17 AS builder
# 작업 디렉토리 설정: 도커 컨테이너 내에서 소스 코드와 빌드 결과가 저장될 경로
WORKDIR /app
# 로컬 프로젝트 파일을 도커 컨테이너의 /app 디렉토리로 복사
COPY . .
# Gradle로 프로젝트 빌드: clean(이전 빌드 제거) --no-daemon 으로 Gradle 데몬 비활성화, -x 테스트 생략
RUN chmod +x gradlew && ./gradlew clean build -x test --no-daemon


# 실행 단계: 경량화된 실행 환경으로 스프링부트 앱 실행
FROM openjdk:17-jdk-slim

# 시간대 설정: KST (Asia/Seoul)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo "Asia/Seoul" > /etc/timezone

# 실행 환경에서도 작업 디렉토리 설정: 앱 실행 파일이 저장될 경로
WORKDIR /app
# 빌드 단계에서 생성된 JAR 파일을 실행환경으로 복사, 이름을 app.jar로 지정
COPY --from=builder /app/build/libs/*.jar app.jar
# 컨테이너가 외부와 통신할 포트 지정
EXPOSE 8080
# 컨테이너 시작 시 실행할 명령: Java 로 JAR 파일 실행, -Dspring.profiles.active=prod로 프로덕션 프로필 활성화
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "app.jar"]