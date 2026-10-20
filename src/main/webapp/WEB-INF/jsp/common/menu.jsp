<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script>
	$(document).ready(function(){
		
		// 메뉴 항목 클릭 시
		$(".goUrlMenu").on("click", function() {
			/* console.log("메뉴항목 클릭 : "+$(this).attr("id")); */
			goMenuSite($(this).attr("id"));
			
			//자동로딩 실행중인 페이지 벗어날 시 자동로딩 중지(21.11.19 민경)
			console.log("autoLoadingFunction: " + autoLoadingFunction);
			if(autoLoadingFunction) {
				autoLoading = false;
				clearInterval(autoLoadingFunction);
			}
			if(pollingForCall){
				clearInterval(pollingForCall);
			}
		});
		
		
		if (authCode === "999") {    
			$('[id="/user/lockedAccounts.do"]').show();
		}
		
	});
</script>
	<ul>
		<li>
			<a id="/receipt/receipt.do" class="goUrlMenu">제보접수</a>
		</li>
		<li>
			<a id="/producer/producer.do" class="goUrlMenu">PD지원</a>
		</li>
		<li>
			<a id="/caster/caster.do" class="goUrlMenu">캐스터지원</a>
		</li>
		<li><a id="/stats/standard.do" class="goUrlMenu">통계 관리</a>
			<ul class="submenu">
				
				<!-- <li id="/stats/receipt.do" class="goUrlMenu"><a>제보접수현황</a></li> -->
				<!--
				<li id="/stats/standard.do" class="goUrlMenu"><a></a></li>
				<li id="/stats/national.do" class="goUrlMenu"><a>통신원별 통계</a></li>
				<li id="/stats/local.do" class="goUrlMenu"><a>실적별 통계</a></li>
				<li id="/stats/mobile.do" class="goUrlMenu"><a>우선순위별 통계</a></li> -->
			</ul>
		</li>
		<li>
			<a id="/user/first.do" class="goUrlMenu">사용자관리</a>
			<ul class="submenu">
				<li id="/user/userMain.do" class="goUrlMenu"><a>사용자관리</a></li>
				<li id="/user/historyMain.do" class="goUrlMenu"><a>사용이력관리</a></li>
				<li id="/user/lockedAccounts.do" class="goUrlMenu" style="display: none;"><a>계정잠금관리</a></li>
			</ul>
		</li>
		<li><a id="/informer/first.do" class="goUrlMenu">통신원관리</a>
			<ul class="submenu">
				<li id="/informer/informerMain.do" class="goUrlMenu"><a>통신원관리</a></li>
				<li id="/informer/mileage/mileageMain.do" class="goUrlMenu mileageMenu" style="display: flex;justify-content: center;top: 0px;"><a>굿 제보 마일리지<br>(통신원)</a></li>
				<li id="/informer/excellenceIfrm/excellenceIfrmMain.do" class="goUrlMenu mileageMenu" style="display: flex;justify-content: center;top: 0px;"><a>우수 제보자<br>(시민,애청자,기관)</a></li>
				<li id="/informer/award/awardMain.do" class="goUrlMenu"><a>시상관리</a></li>
				<li id="/informer/event/eventMain.do" class="goUrlMenu"><a>행사관리</a></li>
				<li id="/informer/bestIfrm/bestIfrmMain.do" class="goUrlMenu mileageMenu"><a>최고 통신원</a></li>
				
			</ul>
		</li>
		<li><a id="/option/codeMng.do" class="goUrlMenu">정보관리</a>
			<ul class="submenu">
				<li id="/option/codeMng.do" class="goUrlMenu"><a>코드관리</a></li>
				<li id="/option/auth/authList.do" class="goUrlMenu"><a>등급관리</a></li>
			</ul>
		</li>
		<li>
			<a id="/notice/notice.do" class="goUrlMenu">공지사항</a>
		</li>
	</ul>
