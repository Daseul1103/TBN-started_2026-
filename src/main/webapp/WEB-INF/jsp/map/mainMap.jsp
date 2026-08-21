<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<script>
	$(document).ready(function() {
		console.log("카카오지도 jsp 호출");
		//22.10.15 
		try{
			infowindow = new kakao.maps.InfoWindow({zIndex:1});
			center = new kakao.maps.LatLng(37.52334726919335, 126.92522447784268);
			center = initXYByRegionID(lgnArea);
			
			var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			mapOption = {center: center, level: level};
			
			map = new kakao.maps.Map(mapContainer, mapOption);
			ps = new kakao.maps.services.Places();
			geocoder = new kakao.maps.services.Geocoder();
			
			searchAddrFromCoords(center, getLocationFromCenter);
			map.addOverlayMapTypeId(kakao.maps.MapTypeId.TRAFFIC);
			
			//toCrtLocationByGPS(); //초기위치지정(GPS기반)
			//cctvInBounds(); //초기 CCTV loading
			
			//클릭 Event
			kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
				var latlng = mouseEvent.latLng;
				//새로운 마커 생성
				marker = createMarker(latlng);
				marker.setMap(map);
				//좌표값 생성
				coordX = latlng.getLat();
				coordY = latlng.getLng();

				if(authCode == "5"){
			        return false;
			    } else {
					var coordXEl = document.getElementById('X_COORDINATE');
					var coordYEl = document.getElementById('Y_COORDINATE');
					coordXEl.value = coordX;
					coordYEl.value = coordY;
			    }
			});
			
			//중심좌표 변경,Zoom Event
			kakao.maps.event.addListener(map, 'idle', function() {
				center = map.getCenter();
				level = map.getLevel();
				//cctvInBounds();
				searchAddrFromCoords(center, getLocationFromCenter);
			});
			apiChk=2;
		}catch{
			console.log("카카오 api 연동 실패");
			apiChk=0;
			
			//22.10.15 수정
			//카카오 지도연동이 실패한 경우
			$("#X_COORDINATE").val("0.0");
			$("#Y_COORDINATE").val("0.0");	
			///////////////////////////////
			
			$("#map").append("<h1 style='font-size:36px;line-height:72px;position:relative;top:162px;text-align:center;'>외부 지도 서버의 장애로</h1>");
			$("#map").append("<h1 style='font-size:36px;line-height:72px;position:relative;top:150px;text-align:center;'>지도 표출이 되지 않습니다.</h1>");
			$("#map").append("<h1 style='font-size:18px;line-height:36px;color:red;position:relative;top:173px;text-align:center;'>[현재는 모든 제보유형에 지도좌표 입력히지 않아도</h1>");
			$("#map").append("<h1 style='font-size:18px;line-height:36px;color:red;position:relative;top:172px;text-align:center;'>제보접수 가능합니다.]</h1>");
		}
		
	});
</script>
</head>
<body style="width:600px; height:700px; overflow-x: hidden; overflow-y: hidden;">
		<!-- 빠른메뉴 -->
	<div class="mt_new_side" style="position:absolute;top:25px;">
		<!-- <img src="../images/cctv/cctv3.png" alt="cctv" onclick="cctvToggle()" style="cursor:pointer;width:60px;"/>
		<img src="../images/cctv/cctv_word.png" alt="cctv" onclick="cctvToggle()" style="width:73px;padding-top:8px;cursor:pointer;" /> -->
		<img src="../images/cctv/cctv3.png" alt="cctv" onclick="mapForCCTV()" style="cursor:pointer;width:60px;"/>
		<img src="../images/cctv/cctv_word.png" alt="cctv" onclick="mapForCCTV()" style="width:73px;padding-top:8px;cursor:pointer;" />
	</div>
	<div id="map" class="map" style="width:600px; height:634px; margin-top:119px; z-index:2"></div>
	<!-- 검색 -->
	<div id="location-search">
		<div class="content" style="padding:8px;margin-left:5px;">
			<span style="padding-right:10px;font-size:13px;"><b>현재위치</b></span>
			<span id="1depth" class="depth">서울특별시</span>
				<img src="../images/map/arrow.png" width="17px" height="15px" />
			<span id="2depth" class="depth">영등포구</span>
				<img src="../images/map/arrow.png" width="17px" height="15px" />
			<span id="3depth" class="depth">여의동</span>
		</div>
		<input id="text_search" type="text" value="" placeholder="장소, 주소 검색" onkeyup="if(event.keyCode == 13)searchPlaces()" autocomplete=off />
		<img id="btn_search" src="../images/map/btn_sch.png" onclick="searchPlaces()" />
	</div>

	
	
	
	
	
	
	