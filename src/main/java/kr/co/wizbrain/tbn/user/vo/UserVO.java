package kr.co.wizbrain.tbn.user.vo;

public class UserVO {
	
	// 접속ID
	public String userId;
	// 접속비밀번호
	public String userPw;
	// 이름
	public String userName;
	// 소속방송국
	public String regionId;
	// 소속방송국 이름
	public String regionName;
	// 전화번호
	public String userPhone;
	// 권한구분
	public String authCode;
	// 권한명
	public String authName;
	// 상태
	public String flagUse;
	// 메모
	public String memo;
	// 내선번호
	public String inTel;
	// 세부지역
	public String regionSubId;
	// 가입일
	public String regDt;
	// 최종로그인
	public String stDt;
	// 최종로그아웃
	public String fnDt;
	//현재 접속여부
	public String cnYn;
	//검색타입
	public String searchType;
	//검색값
	public String searchValue;
	//사용 시간
	public String useTime;
	//검색 시작 시간
	public String sDate;
	//검색 종료 시간
	public String eDate;
	
	
	// 비밀번호 마지막 변경일자
	public String pwChangedt;
	// 비밀번호 변경한 실 사용자
	public String changeUserid;
	
	
	public String getChangeUserid() {
		return changeUserid;
	}
	public void setChangeUserid(String changeUserid) {
		this.changeUserid = changeUserid;
	}
	public String getPwChangedt() {
		return pwChangedt;
	}
	public void setPwChangedt(String pwChangedt) {
		this.pwChangedt = pwChangedt;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getAuthName() {
		return authName;
	}
	public void setAuthName(String authName) {
		this.authName = authName;
	}
	public String getFlagUse() {
		return flagUse;
	}
	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getInTel() {
		return inTel;
	}
	public void setInTel(String inTel) {
		this.inTel = inTel;
	}
	public String getRegionSubId() {
		return regionSubId;
	}
	public void setRegionSubId(String regionSubId) {
		this.regionSubId = regionSubId;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getStDt() {
		return stDt;
	}
	public void setStDt(String stDt) {
		this.stDt = stDt;
	}
	public String getFnDt() {
		return fnDt;
	}
	public void setFnDt(String fnDt) {
		this.fnDt = fnDt;
	}
	public String getCnYn() {
		return cnYn;
	}
	public void setCnYn(String cnYn) {
		this.cnYn = cnYn;
	}
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}
	public String getUseTime() {
		return useTime;
	}
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	@Override
	public String toString() {
		return "UserVO [userId=" + userId + ", userPw=" + userPw + ", userName=" + userName + ", regionId=" + regionId
				+ ", regionName=" + regionName + ", userPhone=" + userPhone + ", authCode=" + authCode + ", authName="
				+ authName + ", flagUse=" + flagUse + ", memo=" + memo + ", inTel=" + inTel + ", regionSubId="
				+ regionSubId + ", regDt=" + regDt + ", stDt=" + stDt + ", fnDt=" + fnDt + ", cnYn=" + cnYn
				+ ", searchType=" + searchType + ", searchValue=" + searchValue + ", useTime=" + useTime + ", sDate="
				+ sDate + ", eDate=" + eDate + "]";
	}
}
