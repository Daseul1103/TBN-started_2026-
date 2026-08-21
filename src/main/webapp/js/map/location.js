/************************************************************************
함수명 : toCrtLocationByGPS
설 명 : 현재위치로 이동(GPS기반) - 현재위치로 이동
인 자 : 
************************************************************************/
function toCrtLocationByGPS(){
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var lat = position.coords.latitude, // 위도
	            lon = position.coords.longitude; // 경도
			latlng = new kakao.maps.LatLng(lat, lon);
			center = latlng;
	        map.setCenter(latlng);
	        searchAddrFromCoords(center, getLocationFromCenter);
	        //cctvInBounds();
	    });
	} 
}
/************************************************************************
함수명 : searchAddrFromCoords - 현재위치
설 명 : 중심좌표에 해당하는 주소(행정동)요청 
인 자 : 지도 중심 좌표, callback함수
************************************************************************/
function searchAddrFromCoords(coords, callback) {
    // 좌표로 행정동 주소 정보를 요청
    geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
}
/************************************************************************
함수명 : getLocationFromCenter - 현재위치
설 명 : 현재위치를 화면에 표시(HTML)
인 자 : 
************************************************************************/
function getLocationFromCenter(result, status) {
    if (status === kakao.maps.services.Status.OK) {
        for(var i = 0; i < result.length; i++) {
            // 행정동의 region_type 값은 'H' 이므로
            if (result[i].region_type === 'H') {
            	depth_1 = result[i].region_1depth_name;
            	depth_2 = result[i].region_2depth_name;
            	depth_3 = result[i].region_3depth_name;
            	
            	$("#location-search #1depth").html(depth_1);
            	$("#location-search #2depth").html(depth_2);
            	$("#location-search #3depth").html(depth_3);
            	
            	$("#1depth").html(depth_1);
            	$("#2depth").html(depth_2);
            	$("#3depth").html(depth_3);
                break;
            }
        }
    } else {
    	alert("Error: 잠시 후 다시 시도해 주세요.");
    }
}
/************************************************************************
함수명 : searchPlaces - 장소검색
설 명 : 장소검색
인 자 : 
************************************************************************/
/*function searchPlaces(artreyVal){
	//22.06.09 충북요청사항 : 도로 클릭시 맵이동
	console.log("클릭한 도로값 : "+artreyVal);
	if(typeof artreyVal !== "undefined"){
	    ps.keywordSearch(artreyVal, artReyMap); 
	}else{
		var text_search = document.getElementById('text_search').value;
	    if (!text_search.replace(/^\s+|\s+$/g, '')) {
	        alert('키워드를 입력해주세요!');
	        return false;
	    }
	    ps.keywordSearch(text_search, placesSearchCB); 
	}
	
}*/


//좌표 검색 마커
var coordinateMarker = null;

// 좌표 → 주소 변환 객체
var geocoder = new kakao.maps.services.Geocoder();


function searchPlaces(artreyVal) {

    // 22.06.09 충북요청사항 : 도로 클릭 시 맵 이동
    /*console.log("클릭한 도로값 : " + artreyVal);*/

    if (typeof artreyVal !== "undefined") {

        // 기존 도로명 검색
        ps.keywordSearch(artreyVal, artReyMap);

    } else {

        var text_search =
            document.getElementById('text_search').value;

        // 앞뒤 공백 제거
        text_search = text_search.replace(/^\s+|\s+$/g, '');

        if (!text_search) {
            alert('키워드 또는 좌표를 입력해주세요!');
            return false;
        }

        /*
         * 쉼표 또는 공백으로 분리
         *
         * 예시
         * 37.334565005581894,127.97595043666223
         * 37.334565005581894 127.97595043666223
         */
        var coordinateArray = text_search.split(/[,\s]+/);

        // 값이 2개인 경우 좌표인지 확인
        if (coordinateArray.length === 2) {

            var firstValue = Number(coordinateArray[0]);
            var secondValue = Number(coordinateArray[1]);

            // 두 값이 모두 숫자인 경우
            if (!isNaN(firstValue) && !isNaN(secondValue)) {

                var latitude;
                var longitude;

                /*
                 * 127.9759, 37.3345로 입력한 경우
                 * 경도(X), 위도(Y)
                 */
                if (Math.abs(firstValue) > 90) {
                    longitude = firstValue;
                    latitude = secondValue;

                /*
                 * 37.3345, 127.9759로 입력한 경우
                 * 위도(Y), 경도(X)
                 */
                } else {
                    latitude = firstValue;
                    longitude = secondValue;
                }

                // 좌표 범위 검사
                if (
                    latitude >= -90 &&
                    latitude <= 90 &&
                    longitude >= -180 &&
                    longitude <= 180
                ) {
                    searchByCoordinate(latitude, longitude);
                    return;
                }

                alert('올바른 좌표값을 입력해주세요.');
                return false;
            }
        }

        // 좌표가 아니면 기존 장소명·주소 검색
        ps.keywordSearch(text_search, placesSearchCB);
    }
}


