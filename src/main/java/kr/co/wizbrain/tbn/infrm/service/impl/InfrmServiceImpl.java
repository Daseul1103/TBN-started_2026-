package kr.co.wizbrain.tbn.infrm.service.impl;

import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import kr.co.wizbrain.tbn.infrm.mapper.InfrmMapper;
import kr.co.wizbrain.tbn.infrm.service.InfrmService;
import kr.co.wizbrain.tbn.infrm.vo.InfrmVO;

/**
 * 사용자 서비스 구현 클래스
 * @author 미래전략사업팀 정다빈
 * @since 2020.07.23
 * @version 1.0
 * @see
 *
 * << 개정이력(Modification Information) >>
 *
 *   수정일            수정자              수정내용
 *  -------    -------- ---------------------------
 *  2020.07.23  정다빈           최초 생성
 */

@Service("infrmService")
public class InfrmServiceImpl implements InfrmService{
	
	@Resource(name="infrmMapper")
	private InfrmMapper infrmMapper;
	
	//전체 회원정보 조회
	public List<InfrmVO> selectInfrmList(InfrmVO infrmVO, List<String> selectedCols) throws Exception{
		System.out.println(infrmVO);
		List<InfrmVO> tlist = infrmMapper.selectInfrmList(infrmVO,selectedCols);
		return tlist;
	}
	
	public List<InfrmVO> countInfrmList(InfrmVO infrmVO) throws Exception{
		System.out.println(infrmVO);
		List<InfrmVO> tlist = infrmMapper.countInfrmList(infrmVO);
		return tlist;
	}
	
	//전체 회원정보 조회(스크롤)
	public List<InfrmVO> selectInfrmList2(InfrmVO infrmVO, int startRnum , int endRnum) throws Exception{
		System.out.println(infrmVO);
		List<InfrmVO> tlist = infrmMapper.selectInfrmList2(infrmVO,startRnum,endRnum);
		return tlist;
	}
	
	//특정 사용자 조회
	public InfrmVO selectInfrm(InfrmVO infrmVO) throws Exception {
		InfrmVO rvo = infrmMapper.selectInfrmList20(infrmVO).get(0);
		rvo.setPhoneCell(rvo.getPhoneCell().replace("-", ""));
		return rvo;
	}

	@Override
	public String creActId(InfrmVO thvo) throws Exception {
		return infrmMapper.creActId(thvo);
	}

	@Override
	public String getInformerId(InfrmVO infrmVO) throws Exception {
		InfrmVO rvo = infrmMapper.selectInfrmList20(infrmVO).get(0);
		return rvo.getInformerId();
	}

