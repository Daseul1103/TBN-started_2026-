package kr.co.wizbrain.tbn.receipt.vo;

public class ReceiptVO {
	private String RECEIPT_ID;
	private String RECEIPT_DAY; //INSERT사용안함
	private String RECEIPT_TIME; //INSERT사용안함
	private String INDIVIDUAL_ID;
	private String INDIVIDUAL_NAME;
	private String INDIVIDUAL_TYPE;
	private String ORG_ID;
	
	private String ORG_NAME;
	private String RECEPTION_ID;
	private String RECEPTION_NAME;
	private String MEMO;				//제보내용
	private String ARTERY_ID;
	
	private String F_LINK_ID;
	private String T_LINK_ID;
	private String F_NODE_NAME;			//FROMNODE명
	private String T_NODE_NAME;			//TONODE명
	private String REGION_ID;			//AREA_CODE
	
	private String START_DAY;
	//private String START_TIME;//INSERT사용안함
	private String STARTTIMEHH;
	private String STARTTIMEMI;
	private String END_DAY;
	//private String END_TIME;//INSERT사용안함
	private String ENDTIMEHH;
	
	private String ENDTIMEMI;
	private String REPORT_TYPE;
	private String REPORTER_TYPE;		//REPORTMEAN_TYPE
	private String FLAG_SEND;			//방송요청
	private String FLAG_IMPORTANT;		//긴급접수
	private String FLAG_DISASTOR;		//재난제보
	
	private String FLAG_BROD; // INSERT사용안함
	private String R_TEL;
	private String X_COORDINATE;		//X좌표
	private String Y_COORDINATE;		//Y좌표
	private String REPORT_TYPE2;
	private String REPORT_TYPE3;
	
	private String AREA_ID;
	private String LANE;
	
	//통신원정보
	private String INFORMER_ID;
	private String ACT_ID;
	private String INFORMER_NAME;
	private String REGIONSUB_ID;
	private String REGIONSUB_NAME;
	private String PHONE_CELL;
	private String PHONE_HOME;
	private String INFORMER_TYPE;
	private String ADDRESS_HOME;
	private String ZIPCODE;
	private String FLAG_BLACKLIST;
	
	private String COMMENT_TXT;
	private String AREA_CODE;
	
	private int STAT1;
	private int STAT2;
	private int STAT3;
	//문자돌발 db에 저보데이터 전송여부
	private String FLAG_WEB;
	//사진 영상제보
	private String FLAG_DMB_SEND;
	//현재는 미사용이나 향후 용도 고려필요
	private String FLAG_KNEX;
	//문자제보 여부
	private String DLS_TEXT;
	
	public ReceiptVO() {}
	
	

