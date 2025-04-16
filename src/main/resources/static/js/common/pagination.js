
// 현재 URL 에 page 번호를 이어주는 함수
function updateQueryParam (pageNo) {
    const url = new URL(window.location.href);  // 현재 URL 주소 객체 생성
    url.searchParams.set('page', pageNo);       // 현재 URL 에 ?page=pageNo 혹은 &page=pageNo 추가
    window.location.href = url.toString();      // 해당 URL 로 이동
}
