package kr.co.wizbrain.tbn.infrm.vo;

import kr.co.wizbrain.tbn.comm.BaseVO;

public class InfrmVO extends BaseVO{

	//PK용ID 
	 public String informerId;
	//통신원코드 
	 public String actId;
	//통신원이름 
	 public String informerName;
	//소속기관코드 
	 public String orgId;
	 //소속기관코드 
	 public String orgName;

	//휴대폰번호 
	 public String phoneCell;
	//SUB기관코드 
	 public String orgSubId;
	//SUB기관명 
	 public String orgSubName;
	//집번호 
	 public String phoneHome;
	//사무실번호(통신원관리) 
	 public String phoneOffice;
	//통신원타입(시민/통신원) 
	 public String informerType;
	 //통신원타입(시민/통신원) 
	 public String informerTypeName;
	//메모1(통신원관리) 
	 public String memo1;
	//전달사항 
	 public String memo;
	//우편번호 
	 public String zipcode;
	//주소(제보접수) 
	 public String addressHome;
	 //주소 (통신원)
	 public String address;
	//우편번호2(통신원관리) 
	 public String zipcode2;
	//사무실주소 
	 public String addressOffice;
	//생일 
	 public String birthday;
	//차량번호 
	 public String carNum;
	//비고란 (통신원관리) 
	 public String comments;
	//통신원등록날짜 
	 public String regDate;
	//통신원삭제일 
	 public String delDate;
	//통신원수정일 
	 public String updDate;
	//사진파일명 
	 public String photoFile;
	//한문이름 
	 public String individualNameChina;
	//차량종류  
	 public String carType;
	//TRS번호  
	 public String trsNo;
	//최종학력 
	 public String lastSchool;
	//소속지역코드 
	 public String areaCode;
	// 지역명
	public String areaName;
	//명예통신원 여부 
	 public String honor;
	//사진제공 동의여부 
	 public String agree;
	//불량제보자여부 
	 public String flagBlacklist;
	//추가메모(통신원관리) 
	 public String memo2;
	//자원봉사여부 
	 public String flagService;
	// 최고통신원 플래그
	 public String flagBest;
	//위촉일 
	 public String startDate;
	//해촉일 
	 public String stopDate;
	//생일구분(양/음력) 
	 public String birthdayDiv;
	//통신원직업 
	 public String informerJob;
	//방송통신원 FLAG 
	 public String flagBroad;
	//위/해촉여부(Y:위촉/N:해촉) 
	public String flagAct;
	
	//업데이트 코드
	public String updateCode;
	//업데이트 텍스트
	public String updateText;
	//히스토리 업데이트일
	public String updateDate;
	
	//히스토리 코드
	public String histCode;
	//로컬파일위치
	public String localFilePath;
	//pagediv
	public String pageDiv;
	
	// 통신원 상세용
	private String MONTH;
	private String ROW_COUNT;
	//통신원관리 화면에서 다운시 분기 플래그
	private String downFlag;
	
