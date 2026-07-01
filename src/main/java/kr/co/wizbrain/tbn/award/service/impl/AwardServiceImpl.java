package kr.co.wizbrain.tbn.award.service.impl;


import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import kr.co.wizbrain.tbn.award.mapper.AwardMapper;
import kr.co.wizbrain.tbn.award.service.AwardService;
import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;

@Service("awardService")
public class AwardServiceImpl implements AwardService{
	@Resource(name="awardMapper")
	private AwardMapper awardMapper;

	// 수상자선정 조회
	@Override
	public List<AwardVO> getAwardInformerList(AwardVO thvo) throws Exception {
		List<AwardVO> list = awardMapper.getAwardInformerList(thvo);
		
		// 개인 정보 암호화 (시상 관리 첫 진입 - 이름, 전화번호)
		try {
	        for(AwardVO vo : list){

	            if(vo.getINFORMER_NAME() != null && !"".equals(vo.getINFORMER_NAME()))
	                vo.setINFORMER_NAME(decryptAES(vo.getINFORMER_NAME()));

	            if(vo.getPHONE_CELL() != null && !"".equals(vo.getPHONE_CELL()))
	                vo.setPHONE_CELL(decryptAES(vo.getPHONE_CELL()));

	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }
		
		return list;
	}

	@Override
	public int selectAwardCnt() throws Exception {
//		String awardCnt = ""; 
//		awardCnt = (String)select("award.selectAwardCnt",null);
//		int cnt = 0;
//		if(awardCnt == null){
//			cnt = 0;
//		}else{
//			cnt  = Integer.parseInt(awardCnt);
//		}
//		 return cnt;
		 return 0;
	}

	@Override
	public void saveAward(AwardVO thvo, List<AwardVO> alist) throws Exception {
		awardMapper.saveAward(thvo,alist);
	}
	
	@Override
	public void deleteAward(String[] slt) throws Exception {
		awardMapper.deleteAward(slt);
	}

	
	// 수상자 조회
	@Override
	public List<AwardVO> selectUserAwardList(AwardVO thvo) throws Exception {
		
		List<AwardVO> list = awardMapper.selectUserAwardList(thvo);
		
		// 개인 정보 암호화 (시상 관리 첫 진입 - 이름, 전화번호)
		try {
	        for(AwardVO vo : list){

	            if(vo.getINFORMER_NAME() != null && !"".equals(vo.getINFORMER_NAME()))
	                vo.setINFORMER_NAME(decryptAES(vo.getINFORMER_NAME()));

	            if(vo.getPHONE_CELL() != null && !"".equals(vo.getPHONE_CELL()))
	                vo.setPHONE_CELL(decryptAES(vo.getPHONE_CELL()));

	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
	    }
		
		return list;
	}

	@Override
	public List<AwardVO> getAwardList(AwardVO awvo) {
		return awardMapper.getAwardList(awvo);
	}

	@Override
	public List<AwardVO> selectGrade(AwardVO thvo) {
		return awardMapper.selectGrade(thvo);
	}

	@Override
	public void updateGrade(AwardVO thvo) {
		awardMapper.updateGrade(thvo);
	}

	
	
	// 수상자 선정 > 엑셀 다운로드용 조회
	@Override
	public List<RecordDto> getAwardInformerList2(AwardVO paramVO) {
		List<RecordDto> list = awardMapper.getAwardInformerList2(paramVO);

		for (RecordDto dto : list) {
		    try {

		        if(dto.get("INFORMER_NAME") != null) {
		            dto.put("INFORMER_NAME",
		                    decryptAES(dto.get("INFORMER_NAME").toString()));
		        }

		        if(dto.get("PHONE_CELL") != null) {
		            dto.put("PHONE_CELL",
		                    decryptAES(dto.get("PHONE_CELL").toString()));
		        }

		    } catch (Exception e) {
		        throw new RuntimeException("개인정보 복호화 실패", e);
		    }
		}

		return list;
	}

	
	
	
	// 수상자 조회 > 엑셀 다운로드용 조회
	@Override
	public List<RecordDto> selectUserAwardList2(AwardVO paramVO) {
		List<RecordDto> list = awardMapper.selectUserAwardList2(paramVO);
		
		for (RecordDto dto : list) {
		    try {

		        if(dto.get("INFORMER_NAME") != null) {
		            dto.put("INFORMER_NAME",
		                    decryptAES(dto.get("INFORMER_NAME").toString()));
		        }

		        if(dto.get("PHONE_CELL") != null) {
		            dto.put("PHONE_CELL",
		                    decryptAES(dto.get("PHONE_CELL").toString()));
		        }

		    } catch (Exception e) {
		        throw new RuntimeException("개인정보 복호화 실패", e);
		    }
		}

		return list;
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