	public ReceiptVO(String rECEIPT_ID, String iNDIVIDUAL_ID, String iNDIVIDUAL_NAME, String iNDIVIDUAL_TYPE,
			String oRG_ID, String oRG_NAME, String rECEPTION_ID, String rECEPTION_NAME, String mEMO, String aRTERY_ID,
			String f_LINK_ID, String t_LINK_ID, String f_NODE_NAME, String t_NODE_NAME, String rEGION_ID,
			String sTART_DAY, String sTARTTIMEHH, String sTARTTIMEMI, String eND_DAY, String eNDTIMEHH,
			String eNDTIMEMI, String rEPORT_TYPE, String rEPORTER_TYPE, String fLAG_SEND, String fLAG_IMPORTANT,
			String fLAG_DISASTOR, String r_TEL, String x_COORDINATE, String y_COORDINATE, String rEPORT_TYPE2,
			String rEPORT_TYPE3, String aREA_ID, String lANE, String iNFORMER_ID, String aCT_ID, String iNFORMER_NAME,
			String rEGIONSUB_ID, String rEGIONSUB_NAME, String pHONE_CELL, String pHONE_HOME, String iNFORMER_TYPE,
			String aDDRESS_HOME, String zIPCODE, String fLAG_BLACKLIST, int sTAT1, int sTAT2, int sTAT3 , String fLAG_WEB,
			String fLAG_DMB_SEND, String fLAG_KNEX) {
		super();
		RECEIPT_ID = rECEIPT_ID;
		INDIVIDUAL_ID = iNDIVIDUAL_ID;
		INDIVIDUAL_NAME = iNDIVIDUAL_NAME;
		INDIVIDUAL_TYPE = iNDIVIDUAL_TYPE;
		ORG_ID = oRG_ID;
		ORG_NAME = oRG_NAME;
		RECEPTION_ID = rECEPTION_ID;
		RECEPTION_NAME = rECEPTION_NAME;
		MEMO = mEMO;
		ARTERY_ID = aRTERY_ID;
		F_LINK_ID = f_LINK_ID;
		T_LINK_ID = t_LINK_ID;
		F_NODE_NAME = f_NODE_NAME;
		T_NODE_NAME = t_NODE_NAME;
		REGION_ID = rEGION_ID;
		START_DAY = sTART_DAY;
		STARTTIMEHH = sTARTTIMEHH;
		STARTTIMEMI = sTARTTIMEMI;
		END_DAY = eND_DAY;
		ENDTIMEHH = eNDTIMEHH;
		ENDTIMEMI = eNDTIMEMI;
		REPORT_TYPE = rEPORT_TYPE;
		REPORTER_TYPE = rEPORTER_TYPE;
		FLAG_SEND = fLAG_SEND;
		FLAG_IMPORTANT = fLAG_IMPORTANT;
		FLAG_DISASTOR = fLAG_DISASTOR;
		R_TEL = r_TEL;
		X_COORDINATE = x_COORDINATE;
		Y_COORDINATE = y_COORDINATE;
		REPORT_TYPE2 = rEPORT_TYPE2;
		REPORT_TYPE3 = rEPORT_TYPE3;
		AREA_ID = aREA_ID;
		LANE = lANE;
		INFORMER_ID = iNFORMER_ID;
		ACT_ID = aCT_ID;
		INFORMER_NAME = iNFORMER_NAME;
		REGIONSUB_ID = rEGIONSUB_ID;
		REGIONSUB_NAME = rEGIONSUB_NAME;
		PHONE_CELL = pHONE_CELL;
		PHONE_HOME = pHONE_HOME;
		INFORMER_TYPE = iNFORMER_TYPE;
		ADDRESS_HOME = aDDRESS_HOME;
		ZIPCODE = zIPCODE;
		FLAG_BLACKLIST = fLAG_BLACKLIST;
		STAT1 = sTAT1;
		STAT2 = sTAT2;
		STAT3 = sTAT3;
		FLAG_WEB = fLAG_WEB;
		FLAG_DMB_SEND = fLAG_DMB_SEND;
		FLAG_KNEX = fLAG_KNEX;
	}


	//통신원정보 생성자
	public ReceiptVO(String iNFORMER_ID, String aCT_ID, String iNFORMER_NAME, String rEGIONSUB_ID,
			String rEGIONSUB_NAME, String pHONE_CELL, String pHONE_HOME, String iNFORMER_TYPE, String aDDRESS_HOME,
			String zIPCODE, String fLAG_BLACKLIST) {
		INFORMER_ID = iNFORMER_ID;
		ACT_ID = aCT_ID;
		INFORMER_NAME = iNFORMER_NAME;
		REGIONSUB_ID = rEGIONSUB_ID;
		REGIONSUB_NAME = rEGIONSUB_NAME;
		PHONE_CELL = pHONE_CELL;
		PHONE_HOME = pHONE_HOME;
		INFORMER_TYPE = iNFORMER_TYPE;
		ADDRESS_HOME = aDDRESS_HOME;
		ZIPCODE = zIPCODE;
		FLAG_BLACKLIST = fLAG_BLACKLIST;
	}
	
	
	
	
	
	public String getRECEIPT_DAY() {
		return RECEIPT_DAY;
	}



	public void setRECEIPT_DAY(String rECEIPT_DAY) {
		RECEIPT_DAY = rECEIPT_DAY;
	}



	public String getRECEIPT_TIME() {
		return RECEIPT_TIME;
	}



	public void setRECEIPT_TIME(String rECEIPT_TIME) {
		RECEIPT_TIME = rECEIPT_TIME;
	}



	public String getFLAG_BROD() {
		return FLAG_BROD;
	}



	public void setFLAG_BROD(String fLAG_BROD) {
		FLAG_BROD = fLAG_BROD;
	}



	public String getAREA_CODE() {
		return AREA_CODE;
	}



	public void setAREA_CODE(String aREA_CODE) {
		AREA_CODE = aREA_CODE;
	}



	public String getCOMMENT_TXT() {
		return COMMENT_TXT;
	}



