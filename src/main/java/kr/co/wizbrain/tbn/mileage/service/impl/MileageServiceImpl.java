package kr.co.wizbrain.tbn.mileage.service.impl;

import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import kr.co.wizbrain.tbn.award.vo.AwardVO;
import kr.co.wizbrain.tbn.comm.RecordDto;
import kr.co.wizbrain.tbn.mileage.mapper.MileageMapper;
import kr.co.wizbrain.tbn.mileage.service.MileageService;
import kr.co.wizbrain.tbn.mileage.vo.MileageVO;
import kr.co.wizbrain.tbn.option.vo.OptAreaVo;

@Service("mileageService")
public class MileageServiceImpl implements MileageService {
	
	@Resource(name="mileageMapper")
	private MileageMapper mileageMapper;
	
	
	
	// 굿 제보 마일리지 조회
	@Override
	public List<MileageVO> mileList(MileageVO MileageVO) throws Exception {
		List<MileageVO> list = mileageMapper.mileList(MileageVO);
		
		try {
	        for(MileageVO vo : list){

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
	
	// 우수 제보자 조회
	@Override
	public List<MileageVO> excellenceList(MileageVO MileageVO) throws Exception {
		List<MileageVO> list =  mileageMapper.excellenceList(MileageVO);
		
		try {
	        for(MileageVO vo : list){

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

	// 시상별 배점 갱신
	public void updateGrade(AwardVO thvo) {
		mileageMapper.updateGrade(thvo);
	}
	

	// 우수 제보자 조회 기능
	@Override
	public List<AwardVO> getExcellInformerList(AwardVO thvo) throws Exception {
		List<AwardVO> list =  mileageMapper.getExcellInformerList(thvo);
		
		try {
		    for (AwardVO vo : list) {

		        // 이름
		        if (vo.getINFORMER_NAME() == null || "".equals(vo.getINFORMER_NAME())) {
		            vo.setINFORMER_NAME("미등록시민");
		        } else {
		            vo.setINFORMER_NAME(decryptAES(vo.getINFORMER_NAME()));
		        }

		        // 전화번호
		        if (vo.getPHONE_CELL() == null || "".equals(vo.getPHONE_CELL())) {

		            if (vo.getINFORMER_ID() != null && vo.getINFORMER_ID().length() >= 15) {

		                String phone = vo.getINFORMER_ID().substring(4);

		                phone = phone.replaceAll(
		                        "(02|.{3})(.+)(.{4})",
		                        "$1-$2-$3");

		                vo.setPHONE_CELL(phone);
		            }

		        } else {

		            vo.setPHONE_CELL(decryptAES(vo.getPHONE_CELL()));

		            // 복호화 후 하이픈 추가
		            vo.setPHONE_CELL(
		                vo.getPHONE_CELL().replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3"));

		        }

		    }
		} catch (Exception e) {
		    throw new RuntimeException("개인정보 복호화 실패", e);
		}
		
		return list;
	}
	
	// 시상별 배점 조회
	@Override
	public List<AwardVO> excellGrade(OptAreaVo thvo) {
		return mileageMapper.excellGrade(thvo);
	}
	
	// 굿 제보 통신원 등록
	@Override
	public void excellenceIfrmInsert(MileageVO mvo, List<String> selection) throws Exception {
		mileageMapper.excellenceIfrmInsert(mvo, selection);
	}
	
	// 굿 제보 통신원 선정 취소
	@Override
	public void excellenceIfrmDelete(MileageVO mvo, List<String> selection) throws Exception {
		mileageMapper.excellenceIfrmDelete(mvo, selection);
	}
	
	// 우수 제보자 > 수상자 조회 
	@Override
	public List<AwardVO> selectUserAwardList(AwardVO thvo) throws Exception {
		List<AwardVO> list = mileageMapper.selectUserAwardList(thvo);
		
		for (AwardVO vo : list) {

		    // 이름
		    if (vo.getINFORMER_NAME() == null || "".equals(vo.getINFORMER_NAME())) {
		        vo.setINFORMER_NAME("미등록시민");
		    } else {
		        vo.setINFORMER_NAME(decryptAES(vo.getINFORMER_NAME()));
		    }

		    // 전화번호
		    if (vo.getPHONE_CELL() == null || "".equals(vo.getPHONE_CELL())) {

		        // AW_ID에서 전화번호 추출
		        String awId = vo.getAW_ID();

		        if (awId != null && awId.contains("_")) {

		            String phone = awId.substring(awId.indexOf('_') + 1);

		            // 뒤 14자리(등록일시) 제거
		            phone = phone.substring(0, phone.length() - 14);

		            // 하이픈 추가
		            phone = phone.replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3");

		            vo.setPHONE_CELL(phone);
		        }

		    } else {

		        vo.setPHONE_CELL(decryptAES(vo.getPHONE_CELL()));

		        // 복호화 후 하이픈 추가
		        vo.setPHONE_CELL(
		            vo.getPHONE_CELL().replaceAll(
		                "(02|.{3})(.+)(.{4})",
		                "$1-$2-$3"));
		    }
		}
		
		return list;
	}
	
	
	
	// 시상 저장
	@Override
	public void saveAward(AwardVO thvo, List<AwardVO> alist) throws Exception {
		mileageMapper.saveAward(thvo,alist);
	}
	
	
	// 시상 삭제
	@Override
	public void deleteAward(String[] slt) throws Exception {
		mileageMapper.deleteAward(slt);
	}
	
	
	// 우수 제보자 > 수상자 선정 엑셀 다운로드
	@Override
	public List<RecordDto> getAwardInformerList2(AwardVO paramVO) {
		List<RecordDto> list = mileageMapper.getAwardInformerList2(paramVO);
		
		try {

		    for (RecordDto row : list) {

		        // 이름
		        Object name = row.get("INFORMER_NAME");

		        if (name == null || "".equals(name.toString())) {
		            row.put("INFORMER_NAME", "미등록시민");
		        } else {
		            row.put("INFORMER_NAME",
		                    decryptAES(name.toString()));
		        }

		        // 전화번호
		        Object phone = row.get("PHONE_CELL");

		        if (phone == null || "".equals(phone.toString())) {

		            String informerId =
		                    row.get("INFORMER_ID").toString();

		            String tel = informerId.substring(4);

		            tel = tel.replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3");

		            row.put("PHONE_CELL", tel);

		        } else {

		            String tel =
		                    decryptAES(phone.toString());

		            tel = tel.replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3");

		            row.put("PHONE_CELL", tel);
		        }

		    }

		} catch (Exception e) {
		    throw new RuntimeException("개인정보 복호화 실패", e);
		}
		
		return list;
	}
	
	
	// 우수 제보자 > 수상자 조회 엑셀 다운로드
	@Override
	public List<RecordDto> selectUserAwardList2(AwardVO paramVO) {
		List<RecordDto> list = mileageMapper.selectUserAwardList2(paramVO);
		
		try {

		    for (RecordDto row : list) {

		        // 이름
		        Object name = row.get("INFORMER_NAME");

		        if (name == null || "".equals(name.toString())) {
		            row.put("INFORMER_NAME", "미등록시민");
		        } else {
		            row.put("INFORMER_NAME",
		                    decryptAES(name.toString()));
		        }

		        // 전화번호
		        Object phone = row.get("PHONE_CELL");

		        if (phone == null || "".equals(phone.toString())) {

		            String informerId =
		                    row.get("INFORMER_ID").toString();

		            String tel = informerId.substring(4);

		            tel = tel.replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3");

		            row.put("PHONE_CELL", tel);

		        } else {

		            String tel =
		                    decryptAES(phone.toString());

		            tel = tel.replaceAll(
		                    "(02|.{3})(.+)(.{4})",
		                    "$1-$2-$3");

		            row.put("PHONE_CELL", tel);
		        }

		    }

		} catch (Exception e) {
		    throw new RuntimeException("개인정보 복호화 실패", e);
		}
		
		return list;
	}
	
	// 최고 통신원 기본 정보 조회
	@Override
	public List<MileageVO> bestIfrmList(MileageVO MileageVO){
		List<MileageVO> list = mileageMapper.bestIfrmList(MileageVO);
		
		try {
	        for(MileageVO vo : list){

	            if(vo.getINFORMER_NAME() != null && !"".equals(vo.getINFORMER_NAME()))
	            	vo.setINFORMER_NAME(decryptAES(vo.getINFORMER_NAME()));

	        }
	    } catch (Exception e) {
	        throw new RuntimeException("개인정보 복호화 실패", e);
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
