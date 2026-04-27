package kr.co.wizbrain.tbn.receipt.vo.popup;

public class ReceivedStatusVO {
	private String RNUM;
	private String RECEIPT_ID; //
	private String RECEIPT_DAY; //
	private String FLAG_SEND; //
	private String FLAG_BROD; //
	private String FLAG_CHK;
	
	private String FLAG_MAP; //
	private String RECEIPT_TIME; //
	private String REPORT_TYPE; //
	private String REPORT_TYPE2; //
	private String INDIVIDUAL_ID;
	
	private String INDIVIDUAL_NAME; //
	private String TYPE_NAME; //TEXT
	private String RECEPTION_ID;
	private String FLAG_SITU_ED;

	private String RECEPTION_NAME; //
	private String MEMO; //
	
	private String REPORTER_TYPE; //TEXT
	private String REGION_NAME; //TEXT
	private String F_COLOR;
	private String B_COLOR;
	private String FLAG_IMPORTANT; //중요제보에 따라 색 구분
	private String FLAG_DISASTOR;		//재난제보
	private String BROAD_TIME; //방송시간
	
	private String PHONE_CELL; // 전화번호
	
	public ReceivedStatusVO() {}

	public ReceivedStatusVO(String rNUM, String rECEIPT_ID, String rECEIPT_DAY, String fLAG_SEND, String fLAG_BROD,
			String fLAG_MAP, String rECEIPT_TIME, String rEPORT_TYPE, String rEPORT_TYPE2, String iNDIVIDUAL_ID,
			String iNDIVIDUAL_NAME, String tYPE_NAME, String rECEPTION_ID, String rECEPTION_NAME, String mEMO,
			String rEPORTER_TYPE, String rEGION_NAME, String f_COLOR, String b_COLOR, String fLAG_IMPORTANT,
			String bROAD_TIME) {
		RNUM = rNUM;
		RECEIPT_ID = rECEIPT_ID;
		RECEIPT_DAY = rECEIPT_DAY;
		FLAG_SEND = fLAG_SEND;
		FLAG_BROD = fLAG_BROD;
		FLAG_MAP = fLAG_MAP;
		RECEIPT_TIME = rECEIPT_TIME;
		REPORT_TYPE = rEPORT_TYPE;
		REPORT_TYPE2 = rEPORT_TYPE2;
		INDIVIDUAL_ID = iNDIVIDUAL_ID;
		INDIVIDUAL_NAME = iNDIVIDUAL_NAME;
		TYPE_NAME = tYPE_NAME;
		RECEPTION_ID = rECEPTION_ID;
		RECEPTION_NAME = rECEPTION_NAME;
		MEMO = mEMO;
		REPORTER_TYPE = rEPORTER_TYPE;
		REGION_NAME = rEGION_NAME;
		F_COLOR = f_COLOR;
		B_COLOR = b_COLOR;
		FLAG_IMPORTANT = fLAG_IMPORTANT;
		BROAD_TIME = bROAD_TIME;
	}
	
	
	
	public String getPHONE_CELL() {
		return PHONE_CELL;
	}

	public void setPHONE_CELL(String pHONE_CELL) {
		PHONE_CELL = pHONE_CELL;
	}

	public String getFLAG_SITU_ED() {
		return FLAG_SITU_ED;
	}

	public void setFLAG_SITU_ED(String fLAG_SITU_ED) {
		FLAG_SITU_ED = fLAG_SITU_ED;
	}

	public String getFLAG_CHK() {
		return FLAG_CHK;
	}

	public void setFLAG_CHK(String fLAG_CHK) {
		FLAG_CHK = fLAG_CHK;
	}
	
	public String getFLAG_DISASTOR() {
		return FLAG_DISASTOR;
	}

	public void setFLAG_DISASTOR(String fLAG_DISASTOR) {
		FLAG_DISASTOR = fLAG_DISASTOR;
	}