/**
 * 좌표 검색 처리
 */
function searchByCoordinate(latitude, longitude) {

    var position =
        new kakao.maps.LatLng(latitude, longitude);

    // 검색한 좌표로 지도 이동
    map.setCenter(position);
    map.setLevel(3);

    // 이전 좌표 검색 마커 삭제
    if (coordinateMarker !== null) {
        coordinateMarker.setMap(null);
    }

    // 검색한 좌표에 마커 생성
    coordinateMarker = new kakao.maps.Marker({
        map: map,
        position: position
    });

    // 좌표를 주소로 변환
    // coord2Address에는 경도(X), 위도(Y) 순서로 전달
    geocoder.coord2Address(
        longitude,
        latitude,
        function (result, status) {

            if (status === kakao.maps.services.Status.OK) {

                var address = '';

                // 도로명 주소가 있는 경우
                if (result[0].road_address) {
                    address =
                        result[0].road_address.address_name;

                // 도로명 주소가 없으면 지번 주소 사용
                } else if (result[0].address) {
                    address =
                        result[0].address.address_name;
                }

            } else {
                console.log('좌표에 해당하는 주소가 없습니다.');
            }
        }
    );
}

/************************************************************************
함수명 : placesSearchCB - 장소검색
설 명 : 장소검색 완료 후 호출되는 콜백함수
인 자 : 
************************************************************************/
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) { //검색결과가 존재할때
    	searchFlag = true;
    	
    	if(resultMarkers.length > 0){
    		hideMarkers(resultMarkers);
    		resultMarkers = [];
    	}
    	
    	var totalBounds = new kakao.maps.LatLngBounds();
    	for(var i=0; i<data.length; i++){
    		createResultMarker(data[i], i, totalBounds);
        }
    	
    	//console.log("검색결과: 총 "+resultMarkers.length+"행");
    	map.setBounds(totalBounds);
    	level = map.getLevel();
    	showMarkers(resultMarkers);
    	searchFlag = false;
    	//cctvInBounds();
    	return;
    	
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');
        return;
        
    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요');
        return;
    }
}
/************************************************************************
함수명 : createResultMarker - 장소검색
설 명 : 장소검색결과 마커group생성
인 자 : result List, index
************************************************************************/
function createResultMarker(obj, i, totalBounds) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),
        imgOptions =  {
            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin : new kakao.maps.Point(0, (i*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
    var placePosition = new kakao.maps.LatLng(obj.y, obj.x);
	var resultMarker = new kakao.maps.Marker({
						position: placePosition,
						image: markerImage,
						clickable: true,
					});
	totalBounds.extend(placePosition);
	
	(function(resultMarker, title) {
		//marker click Event
		kakao.maps.event.addListener(resultMarker, 'click', function() {
			
			// 26-08-19 : 소방청 신규 계정 및 권한 생성에 따른 분기 처리
			if(authCode == "5"){
		        return false;
		    } else {
				var coordXEl = document.getElementById('X_COORDINATE');
				var coordYEl = document.getElementById('Y_COORDINATE');
				coordXEl.value = obj.y;
				coordYEl.value = obj.x;
				if(marker != undefined){
					marker.setMap(null);
				}
		    }
		});
		//marker mouseOver Event
		kakao.maps.event.addListener(resultMarker, 'mouseover', function() {
            displayInfowindow(resultMarker, title);
        });
		//marker mouseOut Event
        kakao.maps.event.addListener(resultMarker, 'mouseout', function() {
            infowindow.close();
        });
	})(resultMarker, obj.place_name);
	
	resultMarkers.push(resultMarker);
}
/************************************************************************
함수명 : displayInfowindow - 장소검색
설 명 : 장소검색결과 마커별 infowindow표출
인 자 : 장소검색 결과 객체리스트
************************************************************************/
function displayInfowindow(marker, title) {
    var content = '<div style="padding:5px;z-index:6;">' + title + '</div>';
    infowindow.setContent(content);
    infowindow.open(map, marker);
}
/************************************************************************
함수명 : initXYByRegionID
설 명 : GeoLocation 미사용시 지도 초기 좌표
인 자 : lgnArea(로그인지역)
************************************************************************/
function initXYByRegionID(lgnArea){
	var center;
	switch(lgnArea){
		case "032" : center = new kakao.maps.LatLng(37.4459182091311, 126.651492417538);	//경인
			break;
		case "033" : center = new kakao.maps.LatLng(37.3319610972938, 127.972671674323);	//강원
			break;
		case "042" : center = new kakao.maps.LatLng(36.335629529684766, 127.36658831989027);//대전
			break;
		case "063" : center = new kakao.maps.LatLng(35.84734981804826, 127.08120691106626);	//전북
			break;
		case "053" : center = new kakao.maps.LatLng(35.84331440178304, 128.58035161491011); //대구
			break;
		case "062" : center = new kakao.maps.LatLng(35.22167676181738, 126.8456261530146);	//광주
			break;
		case "051" : center = new kakao.maps.LatLng(35.13106682366455, 129.1016720821164);	//부산
			break;
		case "052" : center = new kakao.maps.LatLng(35.57301275022596, 129.31383188694105);	//울산
			break;
		case "100" : center = new kakao.maps.LatLng(36.6270874444608, 127.423949058959);	//충북
			break;
		case "055" : center = new kakao.maps.LatLng(35.237720188225886, 128.65856407610062);//경남
			break;
		case "054" : center = new kakao.maps.LatLng(36.08025609777878, 129.37680349978484);	//경북
			break;
		case "064" : center = new kakao.maps.LatLng(33.47840940896314, 126.55001390197313);	//제주
			break;
		case "041" : center = new kakao.maps.LatLng(36.6430715635016, 126.67423394621738);	//충남
			break; 
		default: center = new kakao.maps.LatLng(37.530863782983054, 126.971274718117);
			break;
	}
	return center;
}
/************************************************************************
함수명 : artReyMap
설 명 : 22.06.09 충북 요청사항 
인 자 : 
************************************************************************/
function artReyMap(data, status, pagination){
	
	if (status === kakao.maps.services.Status.OK) { //검색결과가 존재할때
    	searchFlag = true;
    	
    	if(resultMarkers.length > 0){
    		hideMarkers(resultMarkers);
    		resultMarkers = [];
    	}
    	
    	var totalBounds = new kakao.maps.LatLngBounds();
    	for(var i=0; i<data.length; i++){
    		createResultMarker(data[i], i, totalBounds);
        }
    	
    	//console.log("검색결과: 총 "+resultMarkers.length+"행");
    	map.setBounds(totalBounds);
    	level = map.getLevel();
    	showMarkers(resultMarkers);
    	searchFlag = false;
    	//cctvInBounds();
    	return;
    	
    }
	
}
/************************************************************************
함수명 : displayResults
설 명 : 
인 자 : 
************************************************************************/

/************************************************************************
함수명 : displayResults
설 명 : 
인 자 : 
************************************************************************/






