	public void setCOMMENT_TXT(String cOMMENT_TXT) {
		COMMENT_TXT = cOMMENT_TXT;
	}



	public String getDLS_TEXT() {
		return DLS_TEXT;
	}

	public void setDLS_TEXT(String dLS_TEXT) {
		DLS_TEXT = dLS_TEXT;
	}

	public String getFLAG_KNEX() {
		return FLAG_KNEX;
	}

	public void setFLAG_KNEX(String fLAG_KNEX) {
		FLAG_KNEX = fLAG_KNEX;
	}

	public String getFLAG_DISASTOR() {
		return FLAG_DISASTOR;
	}

	public void setFLAG_DISASTOR(String fLAG_DISASTOR) {
		FLAG_DISASTOR = fLAG_DISASTOR;
	}

	public String getRECEIPT_ID() {
		return RECEIPT_ID;
	}
	public void setRECEIPT_ID(String rECEIPT_ID) {
		RECEIPT_ID = rECEIPT_ID;
	}
	public String getINDIVIDUAL_ID() {
		return INDIVIDUAL_ID;
	}
	public void setINDIVIDUAL_ID(String iNDIVIDUAL_ID) {
		INDIVIDUAL_ID = iNDIVIDUAL_ID;
	}
	public String getINDIVIDUAL_NAME() {
		return INDIVIDUAL_NAME;
	}
	public void setINDIVIDUAL_NAME(String iNDIVIDUAL_NAME) {
		INDIVIDUAL_NAME = iNDIVIDUAL_NAME;
	}
	public String getINDIVIDUAL_TYPE() {
		return INDIVIDUAL_TYPE;
	}
	public void setINDIVIDUAL_TYPE(String iNDIVIDUAL_TYPE) {
		INDIVIDUAL_TYPE = iNDIVIDUAL_TYPE;
	}
	public String getORG_ID() {
		return ORG_ID;
	}
	public void setORG_ID(String oRG_ID) {
		ORG_ID = oRG_ID;
	}
	public String getORG_NAME() {
		return ORG_NAME;
	}
	public void setORG_NAME(String oRG_NAME) {
		ORG_NAME = oRG_NAME;
	}
	public String getRECEPTION_ID() {
		return RECEPTION_ID;
	}
	public void setRECEPTION_ID(String rECEPTION_ID) {
		RECEPTION_ID = rECEPTION_ID;
	}
	public String getRECEPTION_NAME() {
		return RECEPTION_NAME;
	}
	public void setRECEPTION_NAME(String rECEPTION_NAME) {
		RECEPTION_NAME = rECEPTION_NAME;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	public String getF_NODE_NAME() {
		return F_NODE_NAME;
	}
	public void setF_NODE_NAME(String f_NODE_NAME) {
		F_NODE_NAME = f_NODE_NAME;
	}
	public String getT_NODE_NAME() {
		return T_NODE_NAME;
	}
	public void setT_NODE_NAME(String t_NODE_NAME) {
		T_NODE_NAME = t_NODE_NAME;
	}
	public String getREGION_ID() {
		return REGION_ID;
	}
	public void setREGION_ID(String rEGION_ID) {
		REGION_ID = rEGION_ID;
	}
	public String getARTERY_ID() {
		return ARTERY_ID;
	}
	public void setARTERY_ID(String aRTERY_ID) {
		ARTERY_ID = aRTERY_ID;
	}
	public String getF_LINK_ID() {
		return F_LINK_ID;
	}
	public void setF_LINK_ID(String f_LINK_ID) {
		F_LINK_ID = f_LINK_ID;
	}
	public String getT_LINK_ID() {
		return T_LINK_ID;
	}
	public void setT_LINK_ID(String t_LINK_ID) {
		T_LINK_ID = t_LINK_ID;
	}
	public String getR_TEL() {
		return R_TEL;
	}
	public void setR_TEL(String r_TEL) {
		R_TEL = r_TEL;
	}
	public String getREPORT_TYPE2() {
		return REPORT_TYPE2;
	}
	public void setREPORT_TYPE2(String rEPORT_TYPE2) {
		REPORT_TYPE2 = rEPORT_TYPE2;
	}
	public String getREPORT_TYPE3() {
		return REPORT_TYPE3;
	}
	public void setREPORT_TYPE3(String rEPORT_TYPE3) {
		REPORT_TYPE3 = rEPORT_TYPE3;
	}
	public String getAREA_ID() {
		return AREA_ID;
	}
	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}
	public String getSTART_DAY() {
		return START_DAY;
	}
	public void setSTART_DAY(String sTART_DAY) {
		START_DAY = sTART_DAY;
	}
	public String getSTARTTIMEHH() {
		return STARTTIMEHH;
	}
	public void setSTARTTIMEHH(String sTARTTIMEHH) {
		STARTTIMEHH = sTARTTIMEHH;
	}
	public String getSTARTTIMEMI() {
		return STARTTIMEMI;
	}
	public void setSTARTTIMEMI(String sTARTTIMEMI) {
		STARTTIMEMI = sTARTTIMEMI;
	}
	public String getENDTIMEHH() {
		return ENDTIMEHH;
	}
	public void setENDTIMEHH(String eNDTIMEHH) {
		ENDTIMEHH = eNDTIMEHH;
	}
	public String getENDTIMEMI() {
		return ENDTIMEMI;
	}
	public void setENDTIMEMI(String eNDTIMEMI) {
		ENDTIMEMI = eNDTIMEMI;
	}
	public String getEND_DAY() {
		return END_DAY;
	}
	public void setEND_DAY(String eND_DAY) {
		END_DAY = eND_DAY;
	}
	public String getREPORT_TYPE() {
		return REPORT_TYPE;
	}
	public void setREPORT_TYPE(String rEPORT_TYPE) {
		REPORT_TYPE = rEPORT_TYPE;
	}
	public String getREPORTER_TYPE() {
		return REPORTER_TYPE;
	}
	public void setREPORTER_TYPE(String rEPORTER_TYPE) {
		REPORTER_TYPE = rEPORTER_TYPE;
	}
	public String getFLAG_SEND() {
		return FLAG_SEND;
	}
	public void setFLAG_SEND(String fLAG_SEND) {
		FLAG_SEND = fLAG_SEND;
	}
	public String getFLAG_IMPORTANT() {
		return FLAG_IMPORTANT;
	}
	public void setFLAG_IMPORTANT(String fLAG_IMPORTANT) {
		FLAG_IMPORTANT = fLAG_IMPORTANT;
	}
	public String getX_COORDINATE() {
		return X_COORDINATE;
	}
	public void setX_COORDINATE(String x_COORDINATE) {
		X_COORDINATE = x_COORDINATE;
	}
	public String getY_COORDINATE() {
		return Y_COORDINATE;
	}
	public void setY_COORDINATE(String y_COORDINATE) {
		Y_COORDINATE = y_COORDINATE;
	}
	