	public String getRNUM() {
		return RNUM;
	}
	public void setRNUM(String rNUM) {
		RNUM = rNUM;
	}
	public String getRECEIPT_ID() {
		return RECEIPT_ID;
	}
	public void setRECEIPT_ID(String rECEIPT_ID) {
		RECEIPT_ID = rECEIPT_ID;
	}
	public String getRECEIPT_DAY() {
		return RECEIPT_DAY;
	}
	public void setRECEIPT_DAY(String rECEIPT_DAY) {
		RECEIPT_DAY = rECEIPT_DAY;
	}
	public String getFLAG_SEND() {
		return FLAG_SEND;
	}
	public void setFLAG_SEND(String fLAG_SEND) {
		FLAG_SEND = fLAG_SEND;
	}
	public String getFLAG_BROD() {
		return FLAG_BROD;
	}
	public void setFLAG_BROD(String fLAG_BROD) {
		FLAG_BROD = fLAG_BROD;
	}
	public String getFLAG_MAP() {
		return FLAG_MAP;
	}
	public void setFLAG_MAP(String fLAG_MAP) {
		FLAG_MAP = fLAG_MAP;
	}
	public String getRECEIPT_TIME() {
		return RECEIPT_TIME;
	}
	public void setRECEIPT_TIME(String rECEIPT_TIME) {
		RECEIPT_TIME = rECEIPT_TIME;
	}
	public String getREPORT_TYPE() {
		return REPORT_TYPE;
	}
	public void setREPORT_TYPE(String rEPORT_TYPE) {
		REPORT_TYPE = rEPORT_TYPE;
	}
	public String getREPORT_TYPE2() {
		return REPORT_TYPE2;
	}
	public void setREPORT_TYPE2(String rEPORT_TYPE2) {
		REPORT_TYPE2 = rEPORT_TYPE2;
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
	public String getTYPE_NAME() {
		return TYPE_NAME;
	}
	public void setTYPE_NAME(String tYPE_NAME) {
		TYPE_NAME = tYPE_NAME;
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
	public String getREPORTER_TYPE() {
		return REPORTER_TYPE;
	}
	public void setREPORTER_TYPE(String rEPORTER_TYPE) {
		REPORTER_TYPE = rEPORTER_TYPE;
	}
	public String getREGION_NAME() {
		return REGION_NAME;
	}
	public void setREGION_NAME(String rEGION_NAME) {
		REGION_NAME = rEGION_NAME;
	}
	public String getF_COLOR() {
		return F_COLOR;
	}
	public void setF_COLOR(String f_COLOR) {
		F_COLOR = f_COLOR;
	}
	public String getB_COLOR() {
		return B_COLOR;
	}
	public void setB_COLOR(String b_COLOR) {
		B_COLOR = b_COLOR;
	}
	public String getFLAG_IMPORTANT() {
		return FLAG_IMPORTANT;
	}
	public void setFLAG_IMPORTANT(String fLAG_IMPORTANT) {
		FLAG_IMPORTANT = fLAG_IMPORTANT;
	}
	
	public String getBROAD_TIME() {
		return BROAD_TIME;
	}
	public void setBROAD_TIME(String bROAD_TIME) {
		BROAD_TIME = bROAD_TIME;
	}

	@Override
	public String toString() {
		return "ReceivedStatusVO [RNUM=" + RNUM + ", RECEIPT_ID=" + RECEIPT_ID + ", RECEIPT_DAY=" + RECEIPT_DAY
				+ ", FLAG_SEND=" + FLAG_SEND + ", FLAG_BROD=" + FLAG_BROD + ", FLAG_MAP=" + FLAG_MAP + ", RECEIPT_TIME="
				+ RECEIPT_TIME + ", REPORT_TYPE=" + REPORT_TYPE + ", REPORT_TYPE2=" + REPORT_TYPE2 + ", INDIVIDUAL_ID="
				+ INDIVIDUAL_ID + ", INDIVIDUAL_NAME=" + INDIVIDUAL_NAME + ", TYPE_NAME=" + TYPE_NAME
				+ ", RECEPTION_ID=" + RECEPTION_ID + ", RECEPTION_NAME=" + RECEPTION_NAME + ", MEMO=" + MEMO
				+ ", REPORTER_TYPE=" + REPORTER_TYPE + ", REGION_NAME=" + REGION_NAME + ", F_COLOR=" + F_COLOR
				+ ", B_COLOR=" + B_COLOR + ", FLAG_IMPORTANT=" + FLAG_IMPORTANT + ", BROAD_TIME=" + BROAD_TIME + "]";
	}
	
}