	public String getDownFlag() {
		return downFlag;
	}
	public void setDownFlag(String downFlag) {
		this.downFlag = downFlag;
	}
	public String getInformerId() {
		return informerId;
	}
	public void setInformerId(String informerId) {
		this.informerId = informerId;
	}
	public String getActId() {
		return actId;
	}
	public void setActId(String actId) {
		this.actId = actId;
	}
	public String getInformerName() {
		return informerName;
	}
	public void setInformerName(String informerName) {
		this.informerName = informerName;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getFlagBest() {
		return flagBest;
	}
	public void setFlagBest(String flagBest) {
		this.flagBest = flagBest;
	}
	public String getPhoneCell() {
		return phoneCell;
	}
	public void setPhoneCell(String phoneCell) {
		this.phoneCell = phoneCell;
	}
	public String getOrgSubId() {
		return orgSubId;
	}
	public void setOrgSubId(String orgSubId) {
		this.orgSubId = orgSubId;
	}
	public String getOrgSubName() {
		return orgSubName;
	}
	public void setOrgSubName(String orgSubName) {
		this.orgSubName = orgSubName;
	}
	public String getPhoneHome() {
		return phoneHome;
	}
	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}
	public String getPhoneOffice() {
		return phoneOffice;
	}
	public void setPhoneOffice(String phoneOffice) {
		this.phoneOffice = phoneOffice;
	}
	public String getInformerType() {
		return informerType;
	}
	public void setInformerType(String informerType) {
		this.informerType = informerType;
	}
	public String getInformerTypeName() {
		return informerTypeName;
	}
	public void setInformerTypeName(String informerTypeName) {
		this.informerTypeName = informerTypeName;
	}
	public String getMemo1() {
		return memo1;
	}
	public void setMemo1(String memo1) {
		this.memo1 = memo1;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddressHome() {
		return addressHome;
	}
	public void setAddressHome(String addressHome) {
		this.addressHome = addressHome;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipcode2() {
		return zipcode2;
	}
	public void setZipcode2(String zipcode2) {
		this.zipcode2 = zipcode2;
	}
	public String getAddressOffice() {
		return addressOffice;
	}
	public void setAddressOffice(String addressOffice) {
		this.addressOffice = addressOffice;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	public String getDelDate() {
		return delDate;
	}
	public void setDelDate(String delDate) {
		this.delDate = delDate;
	}
	public String getUpdDate() {
		return updDate;
	}
	public void setUpdDate(String updDate) {
		this.updDate = updDate;
	}
	public String getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(String photoFile) {
		this.photoFile = photoFile;
	}
	public String getIndividualNameChina() {
		return individualNameChina;
	}
	public void setIndividualNameChina(String individualNameChina) {
		this.individualNameChina = individualNameChina;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public String getTrsNo() {
		return trsNo;
	}
	public void setTrsNo(String trsNo) {
		this.trsNo = trsNo;
	}
	public String getLastSchool() {
		return lastSchool;
	}
	public void setLastSchool(String lastSchool) {
		this.lastSchool = lastSchool;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getHonor() {
		return honor;
	}
	public void setHonor(String honor) {
		this.honor = honor;
	}
	public String getAgree() {
		return agree;
	}
	public void setAgree(String agree) {
		this.agree = agree;
	}
	public String getFlagBlacklist() {
		return flagBlacklist;
	}
	public void setFlagBlacklist(String flagBlacklist) {
		this.flagBlacklist = flagBlacklist;
	}
	public String getMemo2() {
		return memo2;
	}
	public void setMemo2(String memo2) {
		this.memo2 = memo2;
	}
	public String getFlagService() {
		return flagService;
	}
	public void setFlagService(String flagService) {
		this.flagService = flagService;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getBirthdayDiv() {
		return birthdayDiv;
	}
	public void setBirthdayDiv(String birthdayDiv) {
		this.birthdayDiv = birthdayDiv;
	}
	public String getInformerJob() {
		return informerJob;
	}
	public void setInformerJob(String informerJob) {
		this.informerJob = informerJob;
	}
	public String getFlagBroad() {
		return flagBroad;
	}
	public void setFlagBroad(String flagBroad) {
		this.flagBroad = flagBroad;
	}
	public String getFlagAct() {
		return flagAct;
	}
	public void setFlagAct(String flagAct) {
		this.flagAct = flagAct;
	}
	public String getUpdateCode() {
		return updateCode;
	}
	public void setUpdateCode(String updateCode) {
		this.updateCode = updateCode;
	}
	public String getUpdateText() {
		return updateText;
	}
	public void setUpdateText(String updateText) {
		this.updateText = updateText;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getHistCode() {
		return histCode;
	}
	public void setHistCode(String histCode) {
		this.histCode = histCode;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	public String getPageDiv() {
		return pageDiv;
	}
	public void setPageDiv(String pageDiv) {
		this.pageDiv = pageDiv;
	}
	public String getMONTH() {
		return MONTH;
	}
	public void setMONTH(String mONTH) {
		MONTH = mONTH;
	}
	public String getROW_COUNT() {
		return ROW_COUNT;
	}
	public void setROW_COUNT(String rOW_COUNT) {
		ROW_COUNT = rOW_COUNT;
	}
	
	
}