	//통신원정보
	public String getINFORMER_ID() {
		return INFORMER_ID;
	}

	public void setINFORMER_ID(String iNFORMER_ID) {
		INFORMER_ID = iNFORMER_ID;
	}

	public String getACT_ID() {
		return ACT_ID;
	}

	public void setACT_ID(String aCT_ID) {
		ACT_ID = aCT_ID;
	}

	public String getINFORMER_NAME() {
		return INFORMER_NAME;
	}

	public void setINFORMER_NAME(String iNFORMER_NAME) {
		INFORMER_NAME = iNFORMER_NAME;
	}

	public String getREGIONSUB_ID() {
		return REGIONSUB_ID;
	}

	public void setREGIONSUB_ID(String rEGIONSUB_ID) {
		REGIONSUB_ID = rEGIONSUB_ID;
	}

	public String getREGIONSUB_NAME() {
		return REGIONSUB_NAME;
	}

	public void setREGIONSUB_NAME(String rEGIONSUB_NAME) {
		REGIONSUB_NAME = rEGIONSUB_NAME;
	}

	public String getPHONE_CELL() {
		return PHONE_CELL;
	}

	public void setPHONE_CELL(String pHONE_CELL) {
		PHONE_CELL = pHONE_CELL;
	}

	public String getPHONE_HOME() {
		return PHONE_HOME;
	}

	public void setPHONE_HOME(String pHONE_HOME) {
		PHONE_HOME = pHONE_HOME;
	}

	public String getINFORMER_TYPE() {
		return INFORMER_TYPE;
	}

	public void setINFORMER_TYPE(String iNFORMER_TYPE) {
		INFORMER_TYPE = iNFORMER_TYPE;
	}

	public String getADDRESS_HOME() {
		return ADDRESS_HOME;
	}