	@Override
	public int saveInformer(InfrmVO paramVO) {
		int cnt = 0;
		
		
		// 26-06-30 : 개인정보 암호화 
		try {

	        if(paramVO.getInformerName() != null && !"".equals(paramVO.getInformerName())) {
	            paramVO.setInformerName(encryptAES(paramVO.getInformerName()));
	        }

	        if(paramVO.getPhoneCell() != null && !"".equals(paramVO.getPhoneCell())) {
	            paramVO.setPhoneCell(encryptAES(paramVO.getPhoneCell()));
	        }

	        if(paramVO.getAddress() != null && !"".equals(paramVO.getAddress())) {
	            paramVO.setAddress(encryptAES(paramVO.getAddress()));
	        }
	        
	        if(paramVO.getAddressHome() != null && !"".equals(paramVO.getAddressHome())) {
	            paramVO.setAddressHome(encryptAES(paramVO.getAddressHome()));
	        }

	        if(paramVO.getBirthday() != null && !"".equals(paramVO.getBirthday())) {
	            paramVO.setBirthday(encryptAES(paramVO.getBirthday()));
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 암호화 실패", e);
	    }
		
		
		if(paramVO.getPageDiv().equals("new")){
			cnt = infrmMapper.saveInformer(paramVO);
		}else {
			cnt = infrmMapper.updateInformer(paramVO);
		}
		
		return cnt;
	}
	
	
	@Override
	public int saveInformerApp(InfrmVO paramVO) {
		int cnt = 0;
		
		// 26-06-30 : 개인정보 암호화(전화번호, 주소, 이름, 생년월일)
		
		try {

	        if(paramVO.getInformerName() != null && !"".equals(paramVO.getInformerName())) {
	            paramVO.setInformerName(encryptAES(paramVO.getInformerName()));
	        }

	        if(paramVO.getPhoneCell() != null && !"".equals(paramVO.getPhoneCell())) {
	            paramVO.setPhoneCell(encryptAES(paramVO.getPhoneCell()));
	        }

	        if(paramVO.getAddress() != null && !"".equals(paramVO.getAddress())) {
	            paramVO.setAddress(encryptAES(paramVO.getAddress()));
	        }
	        
	        if(paramVO.getAddressHome() != null && !"".equals(paramVO.getAddressHome())) {
	            paramVO.setAddressHome(encryptAES(paramVO.getAddressHome()));
	        }

	        if(paramVO.getBirthday() != null && !"".equals(paramVO.getBirthday())) {
	            paramVO.setBirthday(encryptAES(paramVO.getBirthday()));
	        }

	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 암호화 실패", e);
	    }
		
		cnt = infrmMapper.saveInformerApp(paramVO);
		
		return cnt;
	}
	
	
	@Override
	public int uptInformerApp(InfrmVO paramVO) {
		int cnt = 0;
		cnt = infrmMapper.uptInformerApp(paramVO);
		
		return cnt;
	}

	@Override
	public int deleteApp(String applyId) {
		int cnt = 0;
		cnt = infrmMapper.deleteApp(applyId);
		
		return cnt;
	}
	

	@Override
	public int rejectApp(String applyId) {
		int cnt = 0;
		cnt = infrmMapper.rejectApp(applyId);
		
		return cnt;
	}
	
	
	
	
	@Override
	public void saveInformerHist(InfrmVO paramVO) {
		infrmMapper.saveInformerHist(paramVO);
	}

	@Override
	public String getUpdateCode(InfrmVO paramVO) {
		return infrmMapper.getUpdateCode(paramVO);
	}

	@Override
	public void getAppUpdateCode(InfrmVO paramVO) {
		infrmMapper.getAppUpdateCode(paramVO);
	}
	
	@Override
	public int deleteInformer(InfrmVO paramVO) {
		return infrmMapper.deleteInformer(paramVO);
	}
	
	@Override
	public int chkDupInfrm(String phoneCell) throws Exception {
		return infrmMapper.chkDupInfrm(phoneCell);
	}
	
	@Override
	public void goCancle (String applyId) throws Exception {
		infrmMapper.goCancle(applyId);
	}

	@Override
	public List<InfrmVO> getInformerHistory(InfrmVO ifmVO) {
		return infrmMapper.getInformerHistory(ifmVO);
	}
	
	@Override
	public List<InfrmVO> getInformerHistoryList(InfrmVO ifmVO) {
		return infrmMapper.getInformerHistoryList(ifmVO);
	}


	@Override
	public String getNewId(InfrmVO thvo) throws Exception {
		return infrmMapper.getNewId(thvo);
	}


	@Override
	public InfrmVO detailInformer(InfrmVO thvo) {
		InfrmVO vo = infrmMapper.detailInformer(thvo);
		
		try {

	        if(vo != null) {

	            if(vo.getInformerName() != null && !"".equals(vo.getInformerName()))
	                vo.setInformerName(decryptAES(vo.getInformerName()));

	            if(vo.getPhoneCell() != null && !"".equals(vo.getPhoneCell()))
	                vo.setPhoneCell(decryptAES(vo.getPhoneCell()));

	            if(vo.getAddress() != null && !"".equals(vo.getAddress()))
	                vo.setAddress(decryptAES(vo.getAddress()));
	            
	            if(vo.getAddressHome() != null && !"".equals(vo.getAddressHome()))
	                vo.setAddressHome(decryptAES(vo.getAddressHome()));

	            if(vo.getBirthday() != null && !"".equals(vo.getBirthday()))
	                vo.setBirthday(decryptAES(vo.getBirthday()));

	        }

	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }
		
		return vo;
	}
	
	@Override
	public int encryptAllInformer() {

	    List<InfrmVO> list = infrmMapper.selectAllInformer();

	    int cnt = 0;

	    for(InfrmVO vo : list){

	        try{

	            if(vo.getInformerName() != null && !vo.getInformerName().isEmpty())
	                vo.setInformerName(encryptAES(vo.getInformerName()));

	            if(vo.getPhoneCell() != null && !vo.getPhoneCell().isEmpty())
	                vo.setPhoneCell(encryptAES(vo.getPhoneCell()));

	            if(vo.getAddress() != null && !vo.getAddress().isEmpty())
	                vo.setAddress(encryptAES(vo.getAddress()));
	            
	            if(vo.getAddressHome() != null && !vo.getAddressHome().isEmpty())
	                vo.setAddressHome(encryptAES(vo.getAddressHome()));

	            if(vo.getBirthday() != null && !vo.getBirthday().isEmpty())
	                vo.setBirthday(encryptAES(vo.getBirthday()));

	            cnt += infrmMapper.updateEncryptInformer(vo);

	        }catch(Exception e){
	            e.printStackTrace();
	        }
	    }

	    return cnt;
	}

	
	@Override
	public InfrmVO detailApplyIfrm(InfrmVO thvo) {
		return infrmMapper.detailApplyIfrm(thvo);
	}

	@Override
	public String chkPhone(InfrmVO ifmVO) throws Exception{
		return infrmMapper.chkPhone(ifmVO);
	}
	
	
	// 24-11-21 : 통신원 월별 제보건수
	@Override
	public List<InfrmVO> monthReport(String selectYear, String informerId) throws Exception {
		String selectYear1 = selectYear + "-01-01";
		String selectYear2 = selectYear + "-12-31";
		
		return infrmMapper.monthReport(selectYear1,selectYear2, informerId);
	}

	@Override
	public long countAll(InfrmVO thvo) {
		return infrmMapper.countAll(thvo);
	}

	@Override
	public long countFiltered(InfrmVO thvo){
		return infrmMapper.countFiltered(thvo);
	}

	@Override
	public long countFilteredAPP(InfrmVO thvo){
		return infrmMapper.countFilteredAPP(thvo);
	}
	
	
	@Override
	public List<InfrmVO> findSliceAPP(InfrmVO thvo, int startRnum, int endRnum, String orderby) {
		return infrmMapper.findSliceAPP(thvo,startRnum,endRnum,orderby);
	}
	
	
	@Override
	public long applyAppCountFiltered(InfrmVO thvo){
		return infrmMapper.applyAppCountFiltered(thvo);
	}

	@Override
	public List<InfrmVO> findSlice(InfrmVO thvo, int startRnum, int endRnum, String orderBy){

	    List<InfrmVO> list = infrmMapper.findSlice(thvo, startRnum, endRnum, orderBy);

	    try {
	        for(InfrmVO vo : list){

	            if(vo.getInformerName() != null && !"".equals(vo.getInformerName()))
	                vo.setInformerName(decryptAES(vo.getInformerName()));

	            if(vo.getPhoneCell() != null && !"".equals(vo.getPhoneCell()))
	                vo.setPhoneCell(decryptAES(vo.getPhoneCell()));

	            if(vo.getAddress() != null && !"".equals(vo.getAddress()))
	                vo.setAddress(decryptAES(vo.getAddress()));

	            if(vo.getBirthday() != null && !"".equals(vo.getBirthday()))
	                vo.setBirthday(decryptAES(vo.getBirthday()));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }

	    return list;
	}
	
	@Override
	public List<InfrmVO> applyAppfindSlice(InfrmVO thvo, int startRnum, int endRnum, String orderBy){
		return infrmMapper.applyAppfindSlice(thvo,startRnum,endRnum,orderBy);
	}

	
	private String encryptAES(String plainText) throws Exception {

	    String secretKey = "1234567890123456";

	    SecretKeySpec keySpec =
	            new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

	    Cipher cipher = Cipher.getInstance("AES");

	    cipher.init(Cipher.ENCRYPT_MODE, keySpec);

	    byte[] encrypted =
	            cipher.doFinal(plainText.getBytes("UTF-8"));

	    return Base64.getEncoder().encodeToString(encrypted);
	}
	
	
	private String decryptAES(String encryptedText) throws Exception {

	    String secretKey = "1234567890123456";

	    SecretKeySpec keySpec =
	            new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

	    Cipher cipher = Cipher.getInstance("AES");

	    cipher.init(Cipher.DECRYPT_MODE, keySpec);

	    byte[] decoded =
	            Base64.getDecoder().decode(encryptedText);

	    return new String(cipher.doFinal(decoded), "UTF-8");
	}

}
