// 봉사정보 입력 안하면 alert
function submitVolunteerForm(form) {
    const now = new Date();
    const startDate = new Date(form.startDate.value);
    const endDate = new Date(form.endDate.value);
    const deadLineDate = new Date(form.deadLineDate.value);

    form.title.value = form.title.value.trim();

    if ( form.title.value.length == 0 ) {
        alert("봉사명을 입력해주세요.");
        form.title.focus();
        return;
    }

    form.startDate.value = form.startDate.value.trim();

    if ( form.startDate.value === "" ) {
        alert("봉사 시작 날짜를 선택해주세요.");
        form.startDate.focus();
        return;
    }

    form.endDate.value = form.endDate.value.trim();

    if ( form.endDate.value === "" ) {
        alert("봉사 끝나는 날짜를 선택해주세요.");
        form.endDate.focus();
        return;
    }

    form.deadLineDate.value = form.deadLineDate.value.trim();

    if ( form.deadLineDate.value === "" ) {
        alert("신청 마감 날짜를 선택해주세요.");
        form.deadLineDate.focus();
        return;
    }

    form.location.value = form.location.value.trim();

    if ( form.location.value.length == 0 ) {
        alert("봉사 장소를 입력해주세요.");
        form.location.focus();
        return;
    }

    form.limit.value = form.limit.value.trim();

    if ( form.limit.value.length == 0 ) {
        alert("최대 신청인원을 입력해주세요.");
        form.limit.focus();
        return;
    }

    if (form.thumbnailImg.files.length === 0) {
        alert("대표 이미지를 선택해주세요.");
        form.thumbnailImg.focus();
        return;
    }

    form.content.value = form.content.value.trim();

    if ( form.content.value.length == 0 ) {
        alert("봉사 내용을 입력해주세요.");
        form.content.focus();
        return;
    }

    // 현재 시간 비교해서 날짜 계산
    if (startDate < now) {
        alert("봉사 시작 날짜가 현재를 기준으로 이미 지나간 날짜입니다. \n날짜를 다시 선택해주세요.");
        form.startDate.focus();
        return;
    }

    if (endDate < now) {
        alert("봉사 끝나는 날짜가 이미 지난 날짜입니다. \n날짜를 다시 선택해주세요.");
        form.endDate.focus();
        return;
    }

    if (startDate > endDate) {
        alert("봉사 시작 날짜가 봉사 끝나는 날짜보다 더 늦습니다. \n날짜를 다시 선택해주세요.");
        form.startDate.focus();
        return;
    }

    if (deadLineDate >= startDate) {
        alert("신청 마감 날짜는 봉사 시작 날짜보다 빨라야합니다. \n날짜를 다시 선택해주세요.");
        form.deadLineDate.focus();
        return;
    }

    form.submit();
}

// 이미지 미리보기
function previewImage(event) {
    var reader = new FileReader();
    reader.onload = function(){
        var output = document.getElementById('preview');
        output.src = reader.result;
        output.style.display = 'block'; // 이미지가 로드되면 표시
    }
    reader.readAsDataURL(event.target.files[0]);
}

// 카카오맵 시작
var map;
var selectedPlace = null;
var currentMarker = null; // 현재 마커를 추적하는 변수 추가

// kakao 지도 여는 모달창
function openKakaoMapModal() {
    document.getElementById('kakaoMapModal').style.display = 'block';
    initializeMap(); // 모달 열 때 지도 초기화
}

// 닫기 버튼 눌렀을 때 모달창 닫기
function closeKakaoMapModal() {
    document.getElementById('kakaoMapModal').style.display = 'none';
}

// 카카오맵 초기화
function initializeMap() {
    var mapContainer = document.getElementById('map'),
        mapOption = {
            center: new daum.maps.LatLng(36.3527, 127.3857), // 대전 시청 좌표
            level: 3
        };

    map = new daum.maps.Map(mapContainer, mapOption);
}

// 검색창에 입력한 키워드로 장소 검색
function searchPlaces() {
    var keyword = document.getElementById('keyword').value.trim();
    if (!keyword) {
        alert('검색어를 입력하세요.');
        return;
    }
    var places = new daum.maps.services.Places();
    places.keywordSearch(keyword, placesSearchCB);
}

// 장소 검색 결과를 처리
function placesSearchCB(data, status, pagination) {
    if (status === daum.maps.services.Status.OK) {
        var listEl = document.getElementById('placesList'),
            fragment = document.createDocumentFragment(),
            bounds = new daum.maps.LatLngBounds();

        removeAllChildNodes(listEl);

        for (var i = 0; i < data.length; i++) {
            var itemEl = getListItem(i, data[i]);
            fragment.appendChild(itemEl);
            bounds.extend(new daum.maps.LatLng(data[i].y, data[i].x));
        }

        listEl.appendChild(fragment);
        map.setBounds(bounds);
    } else if (status === daum.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
    } else if (status === daum.maps.services.Status.ERROR) {
        alert('검색 중 오류가 발생했습니다.');
    }
}

// 검색 결과 항목 li 태그로 생성
function getListItem(index, places) {
    var el = document.createElement('li');
    var itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
                  '<div class="info">' +
                  '   <h5>' + places.place_name + '</h5>';

    if (places.road_address_name) {
        itemStr += '    <span>' + places.road_address_name + '</span>'
    } else {
        itemStr += '    <span>' + places.address_name + '</span>';
    }

    el.innerHTML = itemStr;
    el.onclick = function() {
        selectedPlace = places;
        displayPlaceOnMap(places);
        document.getElementById('selectButton').style.display = 'block';
    };
    return el;
}

// 선택된 장소를 지도에 표시
function displayPlaceOnMap(place) {
    var coords = new daum.maps.LatLng(place.y, place.x);
    map.setCenter(coords);

    // 이전 마커가 있으면 제거
    if (currentMarker) {
        currentMarker.setMap(null);
    }

    // 새로운 마커 생성 및 표시
    currentMarker = new daum.maps.Marker({
        map: map,
        position: coords
    });
}

// 선택한 장소를 확인
function selectPlace() {
    if (selectedPlace) {
        var address = selectedPlace.road_address_name ? selectedPlace.road_address_name : selectedPlace.address_name;
        document.getElementById('location').value = address;
        closeKakaoMapModal();
    }
}

// 특정 요소의 모든 자식 노드 제거
function removeAllChildNodes(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}