	public void setADDRESS_HOME(String aDDRESS_HOME) {
		ADDRESS_HOME = aDDRESS_HOME;
	}

	public String getZIPCODE() {
		return ZIPCODE;
	}

	public void setZIPCODE(String zIPCODE) {
		ZIPCODE = zIPCODE;
	}

	public String getFLAG_BLACKLIST() {
		return FLAG_BLACKLIST;
	}

	public void setFLAG_BLACKLIST(String fLAG_BLACKLIST) {
		FLAG_BLACKLIST = fLAG_BLACKLIST;
	}
	public int getSTAT1() {
		return STAT1;
	}
	public void setSTAT1(int sTAT1) {
		STAT1 = sTAT1;
	}
	public int getSTAT2() {
		return STAT2;
	}
	public void setSTAT2(int sTAT2) {
		STAT2 = sTAT2;
	}
	public int getSTAT3() {
		return STAT3;
	}
	public void setSTAT3(int sTAT3) {
		STAT3 = sTAT3;
	}
	public String getLANE() {
		return LANE;
	}

	public void setLANE(String lANE) {
		LANE = lANE;
	}

	public String getFLAG_WEB() {
		return FLAG_WEB;
	}

	public void setFLAG_WEB(String fLAG_WEB) {
		FLAG_WEB = fLAG_WEB;
	}
	
	public String getFLAG_DMB_SEND() {
		return FLAG_DMB_SEND;
	}

	public void setFLAG_DMB_SEND(String fLAG_DMB_SEND) {
		FLAG_DMB_SEND = fLAG_DMB_SEND;
	}

	@Override
	public String toString() {
		return "ReceiptVO [RECEIPT_ID=" + RECEIPT_ID + ", INDIVIDUAL_ID=" + INDIVIDUAL_ID + ", INDIVIDUAL_NAME="
				+ INDIVIDUAL_NAME + ", INDIVIDUAL_TYPE=" + INDIVIDUAL_TYPE + ", ORG_ID=" + ORG_ID + ", ORG_NAME="
				+ ORG_NAME + ", RECEPTION_ID=" + RECEPTION_ID + ", RECEPTION_NAME=" + RECEPTION_NAME + ", MEMO=" + MEMO
				+ ", ARTERY_ID=" + ARTERY_ID + ", F_LINK_ID=" + F_LINK_ID + ", T_LINK_ID=" + T_LINK_ID
				+ ", F_NODE_NAME=" + F_NODE_NAME + ", T_NODE_NAME=" + T_NODE_NAME + ", REGION_ID=" + REGION_ID
				+ ", START_DAY=" + START_DAY + ", STARTTIMEHH=" + STARTTIMEHH + ", STARTTIMEMI=" + STARTTIMEMI
				+ ", END_DAY=" + END_DAY + ", ENDTIMEHH=" + ENDTIMEHH + ", ENDTIMEMI=" + ENDTIMEMI + ", REPORT_TYPE="
				+ REPORT_TYPE + ", REPORTER_TYPE=" + REPORTER_TYPE + ", FLAG_SEND=" + FLAG_SEND + ", FLAG_IMPORTANT="
				+ FLAG_IMPORTANT + ", FLAG_DISASTOR=" + FLAG_DISASTOR + ", R_TEL=" + R_TEL + ", X_COORDINATE="
				+ X_COORDINATE + ", Y_COORDINATE=" + Y_COORDINATE + ", REPORT_TYPE2=" + REPORT_TYPE2 + ", REPORT_TYPE3="
				+ REPORT_TYPE3 + ", AREA_ID=" + AREA_ID + ", LANE=" + LANE + ", INFORMER_ID=" + INFORMER_ID
				+ ", ACT_ID=" + ACT_ID + ", INFORMER_NAME=" + INFORMER_NAME + ", REGIONSUB_ID=" + REGIONSUB_ID
				+ ", REGIONSUB_NAME=" + REGIONSUB_NAME + ", PHONE_CELL=" + PHONE_CELL + ", PHONE_HOME=" + PHONE_HOME
				+ ", INFORMER_TYPE=" + INFORMER_TYPE + ", ADDRESS_HOME=" + ADDRESS_HOME + ", ZIPCODE=" + ZIPCODE
				+ ", FLAG_BLACKLIST=" + FLAG_BLACKLIST + ", STAT1=" + STAT1 + ", STAT2=" + STAT2 + ", STAT3=" + STAT3
				+ "]";
	}


}
